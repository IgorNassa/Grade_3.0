package br.sistema.model.service.interfaces;
import br.sistema.model.entity.Turno;
import java.util.List;

public interface TurnoService {

    void save(Turno turno);

    Turno findByName(String nome);

    void update(Turno turno);

    void delete(Turno turno);

    List<Turno> findAll();

}
