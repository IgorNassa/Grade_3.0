package br.sistema.controller.impl;

import br.sistema.controller.dtos.AulaDTO;
import br.sistema.controller.interfaces.AulaController;
import br.sistema.model.entity.Aula;
import br.sistema.model.service.interfaces.AulaService;
import br.sistema.util.AulaMapper;

import java.util.List;
import java.util.stream.Collectors;

public class AulaControllerImpl implements AulaController {

    private final AulaService aulaService;

    public AulaControllerImpl(AulaService aulaService) {
        this.aulaService = aulaService;
    }

    @Override
    public void save(AulaDTO aulaDTO) {
        Aula aula = AulaMapper.INSTANCE.toEntity(aulaDTO);
        aulaService.save(aula);
    }

    @Override
    public void update(AulaDTO aulaDTO) {
        Aula aula = AulaMapper.INSTANCE.toEntity(aulaDTO);
        aulaService.update(aula);
    }

    @Override
    public void delete(AulaDTO aulaDTO) {
        Aula aula = AulaMapper.INSTANCE.toEntity(aulaDTO);
        aulaService.delete(aula);
    }

    @Override
    public List<AulaDTO> findAll() {
        return aulaService.findAll()
                .stream()
                .map(AulaMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }
}