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
    public void gerarGrade(Turma turma, Map<Disciplina, Integer> cargaHoraria) {
        try {
            em.getTransaction().begin();

            // 1. Limpa a grade anterior
            em.createQuery("DELETE FROM Aula a WHERE a.turma = :turma")
                    .setParameter("turma", turma)
                    .executeUpdate();

            // 2. Nomes dos dias compatíveis com java.time.DayOfWeek (Inglês/Maiúsculo)
            // Se sua entidade usa o Enum DayOfWeek, TEM que ser esses valores:
            List<String> diasSemana = Arrays.asList("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY");

            int maxSlotsPorDia = 5;
            int diaAtualIndex = 0;
            int slotAtual = 1;

            for (Map.Entry<Disciplina, Integer> entrada : cargaHoraria.entrySet()) {
                Disciplina disciplina = entrada.getKey();
                int quantidadeAulas = entrada.getValue();

                if (disciplina == null) continue;

                Professor professorApto = buscarProfessorDaDisciplina(disciplina);

                for (int i = 0; i < quantidadeAulas; i++) {
                    if (diaAtualIndex >= diasSemana.size()) {
                        break;
                    }

                    Aula aula = new Aula();
                    aula.setTurma(turma);
                    aula.setDisciplina(disciplina);
                    aula.setProfessor(professorApto);

                    // 3. Converte a String para o Enum DayOfWeek
                    aula.setDiaDaSemana(java.time.DayOfWeek.valueOf(diasSemana.get(diaAtualIndex)));

                    aula.setSlotHorario(slotAtual);

                    em.persist(aula);

                    slotAtual++;
                    if (slotAtual > maxSlotsPorDia) {
                        slotAtual = 1;
                        diaAtualIndex++;
                    }
                }
            }

            em.getTransaction().commit();
            System.out.println("Grade gerada com sucesso!");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.err.println("[ERRO CRÍTICO] Falha ao persistir grade: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
    // Relembrando a função auxiliar corrigida que deve estar na mesma classe:
    public Professor buscarProfessorDaDisciplina(Disciplina disciplina) {
        try {
            return em.createQuery("SELECT p FROM Professor p WHERE :disc MEMBER OF p.disciplinas", Professor.class)
                    .setParameter("disc", disciplina)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Retorna null para gerar aula como "Vaga"
        }
    }
}