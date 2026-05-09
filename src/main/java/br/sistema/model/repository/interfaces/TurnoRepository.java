package br.sistema.model.repository.interfaces;

import br.sistema.model.entity.Turno;

import java.util.List;

public interface TurnoRepository {

    void save(Turno turno);

    Turno findById(Long id);

    List<Turno> findAll();

    void update(Turno turno);

    void delete(Turno turno);

    Turno findByName(String nome);
}