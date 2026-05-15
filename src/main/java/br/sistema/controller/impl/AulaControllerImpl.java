package br.sistema.controller.impl;

import br.sistema.controller.dtos.AulaDTO;
import br.sistema.controller.dtos.DisciplinaDTO;
import br.sistema.controller.dtos.TurmaDTO;
import br.sistema.controller.interfaces.AulaController;
import br.sistema.model.entity.Aula;
import br.sistema.model.entity.Disciplina;
import br.sistema.model.entity.Turma;
import br.sistema.model.service.interfaces.AulaService;
import br.sistema.util.AulaMapper;
import br.sistema.util.DisciplinaMapper;
import br.sistema.util.TurmaMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public void gerarGrade(TurmaDTO turmaDTO, Map<DisciplinaDTO, Integer> cargaHoraria) {
        Turma turma = TurmaMapper.INSTANCE.toEntity(turmaDTO);
        turma.setNome(turmaDTO.nome()); // Garante o nome para as exceções do backend

        Map<Disciplina, Integer> mapEntity = new HashMap<>();
        for (Map.Entry<DisciplinaDTO, Integer> entry : cargaHoraria.entrySet()) {
            Disciplina d = DisciplinaMapper.INSTANCE.toEntity(entry.getKey());
            d.setNome(entry.getKey().nome());
            mapEntity.put(d, entry.getValue());
        }

        // Delega para o seu algoritmo de Backtracking!
        aulaService.gerarGrade(turma, mapEntity);
    }
}