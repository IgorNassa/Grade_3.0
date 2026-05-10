package br.sistema.controller.interfaces;

import br.sistema.controller.dtos.ProfessorDTO;
import java.util.List;


public interface ProfessorController {

    void save(ProfessorDTO professorDTO);

    void update(ProfessorDTO professorDTO);

    void delete(ProfessorDTO professorDTO);

    List<ProfessorDTO> findAll();

    ProfessorDTO findByName(String nome);
}