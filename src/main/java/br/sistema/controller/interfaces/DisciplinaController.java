package br.sistema.controller.interfaces;

import br.sistema.controller.dtos.DisciplinaDTO;
import java.util.List;

public interface DisciplinaController {

    void save(DisciplinaDTO disciplinaDTO);

    void update(DisciplinaDTO disciplinaDTO);

    void delete(DisciplinaDTO disciplinaDTO);

    List<DisciplinaDTO> findAll();

    DisciplinaDTO findByName(String nome);
}