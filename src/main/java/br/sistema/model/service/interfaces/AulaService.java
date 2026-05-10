package br.sistema.model.service.interfaces;

import br.sistema.model.entity.Aula;
import br.sistema.model.entity.Disciplina;
import br.sistema.model.entity.Professor;
import br.sistema.model.entity.Turma;

import java.util.List;
import java.util.Map;

public interface AulaService {

    void gerarGrade(Turma turma, Map<Disciplina, Integer> cargaHoraria);

    Professor buscarProfessorDaDisciplina(Disciplina disciplina);

    void save(Aula aula);

    void update(Aula aula);

    void delete(Aula aula);

    List<Aula> findAll();
}