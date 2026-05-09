package br.sistema.model.repository.impl;

import br.sistema.model.entity.Disciplina;
import br.sistema.model.entity.Professor;
import br.sistema.model.repository.interfaces.ProfessorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

public class ProfessorRepositoryImpl implements ProfessorRepository {

    private final EntityManager em;

    public ProfessorRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Professor professor) {
        em.getTransaction().begin();

        try {
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
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;
        }
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
        em.getTransaction().begin();

        try {
            if (professor.getDisciplinas() != null) {
                List<Disciplina> disciplinasManaged = new ArrayList<>();

                for (Disciplina disciplina : professor.getDisciplinas()) {
                    disciplinasManaged.add(em.merge(disciplina));
                }

                professor.setDisciplinas(disciplinasManaged);
            }

            em.merge(professor);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;
        }
    }

    @Override
    public void delete(Professor professor) {
        em.getTransaction().begin();

        try {
            if (professor == null || professor.getId() == null) {
                throw new IllegalArgumentException("Professor inválido para exclusão.");
            }

            Professor professorManaged = em.find(Professor.class, professor.getId());

            if (professorManaged != null) {
                em.remove(professorManaged);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;
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

        try {
            return em.createQuery(
                            "SELECT p FROM Professor p WHERE :disciplina MEMBER OF p.disciplinas",
                            Professor.class
                    )
                    .setParameter("disciplina", disciplina)
                    .getResultList();

        } catch (Exception e) {
            System.err.println("[ERRO] Falha ao buscar professores por disciplina: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}