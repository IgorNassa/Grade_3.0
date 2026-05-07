package br.sistema.controller.dtos;
import java.time.DayOfWeek;
import br.sistema.model.entity.Aula;
import java.time.DayOfWeek;

    public record AulaDTO(
            Long id,
            TurmaDTO turma,
            ProfessorDTO professor,
            DisciplinaDTO disciplina,
            DayOfWeek diaDaSemana,
            Integer slotHorario
    ) {}


