package br.sistema.model.repository.interfaces;

import br.sistema.model.entity.Aula;
import br.sistema.model.entity.Professor;

import java.time.DayOfWeek;

public interface AulaRepository {

    void save(Aula aula);

    boolean professorOcupadoNoBanco(Professor professor, DayOfWeek dia, Integer slot);
}
