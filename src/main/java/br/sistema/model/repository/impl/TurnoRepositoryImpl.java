package br.sistema.model.repository.impl;

import br.sistema.model.entity.Turno;
import br.sistema.model.repository.interfaces.TurnoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class TurnoRepositoryImpl implements TurnoRepository {

    private final EntityManager em;

    public TurnoRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Turno turno) {
        em.persist(turno);
        em.flush();
        em.refresh(turno);
    }

    @Override
    public Turno findById(Long id) {
        if (id == null) {
            return null;
        }

        return em.find(Turno.class, id);
    }

    @Override
    public List<Turno> findAll() {
        return em.createQuery(
                "SELECT t FROM Turno t ORDER BY t.id",
                Turno.class
        ).getResultList();
    }

    @Override
    public void update(Turno turno) {
        em.merge(turno);
    }

    @Override
    public void delete(Turno turno) {
        if (turno == null || turno.getId() == null) {
            throw new IllegalArgumentException("Turno inválido para exclusão.");
        }

        Turno turnoManaged = em.find(Turno.class, turno.getId());

        if (turnoManaged != null) {
            em.remove(turnoManaged);
        }
    }

    @Override
    public Turno findByName(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }

        try {
            return em.createQuery(
                            "SELECT t FROM Turno t WHERE CAST(t.nomeTurno AS string) = :nome",
                            Turno.class
                    )
                    .setParameter("nome", nome.trim().toUpperCase())
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }
}