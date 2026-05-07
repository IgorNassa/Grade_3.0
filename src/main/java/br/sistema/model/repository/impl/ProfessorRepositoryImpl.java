package br.sistema.model.repository.impl;

import br.sistema.model.entity.Disciplina;
import br.sistema.model.entity.Professor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

public class ProfessorRepositoryImpl {

    private EntityManager em;

    public ProfessorRepositoryImpl(EntityManager em){
        this.em = em;
    }

    public void save(Professor professor) {
        em.getTransaction().begin();
        try {
            if (professor.getDisciplinas() != null) {
                List<Disciplina> disciplinasManaged = new ArrayList<>();
                for (Disciplina d : professor.getDisciplinas()) {
                    disciplinasManaged.add(em.merge(d));
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

    public List<Professor> findAll(){
        return em.createQuery("from Professor", Professor.class).getResultList();
    }

    public void update(Professor professor) {
        em.getTransaction().begin();
        try {
            em.merge(professor);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    public void delete(Professor professor) {
        em.getTransaction().begin();
        try {
            em.remove(em.contains(professor) ? professor : em.merge(professor));
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    public Professor findByName(String nome) {
        try {
            return em.createQuery("SELECT p FROM Professor p WHERE LOWER(p.nome) = LOWER(:nome)", Professor.class)
                    .setParameter("nome", nome)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Professor> findByDisciplina(Disciplina disc) {
        try {
            return em.createQuery(
                            "SELECT p FROM Professor p WHERE :disc MEMBER OF p.disciplinas",
                            Professor.class)
                    .setParameter("disc", disc)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("[ERRO] Falha ao buscar professores por disciplina: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}