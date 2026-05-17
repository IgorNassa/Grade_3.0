package br.sistema.controller.impl;

import br.sistema.controller.dtos.DisciplinaDTO;
import br.sistema.controller.interfaces.DisciplinaController;
import br.sistema.model.entity.Disciplina;
import br.sistema.model.service.interfaces.DisciplinaService;
import br.sistema.util.DisciplinaMapper;

import java.util.List;
import java.util.stream.Collectors;

public class DisciplinaControllerImpl implements DisciplinaController {

    private final DisciplinaService disciplinaService;

    public DisciplinaControllerImpl(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @Override
    public void save(DisciplinaDTO disciplinaDTO) {
        Disciplina disciplina = DisciplinaMapper.INSTANCE.toEntity(disciplinaDTO);
        disciplinaService.save(disciplina);
    }

    @Override
    public void update(DisciplinaDTO disciplinaDTO) {
        Disciplina disciplina = DisciplinaMapper.INSTANCE.toEntity(disciplinaDTO);
        disciplinaService.update(disciplina);
    }

    @Override
    public void delete(DisciplinaDTO disciplinaDTO) {
        Disciplina disciplina = DisciplinaMapper.INSTANCE.toEntity(disciplinaDTO);
        disciplinaService.delete(disciplina);
    }

    @Override
    public List<DisciplinaDTO> findAll() {
        return disciplinaService.findAll()
                .stream()
                .map(DisciplinaMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DisciplinaDTO findByName(String nome) {
        Disciplina disciplina = disciplinaService.findByName(nome);
        return DisciplinaMapper.INSTANCE.toDTO(disciplina);
    }
}