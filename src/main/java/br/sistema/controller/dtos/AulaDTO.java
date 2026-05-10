package br.sistema.controller.dtos;

import java.time.DayOfWeek;

public record AulaDTO(
        Long id,
        Long turmaId,
        Long professorId,
        Long disciplinaId,
        DayOfWeek diaDaSemana,
        Integer slotHorario
) {
}