package br.sistema.model.service.impl;

import br.sistema.model.entity.Turma;
import br.sistema.model.service.interfaces.TurmaService;
import jakarta.persistence.EntityManager;

import java.util.List;

public class TurmaServiceTxDecorator implements TurmaService {

    private final TurmaService target;
    private final EntityManager em;

    public TurmaServiceTxDecorator(TurmaService target, EntityManager em) {
        this.target = target;
        this.em = em;
    }

    @Override
    public void save(Turma turma) {
        try {
            em.getTransaction().begin();
            target.save(turma);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void update(Turma turma) {
        try {
            em.getTransaction().begin();
            target.update(turma);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void delete(Turma turma) {
        try {
            em.getTransaction().begin();
            target.delete(turma);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public Turma findById(Long id) {
        return target.findById(id);
    }

    @Override
    public Turma findByName(String nome) {
        return target.findByName(nome);
    }

    @Override
    public List<Turma> findAll() {
        return target.findAll();
    }
}
