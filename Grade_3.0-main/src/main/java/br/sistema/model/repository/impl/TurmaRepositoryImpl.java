package br.sistema.model.repository.impl;

import br.sistema.model.entity.Turma;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class TurmaRepositoryImpl {

    private EntityManager em;

    public TurmaRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    public void save(Turma turma) {
        em.getTransaction().begin();
        try {
            em.persist(turma);
            em.flush();
            em.refresh(turma);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    public List<Turma> findAll(){
        return em.createQuery("SELECT t FROM Turma t", Turma.class).getResultList();
    }

    public void update(Turma turma) {
        em.getTransaction().begin();
        try {
            em.merge(turma);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    public void delete(Turma turma) {
        em.getTransaction().begin();
        try {
            em.remove(em.contains(turma) ? turma : em.merge(turma));
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    public Turma findById(long id) {
        return em.find(Turma.class, id);
    }

    public Turma findByName(String nome) {
        try {
            return em.createQuery("SELECT t FROM Turma t WHERE LOWER(t.nome) = LOWER(:nome)", Turma.class)
                    .setParameter("nome", nome)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}