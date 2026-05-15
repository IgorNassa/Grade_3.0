package br.sistema.model.service.interfaces;
import br.sistema.model.entity.Disciplina;
import br.sistema.model.entity.Turma;
import java.util.List;

public interface TurmaService {

    void save(Turma turma);

    Turma findByName(String nome);

    Turma findById(Long id);

    void update(Turma turma);

    void delete(Turma turma);

    List<Turma> findAll();

}
