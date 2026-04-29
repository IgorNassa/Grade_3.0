package br.sistema.model.service.impl;

import br.sistema.model.entity.Turma;
import br.sistema.model.repository.impl.TurmaRepositoryImpl;
import java.util.List;

public class TurmaServiceImpl {

    private final TurmaRepositoryImpl turmaRepositoryImpl;

    public TurmaServiceImpl(TurmaRepositoryImpl turmaRepositoryImpl) {
        this.turmaRepositoryImpl = turmaRepositoryImpl;
    }

    public void save(Turma turma) {
        try {
            if (turma.getNome() == null || turma.getNome().trim().isEmpty()) {
                throw new IllegalArgumentException("Nome da turma é obrigatório.");
            }

            // Busca para evitar duplicados (IgnoreCase)
            Turma existe = turmaRepositoryImpl.findByName(turma.getNome().trim());
            if (existe != null) {
                throw new RuntimeException("Já existe uma turma com o nome: " + turma.getNome());
            }

            turmaRepositoryImpl.save(turma);
        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação salvar: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void update(Turma turma) {
        try {
            Turma existente = turmaRepositoryImpl.findByName(turma.getNome());
            if (existente == null) {
                throw new RuntimeException("Turma não encontrada para atualização.");
            }
            turmaRepositoryImpl.update(turma);
        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação update: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(Turma turma) {
        try {
            Turma existente = turmaRepositoryImpl.findByName(turma.getNome());
            if (existente == null) {
                throw new RuntimeException("Turma não encontrada para exclusão.");
            }
            turmaRepositoryImpl.delete(existente);
        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação delete: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Turma> findAll() {
        return turmaRepositoryImpl.findAll();
    }

    public Turma findByName(String nome) {
        // No Repository, garanta que a query use LOWER(t.nome) = LOWER(:nome)
        return turmaRepositoryImpl.findByName(nome.trim());
    }
}