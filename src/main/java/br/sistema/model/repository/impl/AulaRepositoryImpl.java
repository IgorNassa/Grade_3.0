package br.sistema.model.repository.impl;

import br.sistema.model.entity.Aula;
import br.sistema.model.entity.Professor;
import br.sistema.model.repository.interfaces.AulaRepository;
import jakarta.persistence.EntityManager;

import java.time.DayOfWeek;

public class AulaRepositoryImpl implements AulaRepository {

    private EntityManager em;

    public AulaRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    public void save(Aula aula) {
        // Não abrimos transação aqui pq o Service vai abrir uma transação
        // em lote para salvar a grade inteira de uma vez.
        em.persist(aula);
    }

    // Consulta se o professor já tem aula no banco neste dia e slot
    public boolean professorOcupadoNoBanco(Professor professor, DayOfWeek dia, Integer slot) {
        Long count = em.createQuery(
                        "SELECT COUNT(a) FROM Aula a WHERE a.professor = :prof AND a.diaDaSemana = :dia AND a.slotHorario = :slot", Long.class)
                .setParameter("prof", professor)
                .setParameter("dia", dia)
                .setParameter("slot", slot)
                .getSingleResult();
        return count > 0;
    }
}