package br.sistema.model.service.impl;

import br.sistema.model.entity.Aula;
import br.sistema.model.entity.Disciplina;
import br.sistema.model.entity.Professor;
import br.sistema.model.entity.Turma;
import br.sistema.model.service.interfaces.AulaService;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Map;

public class AulaServiceTxDecorator implements AulaService {

    private final AulaService target;
    private final EntityManager em;

    public AulaServiceTxDecorator(AulaService target, EntityManager em) {
        this.target = target;
        this.em = em;
    }

    @Override
    public void gerarGrade(Turma turma, Map<Disciplina, Integer> cargaHoraria, String turno) {
        try {
            em.getTransaction().begin();
            target.gerarGrade(turma, cargaHoraria, turno);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void save(Aula aula) {
        try {
            em.getTransaction().begin();
            target.save(aula);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void deleteByTurma(Turma turma) {
        try {
            em.getTransaction().begin();
            target.deleteByTurma(turma);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void update(Aula aula) {
        try {
            em.getTransaction().begin();
            target.update(aula);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void delete(Aula aula) {
        try {
            em.getTransaction().begin();
            target.delete(aula);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    // Métodos de leitura (Find) não precisam de controle de transação explícito
    @Override
    public Professor buscarProfessorDaDisciplina(Disciplina disciplina) {
        return target.buscarProfessorDaDisciplina(disciplina);
    }

    @Override
    public List<Aula> findAll() {
        return target.findAll();
    }
}