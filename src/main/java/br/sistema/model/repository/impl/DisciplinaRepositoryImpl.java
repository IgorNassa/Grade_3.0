package br.sistema.model.repository.impl;

import br.sistema.model.entity.Disciplina;
import br.sistema.model.repository.interfaces.DisciplinaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class DisciplinaRepositoryImpl implements DisciplinaRepository {

    private final EntityManager em;

    public DisciplinaRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Disciplina disciplina) {
        em.getTransaction().begin();

        try {
            em.persist(disciplina);
            em.flush();
            em.refresh(disciplina);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;
        }
    }

    @Override
    public List<Disciplina> findAll() {
        return em.createQuery(
                "SELECT d FROM Disciplina d ORDER BY d.nome",
                Disciplina.class
        ).getResultList();
    }

    @Override
    public void update(Disciplina disciplina) {
        em.getTransaction().begin();

        try {
            em.merge(disciplina);
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
        em.getTransaction().begin();

        try {
            Disciplina disciplinaGerenciada = em.contains(disciplina)
                    ? disciplina
                    : em.merge(disciplina);

            em.remove(disciplinaGerenciada);
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
        return em.find(Disciplina.class, id);
    }

    @Override
    public Disciplina findByName(String nome) {
        try {
            return em.createQuery(
                            "SELECT d FROM Disciplina d WHERE LOWER(d.nome) = LOWER(:nome)",
                            Disciplina.class
                    )
                    .setParameter("nome", nome.trim())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}