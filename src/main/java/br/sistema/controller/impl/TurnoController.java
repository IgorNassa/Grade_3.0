package br.sistema.controller.impl;
import br.sistema.util.TurnoMapper;
import br.sistema.controller.dtos.TurnoDTO;
import br.sistema.model.entity.Turno;
import jakarta.persistence.EntityManager;
import java.util.List;
import br.sistema.config.database.JPAConfig;
import br.sistema.controller.dtos.TurnoDTO;

public class TurnoController {

    public void salvar(TurnoDTO dto) {
        EntityManager em = JPAConfig.getEntityManager();

        Turno entity = TurnoMapper.INSTANCE.toEntity(dto);

        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<TurnoDTO> listarTodos() {
        EntityManager em = JPAConfig.getEntityManager();
        List<Turno> lista = em.createQuery("from Turno", Turno.class).getResultList();
        em.close();

        return lista.stream()
                .map(TurnoMapper.INSTANCE::toDTO)
                .toList();
    }
}



