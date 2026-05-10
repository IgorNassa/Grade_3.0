package br.sistema.controller.interfaces;

import br.sistema.controller.dtos.TurmaDTO;
import java.util.List;

public interface TurmaController {
    void save(TurmaDTO turmaDTO);

    void update(TurmaDTO turmaDTO);

    void delete(TurmaDTO turmaDTO);

    List<TurmaDTO> findAll();

    TurmaDTO findByName(String nome);
}