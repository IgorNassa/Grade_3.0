package br.sistema.model.repository.interfaces;

import br.sistema.model.entity.Disciplina;

import java.util.List;

public interface DisciplinaRepository {

    void save(Disciplina disciplina);

    List<Disciplina> findAll();

    void update(Disciplina disciplina);

    void delete(Disciplina disciplina);

    Disciplina findById(Long id);

    Disciplina findByName(String nome);
}