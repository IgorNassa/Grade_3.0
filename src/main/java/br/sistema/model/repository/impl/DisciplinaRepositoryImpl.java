package br.sistema.model.repository.impl;

import br.sistema.model.entity.Disciplina;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class DisciplinaRepositoryImpl {

    private EntityManager em;

    public DisciplinaRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    //*C*RUD - Função de inserir no banco
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

    //C*R*UD - Função de listar todos do banco
    public List<Disciplina> findAll() {
        return em.createQuery("select d from Disciplina d", Disciplina.class).getResultList();
    }

    //CR*U*D - Função de atualizar dados do banco
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

    //CRU*D* - Função de deletar dados do banco
    public void delete(Disciplina disciplina) {
        em.getTransaction().begin();
        try {
            Disciplina disciplinaGerenciada = em.contains(disciplina) ? disciplina : em.merge(disciplina);
            em.remove(disciplinaGerenciada);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    //C*R*UD - Função de listar por nome do banco
    public Disciplina findByName(String nome) {
        try {
            return em.createQuery("SELECT d FROM Disciplina d WHERE LOWER(d.nome) = LOWER(:nome)", Disciplina.class)
                    .setParameter("nome", nome)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}