package br.sistema.model.service.impl;

import br.sistema.model.entity.Professor;
import br.sistema.model.service.interfaces.ProfessorService;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ProfessorServiceTxDecorator implements ProfessorService {

    private final ProfessorService target;
    private final EntityManager em;

    public ProfessorServiceTxDecorator(ProfessorService target, EntityManager em) {
        this.target = target;
        this.em = em;
    }

    @Override
    public void save(Professor professor) {
        try {
            em.getTransaction().begin();
            target.save(professor);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void update(Professor professor) {
        try {
            em.getTransaction().begin();
            target.update(professor);
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
        try {
            em.getTransaction().begin();
            target.delete(professor);
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
        return target.findById(id);
    }

    @Override
    public Professor findByName(String nome) {
        return target.findByName(nome);
    }

    @Override
    public List<Professor> findAll() {
        return target.findAll();
    }
}
