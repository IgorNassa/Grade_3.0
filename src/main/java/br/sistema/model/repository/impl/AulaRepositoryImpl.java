package br.sistema.model.repository.impl;

import br.sistema.model.entity.Aula;
import br.sistema.model.entity.Professor;
import br.sistema.model.entity.Turma;
import br.sistema.model.repository.interfaces.AulaRepository;
import jakarta.persistence.EntityManager;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class AulaRepositoryImpl implements AulaRepository {

    private final EntityManager em;

    public AulaRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Aula aula) {
        em.persist(aula);
    }

    @Override
    public void saveAll(List<Aula> aulas) {
        if (aulas == null || aulas.isEmpty()) {
            return;
        }

        int batchSize = 50;

        for (int i = 0; i < aulas.size(); i++) {
            em.persist(aulas.get(i));

            if (i > 0 && i % batchSize == 0) {
                em.flush();
            }
        }

        em.flush();
    }

    @Override
    public Aula findById(Long id) {
        if (id == null) {
            return null;
        }

        return em.find(Aula.class, id);
    }

    @Override
    public List<Aula> findAll() {
        return em.createQuery(
                "SELECT a FROM Aula a ORDER BY a.turma.nome, a.diaDaSemana, a.slotHorario",
                Aula.class
        ).getResultList();
    }

    @Override
    public void update(Aula aula) {
        em.merge(aula);
    }

    @Override
    public void delete(Aula aula) {
        if (aula == null) {
            throw new IllegalArgumentException("Aula inválida para exclusão.");
        }

        Aula aulaGerenciada = em.contains(aula) ? aula : em.merge(aula);
        em.remove(aulaGerenciada);
    }

    @Override
    public List<Aula> findByTurma(Turma turma) {
        if (turma == null) {
            return new ArrayList<>();
        }

        return em.createQuery(
                        "SELECT a FROM Aula a WHERE a.turma = :turma ORDER BY a.diaDaSemana, a.slotHorario",
                        Aula.class
                )
                .setParameter("turma", turma)
                .getResultList();
    }

    @Override
    public List<Aula> findByProfessor(Professor professor) {
        if (professor == null) {
            return new ArrayList<>();
        }

        return em.createQuery(
                        "SELECT a FROM Aula a WHERE a.professor = :professor ORDER BY a.diaDaSemana, a.slotHorario",
                        Aula.class
                )
                .setParameter("professor", professor)
                .getResultList();
    }

    @Override
    public void deleteByTurma(Turma turma) {
        if (turma == null) {
            return;
        }

        em.createQuery("DELETE FROM Aula a WHERE a.turma = :turma")
                .setParameter("turma", turma)
                .executeUpdate();
    }

    @Override
    public boolean professorOcupadoNoBanco(Professor professor, DayOfWeek dia, Integer slot, Turma turmaAExcluir) {
        if (professor == null || dia == null || slot == null) {
            return false;
        }

        String query = "SELECT COUNT(a) FROM Aula a WHERE a.professor = :professor AND a.diaDaSemana = :dia AND a.slotHorario = :slot";
        if (turmaAExcluir != null) {
            query += " AND a.turma <> :turmaExcluir";
        }

        var q = em.createQuery(query, Long.class)
                .setParameter("professor", professor)
                .setParameter("dia", dia)
                .setParameter("slot", slot);

        if (turmaAExcluir != null) {
            q.setParameter("turmaExcluir", turmaAExcluir);
        }

        return q.getSingleResult() > 0;
    }

    @Override
    public boolean turmaOcupadaNoBanco(Turma turma, DayOfWeek dia, Integer slot) {
        if (turma == null || dia == null || slot == null) {
            return false;
        }

        Long count = em.createQuery(
                        "SELECT COUNT(a) FROM Aula a WHERE a.turma = :turma AND a.diaDaSemana = :dia AND a.slotHorario = :slot", Long.class)
                .setParameter("turma", turma)
                .setParameter("dia", dia)
                .setParameter("slot", slot)
                .getSingleResult();

        return count > 0;
    }

}