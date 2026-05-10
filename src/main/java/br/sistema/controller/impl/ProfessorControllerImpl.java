package br.sistema.controller.impl;

import br.sistema.controller.dtos.ProfessorDTO;
import br.sistema.controller.interfaces.ProfessorController;
import br.sistema.model.service.interfaces.ProfessorService;
import java.util.List;

public class ProfessorControllerImpl implements ProfessorController {

    private final ProfessorService professorService;

    public ProfessorControllerImpl(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @Override
    public void save(ProfessorDTO professorDTO) {
        professorService.save(professorDTO);
    }

    @Override
    public void update(ProfessorDTO professorDTO) {
        professorService.update(professorDTO);
    }

    @Override
    public void delete(ProfessorDTO professorDTO) {
        professorService.delete(professorDTO);
    }

    @Override
    public List<ProfessorDTO> findAll() {
        return professorService.findAll();
    }

    @Override
    public ProfessorDTO findByName(String nome) {
        return professorService.findByName(nome);
    }
}