package br.sistema.model.service.impl;

import br.sistema.model.entity.Disciplina;
import br.sistema.model.service.interfaces.DisciplinaService;
import jakarta.persistence.EntityManager;

import java.util.List;

public class DisciplinaServiceTxDecorator implements DisciplinaService {

    private final DisciplinaService target;
    private final EntityManager em;

    public DisciplinaServiceTxDecorator(DisciplinaService target, EntityManager em) {
        this.target = target;
        this.em = em;
    }

    @Override
    public void save(Disciplina disciplina) {
        try {
            em.getTransaction().begin();
            target.save(disciplina);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void update(Disciplina disciplina) {
        try {
            em.getTransaction().begin();
            target.update(disciplina);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void delete(Disciplina disciplina) {
        try {
            em.getTransaction().begin();
            target.delete(disciplina);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public Disciplina findById(Long id) {
        return target.findById(id);
    }

    @Override
    public Disciplina findByName(String nome) {
        return target.findByName(nome);
    }

    @Override
    public List<Disciplina> findAll() {
        return target.findAll();
    }
}
