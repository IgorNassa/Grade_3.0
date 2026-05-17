package br.sistema.model.repository.impl;

import br.sistema.model.entity.Turma;
import br.sistema.model.repository.interfaces.TurmaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class TurmaRepositoryImpl implements TurmaRepository {

    private final EntityManager em;

    public TurmaRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Turma turma) {
        em.persist(turma);
        em.flush();
        em.refresh(turma);
    }

    @Override
    public Turma findById(Long id) {
        if (id == null) {
            return null;
        }

        return em.find(Turma.class, id);
    }

    @Override
    public List<Turma> findAll() {
        return em.createQuery(
                "SELECT t FROM Turma t ORDER BY t.nome",
                Turma.class
        ).getResultList();
    }

    @Override
    public void update(Turma turma) {
        em.merge(turma);
    }

    @Override
    public void delete(Turma turma) {
        if (turma == null || turma.getId() == null) {
            throw new IllegalArgumentException("Turma inválida para exclusão.");
        }

        Turma turmaManaged = em.find(Turma.class, turma.getId());

        if (turmaManaged != null) {
            em.remove(turmaManaged);
        }
    }

    @Override
    public Turma findByName(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }

        try {
            return em.createQuery(
                            "SELECT t FROM Turma t WHERE LOWER(t.nome) = LOWER(:nome)",
                            Turma.class
                    )
                    .setParameter("nome", nome.trim())
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }
}