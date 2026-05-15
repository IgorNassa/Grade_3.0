package br.sistema.controller.dtos;

import java.util.List;

public record ProfessorDTO(
        Long id,
        String nome,
        List<String> disciplinas
) {
}
