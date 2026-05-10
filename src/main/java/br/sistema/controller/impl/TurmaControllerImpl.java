package br.sistema.controller.impl;

import br.sistema.controller.dtos.TurmaDTO;
import br.sistema.controller.interfaces.TurmaController;
import br.sistema.model.service.interfaces.TurmaService;
import java.util.List;

public class TurmaControllerImpl implements TurmaController {

    private final TurmaService turmaService;

    public TurmaControllerImpl(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    @Override
    public void save(TurmaDTO turmaDTO) {
        turmaService.save(turmaDTO);
    }

    @Override
    public void update(TurmaDTO turmaDTO) {
        turmaService.update(turmaDTO);
    }

    @Override
    public void delete(TurmaDTO turmaDTO) {
        turmaService.delete(turmaDTO);
    }

    @Override
    public List<TurmaDTO> findAll() {
        return turmaService.findAll();
    }

    @Override
    public TurmaDTO findByName(String nome) {
        return turmaService.findByName(nome);
    }
}