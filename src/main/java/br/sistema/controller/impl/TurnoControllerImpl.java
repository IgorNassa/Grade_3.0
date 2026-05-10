package br.sistema.controller.impl;

import br.sistema.controller.dtos.TurnoDTO;
import br.sistema.model.entity.Turno;
import br.sistema.util.TurnoMapper;
import java.util.stream.Collectors;
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
       Turno turno = TurnoMapper.INSTANCE.toEntity(turnoDTO);
       turnoService.save(turno);
    }

    @Override
    public void update(TurnoDTO turnoDTO) {
        Turno turno = TurnoMapper.INSTANCE.toEntity(turnoDTO);
        turnoService.update(turno);
    }

    @Override
    public void delete(TurnoDTO turnoDTO) {
       Turno turno = TurnoMapper.INSTANCE.toEntity(turnoDTO);
       turnoService.delete(turno);
    }

    @Override
    public List<TurnoDTO> findAll() {
       return turnoService.findAll().stream().map(TurnoMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    @Override
    public TurnoDTO findByName(String nome) {
        Turno turno = turnoService.findByName(nome);
        return TurnoMapper.INSTANCE.toDTO(turno);
    }
}