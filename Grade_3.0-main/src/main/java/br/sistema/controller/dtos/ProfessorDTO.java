package br.sistema.controller.dtos;
import br.sistema.model.entity.Professor;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public record ProfessorDTO(
        Long id,
        String nome,
        String cpf,
        String especialidade
) {}
