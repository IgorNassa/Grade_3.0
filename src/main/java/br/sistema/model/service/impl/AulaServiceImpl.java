package br.sistema.model.service.impl;

import br.sistema.model.entity.Aula;
import br.sistema.model.entity.Disciplina;
import br.sistema.model.entity.Professor;
import br.sistema.model.entity.Turma;
import br.sistema.model.repository.impl.ProfessorRepositoryImpl;
import br.sistema.model.repository.interfaces.AulaRepository;
import br.sistema.model.service.interfaces.AulaService;
import br.sistema.model.exception.BusinessException;
import br.sistema.model.exception.NotFoundException;
import br.sistema.model.exception.ValidationException;

import java.time.DayOfWeek;
import java.util.*;

public class AulaServiceImpl implements AulaService {

    private final AulaRepository aulaRepository;
    private final ProfessorRepositoryImpl professorRepository;

    public AulaServiceImpl(AulaRepository aulaRepository, ProfessorRepositoryImpl professorRepository) {
        this.aulaRepository = aulaRepository;
        this.professorRepository = professorRepository;
    }

    private final List<DayOfWeek> DIAS_LETIVOS = Arrays.asList(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
    );

    // Record para abstrair a aula antes de ter dia e horário definidos
    private record AulaPendente(Disciplina disciplina, Professor professor) {}

    @Override
    public void gerarGrade(Turma turma, Map<Disciplina, Integer> cargaHoraria) {
        if (turma == null || turma.getId() == null) {
            throw new ValidationException("Turma inválida para geração da grade.");
        }
        if (cargaHoraria == null || cargaHoraria.isEmpty()) {
            throw new ValidationException("Carga horária não informada.");
        }

        // Delegação limpa para o repositório deletar a grade antiga
        aulaRepository.deleteByTurma(turma);

        // Achatamento (Flattening) do Map para uma List plana
        List<AulaPendente> aulasPendentes = new ArrayList<>();
        for (Map.Entry<Disciplina, Integer> entrada : cargaHoraria.entrySet()) {
            Disciplina disciplina = entrada.getKey();
            Integer quantidadeAulas = entrada.getValue();

            if (disciplina == null || disciplina.getId() == null) {
                throw new ValidationException("Disciplina inválida na carga horária.");
            }
            if (quantidadeAulas == null || quantidadeAulas <= 0) {
                throw new ValidationException("Carga horária inválida para a disciplina: " + disciplina.getNome());
            }

            Professor professorApto = buscarProfessorDaDisciplina(disciplina);
            if (professorApto == null) {
                throw new NotFoundException("Nenhum professor vinculado à disciplina: " + disciplina.getNome());
            }

            for (int i = 0; i < quantidadeAulas; i++) {
                aulasPendentes.add(new AulaPendente(disciplina, professorApto));
            }
        }

        // Inicia o Backtracking
        List<Aula> gradeGerada = new ArrayList<>();
        boolean gradePossivel = resolverGradeBacktracking(turma, aulasPendentes, 0, gradeGerada);

        if (!gradePossivel) {
            throw new BusinessException("Conflito matemático: Não foi possível gerar uma grade sem conflitos com a carga horária e disponibilidade atuais.");
        }

        // Delegação limpa para salvar em Batch no repositório
        aulaRepository.saveAll(gradeGerada);
        System.out.println("Grade gerada com sucesso via Backtracking!");
    }

    private boolean resolverGradeBacktracking(Turma turma, List<AulaPendente> pendentes, int index, List<Aula> gradeAtual) {
        // Condição de parada
        if (index == pendentes.size()) return true;

        AulaPendente aulaAtual = pendentes.get(index);
        int maxSlotsPorDia = 5;

        for (DayOfWeek dia : DIAS_LETIVOS) {
            for (int slot = 1; slot <= maxSlotsPorDia; slot++) {
                if (possoAlocar(aulaAtual.professor(), turma, dia, slot, gradeAtual)) {
                    // Escolhe (Take)
                    Aula novaAula = new Aula();
                    novaAula.setTurma(turma);
                    novaAula.setDisciplina(aulaAtual.disciplina());
                    novaAula.setProfessor(aulaAtual.professor());
                    novaAula.setDiaDaSemana(dia);
                    novaAula.setSlotHorario(slot);

                    gradeAtual.add(novaAula);

                    // Explora (Explore)
                    if (resolverGradeBacktracking(turma, pendentes, index + 1, gradeAtual)) {
                        return true;
                    }

                    // Desfaz (Backtrack)
                    gradeAtual.remove(gradeAtual.size() - 1);
                }
            }
        }
        return false;
    }

    private boolean possoAlocar(Professor professor, Turma turma, DayOfWeek dia, int slot, List<Aula> gradeAtual) {
        // Verifica conflitos na memória (grade sendo montada)
        for (Aula aulaAlocada : gradeAtual) {
            if (aulaAlocada.getDiaDaSemana() == dia && aulaAlocada.getSlotHorario() == slot) {
                if (aulaAlocada.getTurma().getId().equals(turma.getId())) return false;
                if (aulaAlocada.getProfessor().getId().equals(professor.getId())) return false;
            }
        }

        // Verifica conflitos no banco de dados
        return !aulaRepository.professorOcupadoNoBanco(professor, dia, slot) &&
                !aulaRepository.turmaOcupadaNoBanco(turma, dia, slot);
    }

    @Override
    public Professor buscarProfessorDaDisciplina(Disciplina disciplina) {
        List<Professor> aptos = professorRepository.findByDisciplina(disciplina);
        return aptos.isEmpty() ? null : aptos.get(0);
    }

    // Delegações simples para os métodos CRUD restantes da interface
    @Override
    public void save(Aula aula) {
        aulaRepository.save(aula);
    }

    @Override
    public void update(Aula aula) {
        aulaRepository.update(aula);
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