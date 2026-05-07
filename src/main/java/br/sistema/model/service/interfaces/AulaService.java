package br.sistema.model.service.interfaces;

import br.sistema.model.entity.Disciplina;
import br.sistema.model.entity.Professor;
import br.sistema.model.entity.Turma;

import java.util.Map;

public interface AulaService {

    void gerarGrade(Turma turma, Map<Disciplina, Integer> cargaHoraria);

    Professor buscarProfessorDaDisciplina(Disciplina disciplina);
}
