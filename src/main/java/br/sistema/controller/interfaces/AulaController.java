package br.sistema.controller.interfaces;

import br.sistema.controller.dtos.AulaDTO;
import br.sistema.controller.dtos.DisciplinaDTO;
import br.sistema.controller.dtos.TurmaDTO;

import java.util.List;
import java.util.Map;

public interface AulaController {

    void save(AulaDTO aulaDTO);

    void update(AulaDTO aulaDTO);

    void delete(AulaDTO aulaDTO);

    List<AulaDTO> findAll();

    // Novo contrato para acionar o algoritmo de geração
    void gerarGrade(TurmaDTO turmaDTO, Map<DisciplinaDTO, Integer> cargaHoraria, String turno);
    void deleteByTurma(TurmaDTO turmaDTO);
}