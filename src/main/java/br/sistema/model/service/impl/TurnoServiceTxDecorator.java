package br.sistema.model.service.impl;

import br.sistema.model.entity.Turno;
import br.sistema.model.service.interfaces.TurnoService;
import jakarta.persistence.EntityManager;

import java.util.List;

public class TurnoServiceTxDecorator implements TurnoService {

    private final TurnoService target;
    private final EntityManager em;

    public TurnoServiceTxDecorator(TurnoService target, EntityManager em) {
        this.target = target;
        this.em = em;
    }

    @Override
    public void save(Turno turno) {
        try {
            em.getTransaction().begin();
            target.save(turno);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void update(Turno turno) {
        try {
            em.getTransaction().begin();
            target.update(turno);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void delete(Turno turno) {
        try {
            em.getTransaction().begin();
            target.delete(turno);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public Turno findByName(String nome) {
        return target.findByName(nome);
    }

    @Override
    public Turno findById(Long id) {
        return target.findById(id);
    }

    @Override
    public List<Turno> findAll() {
        return target.findAll();
    }
}
