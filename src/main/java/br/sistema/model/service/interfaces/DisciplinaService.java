package br.sistema.model.service.interfaces;

import br.sistema.model.entity.Disciplina;

import java.util.List;

public interface DisciplinaService {

    void save(Disciplina disciplina);

    Disciplina findById(Long id);

    Disciplina findByName(String nome);

    void update(Disciplina disciplina);

    void delete(Disciplina disciplina);

    List<Disciplina> findAll();
}