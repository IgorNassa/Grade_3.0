package br.sistema.model.repository.interfaces;

import br.sistema.model.entity.Turma;

import java.util.List;

public interface TurmaRepository {

    void save(Turma turma);

    Turma findById(Long id);

    List<Turma> findAll();

    void update(Turma turma);

    void delete(Turma turma);

    Turma findByName(String nome);
}