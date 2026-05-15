package br.sistema.model.service.interfaces;

import br.sistema.model.entity.Professor;

import java.util.List;

public interface ProfessorService {

    void save(Professor professor);

    Professor findById(Long id);

    Professor findByName(String nome);

    List<Professor> findAll();

    void update(Professor professor);

    void delete(Professor professor);
}