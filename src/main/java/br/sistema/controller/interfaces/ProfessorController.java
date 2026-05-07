package br.sistema.controller.interfaces;

import br.sistema.controller.dtos.ProfessorDTO;
import java.util.List;

public interface ProfessorController {
    void salvar(ProfessorDTO professorDTO);
    List<ProfessorDTO> listarTodos();
    void execluir(Long id);

    List<ProfessorDTO> buscarPornome(String nome);
}
