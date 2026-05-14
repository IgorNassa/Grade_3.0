package br.sistema.controller.interfaces;

import br.sistema.controller.dtos.TurnoDTO;
import java.util.List;

public interface TurnoController {

    void save(TurnoDTO turnoDTO);

    void update(TurnoDTO turnoDTO);

    void delete(TurnoDTO turnoDTO);

    List<TurnoDTO> findAll();

    TurnoDTO findByName(String nome);
}