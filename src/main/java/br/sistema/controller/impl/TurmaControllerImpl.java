package br.sistema.controller.impl;

import br.sistema.controller.dtos.TurmaDTO;
import br.sistema.model.entity.Turma;
import br.sistema.util.TurmaMapper;
import java.util.stream.Collectors;
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
      Turma turma = TurmaMapper.INSTANCE.toEntity(turmaDTO);
      turmaService.save(turma);
    }
    //converte o turmaDTO para turma

    @Override
    public void update(TurmaDTO turmaDTO) {
        Turma turma = TurmaMapper.INSTANCE.toEntity(turmaDTO);
        turmaService.update(turma);

    }

    @Override
    public void delete(TurmaDTO turmaDTO) {
        Turma turma = TurmaMapper.INSTANCE.toEntity(turmaDTO);
        turmaService.delete(turma);

    }

    @Override
    public List<TurmaDTO> findAll() {
      return turmaService.findAll().stream().map(TurmaMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    @Override
    public TurmaDTO findByName(String nome) {
        Turma turma = turmaService.findByName(nome);
        return TurmaMapper.INSTANCE.toDTO(turma);
    }
}