package br.sistema.model.service.interfaces;
import br.sistema.model.entity.Professor;
import java.util.List;

public interface ProfessorService {

    void save(Professor professor);

    Professor findByName(String nome);

    void update(Professor professor);

    void delete(Professor professor);

    List<Professor> findAll();
}
