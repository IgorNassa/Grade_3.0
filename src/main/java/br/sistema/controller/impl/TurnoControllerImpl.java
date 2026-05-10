package br.sistema.controller.impl;

import br.sistema.controller.dtos.TurnoDTO;
import br.sistema.controller.interfaces.TurnoController;
import br.sistema.model.service.interfaces.TurnoService;
import java.util.List;

public class TurnoControllerImpl implements TurnoController {

    private final TurnoService turnoService;

    public TurnoControllerImpl(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    @Override
    public void save(TurnoDTO turnoDTO) {
        turnoService.save(turnoDTO);
    }

    @Override
    public void update(TurnoDTO turnoDTO) {
        turnoService.update(turnoDTO);
    }

    @Override
    public void delete(TurnoDTO turnoDTO) {
        turnoService.delete(turnoDTO);
    }

    @Override
    public List<TurnoDTO> findAll() {
        return turnoService.findAll();
    }

    @Override
    public TurnoDTO findByName(String nome) {
        return turnoService.findByName(nome);
    }
}