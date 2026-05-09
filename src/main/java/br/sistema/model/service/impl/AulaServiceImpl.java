package br.sistema.model.service.impl;

import br.sistema.model.entity.Aula;
import br.sistema.model.entity.Disciplina;
import br.sistema.model.entity.Professor;
import br.sistema.model.entity.Turma;
import br.sistema.model.repository.interfaces.AulaRepository;
import br.sistema.model.service.interfaces.AulaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.time.DayOfWeek;
import java.util.*;

public class AulaServiceImpl implements AulaService {

    private final AulaRepository aulaRepository;
    private final EntityManager em;

    public AulaServiceImpl(AulaRepository aulaRepository, EntityManager em) {
        this.aulaRepository = aulaRepository;
        this.em = em;
    }

    private final List<DayOfWeek> DIAS_LETIVOS = Arrays.asList(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
    );

    // Sem parâmetro dinâmico de horas. Mais simples.
    @Override
    public void gerarGrade(Turma turma, Map<Disciplina, Integer> cargaHoraria) {
        try {
            if (turma == null || turma.getId() == null) {
                throw new IllegalArgumentException("Turma inválida para geração da grade.");
            }

            if (cargaHoraria == null || cargaHoraria.isEmpty()) {
                throw new IllegalArgumentException("Carga horária não informada.");
            }

            em.getTransaction().begin();

            aulaRepository.deleteByTurma(turma);

            int maxSlotsPorDia = 5;

            List<Aula> aulasParaSalvar = new ArrayList<>();

            for (Map.Entry<Disciplina, Integer> entrada : cargaHoraria.entrySet()) {
                Disciplina disciplina = entrada.getKey();
                Integer quantidadeAulas = entrada.getValue();

                if (disciplina == null || disciplina.getId() == null) {
                    throw new RuntimeException("Disciplina inválida na carga horária.");
                }

                if (quantidadeAulas == null || quantidadeAulas <= 0) {
                    throw new RuntimeException("Carga horária inválida para a disciplina: " + disciplina.getNome());
                }

                Professor professorApto = buscarProfessorDaDisciplina(disciplina);

                if (professorApto == null) {
                    throw new RuntimeException("Nenhum professor vinculado à disciplina: " + disciplina.getNome());
                }

                int aulasAlocadas = 0;

                for (DayOfWeek dia : DIAS_LETIVOS) {
                    for (int slot = 1; slot <= maxSlotsPorDia; slot++) {
                        if (aulasAlocadas == quantidadeAulas) {
                            break;
                        }

                        boolean professorOcupado = aulaRepository.professorOcupadoNoBanco(professorApto, dia, slot);

                        if (!professorOcupado) {
                            for (Aula a : aulasParaSalvar) {
                                if (a.getProfessor().equals(professorApto) && a.getDiaDaSemana() == dia && a.getSlotHorario() == slot) {
                                    professorOcupado = true;
                                    break;
                                }
                            }
                        }

                        boolean turmaOcupada = aulaRepository.turmaOcupadaNoBanco(turma, dia, slot);
                        if (!turmaOcupada){
                            for (Aula a : aulasParaSalvar){
                                if (a.getTurma().equals(turma) && a.getDiaDaSemana() == dia && a.getSlotHorario().equals(slot)){
                                    turmaOcupada = true;
                                    break;
                                }
                            }
                        }

                        if (professorOcupado || turmaOcupada) {
                            continue;
                        }

                        Aula aula = new Aula();
                        aula.setTurma(turma);
                        aula.setDisciplina(disciplina);
                        aula.setProfessor(professorApto);
                        aula.setDiaDaSemana(dia);
                        aula.setSlotHorario(slot);

                        aulasParaSalvar.add(aula);
                        aulasAlocadas++;
                    }

                    if (aulasAlocadas == quantidadeAulas) {
                        break;
                    }
                }

                if (aulasAlocadas < quantidadeAulas) {
                    throw new RuntimeException("Não foi possível alocar todas as aulas da disciplina: " + disciplina.getNome());
                }

            }

            if (!aulasParaSalvar.isEmpty()) {
                aulaRepository.saveAll(aulasParaSalvar);
            }

            em.getTransaction().commit();
            System.out.println("Grade gerada com sucesso!");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            System.err.println("[ERRO CRÍTICO] Falha ao persistir grade: " + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    // Relembrando a função auxiliar corrigida que deve estar na mesma classe:
    @Override
    public Professor buscarProfessorDaDisciplina(Disciplina disciplina) {
        if (disciplina == null || disciplina.getId() == null) {
            return null;
        }

        try {
            return em.createQuery(
                            "SELECT p FROM Professor p JOIN p.disciplinas d WHERE d.id = :disciplinaId",
                            Professor.class
                    )
                    .setParameter("disciplinaId", disciplina.getId())
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}