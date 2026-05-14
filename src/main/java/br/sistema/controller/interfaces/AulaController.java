package br.sistema.controller.interfaces;

import br.sistema.controller.dtos.AulaDTO;

import java.util.List;

public interface AulaController {

    void save(AulaDTO aulaDTO);

    void update(AulaDTO aulaDTO);

    void delete(AulaDTO aulaDTO);

    List<AulaDTO> findAll();
}