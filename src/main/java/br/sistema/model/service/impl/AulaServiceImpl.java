package br.sistema.model.service.impl;

import br.sistema.model.entity.Aula;
import br.sistema.model.entity.Disciplina;
import br.sistema.model.entity.Professor;
import br.sistema.model.entity.Turma;
import br.sistema.model.repository.interfaces.AulaRepository;
import br.sistema.model.repository.interfaces.ProfessorRepository;
import br.sistema.model.service.interfaces.AulaService;
import br.sistema.model.exception.BusinessException;
import br.sistema.model.exception.NotFoundException;
import br.sistema.model.exception.ValidationException;

import java.time.DayOfWeek;
import java.util.*;

public class AulaServiceImpl implements AulaService {

    private final AulaRepository aulaRepository;
    private final ProfessorRepository professorRepository;

    public AulaServiceImpl(AulaRepository aulaRepository, ProfessorRepository professorRepository) {
        this.aulaRepository = aulaRepository;
        this.professorRepository = professorRepository;
    }

    private final List<DayOfWeek> DIAS_LETIVOS = Arrays.asList(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
    );

    private record Horario(DayOfWeek dia, int slot) {}

    @Override
    public void gerarGrade(Turma turma, Map<Disciplina, Integer> cargaHoraria) {
        if (turma == null || turma.getId() == null) {
            throw new ValidationException("Turma inválida para geração da grade.");
        }
        if (cargaHoraria == null || cargaHoraria.isEmpty()) {
            throw new ValidationException("Carga horária não informada.");
        }

        // 1. Preparar lista de disciplinas pendentes (carga horária)
        List<Disciplina> disciplinasPendentes = new ArrayList<>();
        for (Map.Entry<Disciplina, Integer> entrada : cargaHoraria.entrySet()) {
            Disciplina d = entrada.getKey();
            int qtd = entrada.getValue();
            for (int i = 0; i < qtd; i++) {
                disciplinasPendentes.add(d);
            }
        }

        // 2. Preparar e embaralhar horários para melhor distribuição
        List<Horario> todosHorarios = new ArrayList<>();
        for (DayOfWeek dia : DIAS_LETIVOS) {
            for (int slot = 1; slot <= 5; slot++) {
                todosHorarios.add(new Horario(dia, slot));
            }
        }
        Collections.shuffle(todosHorarios); // Distribuição aleatória inicial para espalhar as aulas

        // 3. Pré-carregar professores por disciplina
        Map<Long, List<Professor>> professoresPorDisciplina = new HashMap<>();
        for (Disciplina d : cargaHoraria.keySet()) {
            List<Professor> professores = professorRepository.findByDisciplina(d);
            if (professores.isEmpty()) {
                throw new NotFoundException("A disciplina '" + d.getNome() + "' não possui nenhum professor disponível cadastrado.");
            }
            professoresPorDisciplina.put(d.getId(), professores);
        }

        // 4. Iniciar Backtracking em memória
        List<Aula> gradeGerada = new ArrayList<>();
        boolean sucesso = resolverBacktracking(turma, disciplinasPendentes, 0, todosHorarios, professoresPorDisciplina, gradeGerada);

        if (!sucesso) {
            throw new BusinessException("Não foi possível gerar uma grade sem conflitos para a turma '" + turma.getNome() +
                    "'. Isso pode ocorrer por falta de professores disponíveis nos horários necessários.");
        }

        // 5. Salvar no banco (limpando a antiga primeiro)
        try {
            aulaRepository.deleteByTurma(turma);
            aulaRepository.saveAll(gradeGerada);
        } catch (Exception e) {
            throw new BusinessException("Erro crítico ao salvar a nova grade no banco de dados.", e);
        }
    }

    private boolean resolverBacktracking(Turma turma, List<Disciplina> pendentes, int index,
                                         List<Horario> todosHorarios, Map<Long, List<Professor>> professoresPorDisciplina,
                                         List<Aula> gradeAtual) {

        if (index == pendentes.size()) return true;

        Disciplina disciplinaAtual = pendentes.get(index);
        List<Professor> professoresAptos = professoresPorDisciplina.get(disciplinaAtual.getId());

        // Tenta cada professor da disciplina
        for (Professor prof : professoresAptos) {
            // Tenta cada horário (já embaralhado para distribuição)
            for (Horario h : todosHorarios) {
                if (possoAlocar(prof, turma, h.dia(), h.slot(), gradeAtual)) {

                    Aula novaAula = new Aula();
                    novaAula.setTurma(turma);
                    novaAula.setDisciplina(disciplinaAtual);
                    novaAula.setProfessor(prof);
                    novaAula.setDiaDaSemana(h.dia());
                    novaAula.setSlotHorario(h.slot());

                    gradeAtual.add(novaAula);

                    if (resolverBacktracking(turma, pendentes, index + 1, todosHorarios, professoresPorDisciplina, gradeAtual)) {
                        return true;
                    }

                    gradeAtual.remove(gradeAtual.size() - 1);
                }
            }
        }

        return false;
    }

    private boolean possoAlocar(Professor professor, Turma turma, DayOfWeek dia, int slot, List<Aula> gradeAtual) {
        // Conflito da turma em memória (mesmo horário)
        for (Aula a : gradeAtual) {
            if (a.getDiaDaSemana() == dia && a.getSlotHorario() == slot) return false;
        }

        // Conflito do professor em memória (mesmo horário)
        for (Aula a : gradeAtual) {
            if (a.getProfessor().getId().equals(professor.getId()) && a.getDiaDaSemana() == dia && a.getSlotHorario() == slot) return false;
        }

        // Conflito do professor no banco (em OUTRAS turmas)
        if (aulaRepository.professorOcupadoNoBanco(professor, dia, slot, turma)) {
            return false;
        }

        // Regra de disponibilidade do professor (cadastrada no sistema)
        if (!professorRepository.isProfessorDisponivel(professor, dia, slot)) {
            return false;
        }

        return true;
    }

    @Override
    public Professor buscarProfessorDaDisciplina(Disciplina disciplina) {
        List<Professor> aptos = professorRepository.findByDisciplina(disciplina);
        return aptos.isEmpty() ? null : aptos.get(0);
    }

    @Override
    public void save(Aula aula) {
        validarIndividual(aula);
        aulaRepository.save(aula);
    }

    @Override
    public void update(Aula aula) {
        validarIndividual(aula);
        aulaRepository.update(aula);
    }

    private void validarIndividual(Aula aula) {
        if (aulaRepository.professorOcupadoNoBanco(aula.getProfessor(), aula.getDiaDaSemana(), aula.getSlotHorario(), aula.getTurma())) {
            throw new BusinessException("Conflito: O professor " + aula.getProfessor().getNome() + " já está ocupado neste horário em outra turma.");
        }
        if (!professorRepository.isProfessorDisponivel(aula.getProfessor(), aula.getDiaDaSemana(), aula.getSlotHorario())) {
            throw new BusinessException("Conflito: O professor " + aula.getProfessor().getNome() + " não está disponível neste horário.");
        }
    }

    @Override
    public void delete(Aula aula) {
        aulaRepository.delete(aula);
    }

    @Override
    public List<Aula> findAll() {
        return aulaRepository.findAll();
    }
}