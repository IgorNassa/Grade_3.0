package br.sistema.controller.impl;

import br.sistema.controller.dtos.DisciplinaDTO;
import br.sistema.controller.interfaces.DisciplinaController;
import br.sistema.model.service.interfaces.DisciplinaService;
import java.util.List;

public class DisciplinaControllerImpl implements DisciplinaController {

    private final DisciplinaService disciplinaService;

    public DisciplinaControllerImpl(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @Override
    public void save(DisciplinaDTO disciplinaDTO) {
        disciplinaService.save(disciplinaDTO);
    }

    @Override
    public void update(DisciplinaDTO disciplinaDTO) {
        disciplinaService.update(disciplinaDTO);
    }

    @Override
    public void delete(DisciplinaDTO disciplinaDTO) {
        disciplinaService.delete(disciplinaDTO);
    }

    @Override
    public List<DisciplinaDTO> findAll() {
        return disciplinaService.findAll();
    }

    @Override
    public DisciplinaDTO findByName(String nome) {
        return disciplinaService.findByName(nome);
    }
}