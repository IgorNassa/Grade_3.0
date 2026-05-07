package br.sistema.controller.dtos;
import java.util.List;

public record TurmaDTO(
        Long id,
        String nome,
        boolean eMedio,
        List<DisciplinaDTO> disciplinas // Relacionamento mapeado para DTO
) {}