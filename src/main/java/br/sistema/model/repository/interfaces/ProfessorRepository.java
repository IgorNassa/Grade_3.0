package br.sistema.model.repository.interfaces;

import br.sistema.model.entity.Disciplina;
import br.sistema.model.entity.Professor;

import java.time.DayOfWeek;
import java.util.List;

public interface ProfessorRepository {

    void save(Professor professor);

    Professor findById(Long id);

    List<Professor> findAll();

    void update(Professor professor);

    void delete(Professor professor);

    Professor findByName(String nome);

    List<Professor> findByDisciplina(Disciplina disciplina);

    boolean isProfessorDisponivel(Professor professor, DayOfWeek dia, Integer slot);
}