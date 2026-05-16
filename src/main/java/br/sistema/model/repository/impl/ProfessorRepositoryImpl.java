package br.sistema.model.repository.impl;

import br.sistema.model.entity.Disciplina;
import br.sistema.model.entity.Professor;
import br.sistema.model.repository.interfaces.ProfessorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class ProfessorRepositoryImpl implements ProfessorRepository {

    private final EntityManager em;

    public ProfessorRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Professor professor) {
        if (professor.getDisciplinas() != null) {
            List<Disciplina> disciplinasManaged = new ArrayList<>();

            for (Disciplina disciplina : professor.getDisciplinas()) {
                disciplinasManaged.add(em.merge(disciplina));
            }

            professor.setDisciplinas(disciplinasManaged);
        }

        em.persist(professor);
        em.flush();
        em.refresh(professor);
    }

    @Override
    public Professor findById(Long id) {
        if (id == null) {
            return null;
        }

        return em.find(Professor.class, id);
    }

    @Override
    public List<Professor> findAll() {
        return em.createQuery(
                "SELECT p FROM Professor p ORDER BY p.nome",
                Professor.class
        ).getResultList();
    }

    @Override
    public void update(Professor professor) {
        if (professor.getDisciplinas() != null) {
            List<Disciplina> disciplinasManaged = new ArrayList<>();

            for (Disciplina disciplina : professor.getDisciplinas()) {
                disciplinasManaged.add(em.merge(disciplina));
            }

            professor.setDisciplinas(disciplinasManaged);
        }

        em.merge(professor);
    }

    @Override
    public void delete(Professor professor) {
        if (professor == null || professor.getId() == null) {
            throw new IllegalArgumentException("Professor inválido para exclusão.");
        }

        Professor professorManaged = em.find(Professor.class, professor.getId());

        if (professorManaged != null) {
            em.remove(professorManaged);
        }
    }

    @Override
    public Professor findByName(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }

        try {
            return em.createQuery(
                            "SELECT p FROM Professor p WHERE LOWER(p.nome) = LOWER(:nome)",
                            Professor.class
                    )
                    .setParameter("nome", nome.trim())
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Professor> findByDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            return new ArrayList<>();
        }

        return em.createQuery(
                        "SELECT p FROM Professor p WHERE :disciplina MEMBER OF p.disciplinas",
                        Professor.class
                )
                .setParameter("disciplina", disciplina)
                .getResultList();
    }

    @Override
    public boolean isProfessorDisponivel(Professor professor, DayOfWeek dia, Integer slot) {
        if (professor == null || dia == null || slot == null) {
            return false;
        }

        Long count = em.createQuery(
                        "SELECT COUNT(pd) FROM ProfessorDisponibilidade pd " +
                                "WHERE pd.professor = :professor " +
                                "AND pd.diaDaSemana = :dia " +
                                "AND pd.slotHorario = :slot " +
                                "AND pd.disponivel = false",
                        Long.class
                )
                .setParameter("professor", professor)
                .setParameter("dia", dia)
                .setParameter("slot", slot)
                .getSingleResult();

        return count == 0;
    }
}