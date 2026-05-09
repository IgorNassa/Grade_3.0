package br.sistema.model.repository.interfaces;

import br.sistema.model.entity.Aula;
import br.sistema.model.entity.Professor;
import br.sistema.model.entity.Turma;
import java.time.DayOfWeek;
import java.util.List;

public interface AulaRepository {

    void save(Aula aula);

    void saveAll(List<Aula> aulas);

    Aula findById(Long id);

    List<Aula> findAll();

    void update(Aula aula);

    void delete(Aula aula);

    List<Aula> findByTurma(Turma turma);

    List<Aula> findByProfessor(Professor professor);

    void deleteByTurma(Turma turma);

    boolean professorOcupadoNoBanco(Professor professor, DayOfWeek dia, Integer slot);

    boolean turmaOcupadaNoBanco(Turma turma, DayOfWeek dia, Integer slot);


}
