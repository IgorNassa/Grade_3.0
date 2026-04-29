package br.sistema.controller.dtos;

import br.sistema.model.entity.TipoTurno;

import java.time.LocalTime;

public record TurnoDTO(
        TipoTurno nomeTurno,
        LocalTime inicioTurno,
        LocalTime fimTurno,
        Integer tempoAula,
        Integer aulasTurno,
        Integer aulasAntesIntervalo
) {
}
