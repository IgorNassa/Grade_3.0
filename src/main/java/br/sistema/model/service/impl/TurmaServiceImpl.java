package br.sistema.model.service.impl;

import br.sistema.model.entity.Turma;
import br.sistema.model.repository.interfaces.TurmaRepository;
import br.sistema.model.service.interfaces.TurmaService;

import java.util.List;

public class TurmaServiceImpl implements TurmaService {

    private final TurmaRepository turmaRepository;

    public TurmaServiceImpl(TurmaRepository turmaRepositoryImpl) {
        this.turmaRepository = turmaRepositoryImpl;
    }

    public void save(Turma turma) {
        try {
            if (turma.getNome() == null || turma.getNome().trim().isEmpty()) {
                throw new IllegalArgumentException("Nome da turma é obrigatório.");
            }

            // Busca para evitar duplicados (IgnoreCase)
            Turma existe = turmaRepository.findByName(turma.getNome().trim());
            if (existe != null) {
                throw new RuntimeException("Já existe uma turma com o nome: " + turma.getNome());
            }

            turmaRepository.save(turma);
        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação salvar: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Turma findById(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("ID da turma obrigatório.");
            }

            Turma turma = turmaRepository.findById(id);

            if (turma == null) {
                throw new RuntimeException("Turma não encontrada.");
            }

            return turma;

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação findById: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar turma pelo ID.", e);
        }
    }

    public void update(Turma turma) {
        try {
            Turma existente = turmaRepository.findByName(turma.getNome());
            if (existente == null) {
                throw new RuntimeException("Turma não encontrada para atualização.");
            }
            turmaRepository.update(turma);
        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação update: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(Turma turma) {
        try {
            Turma existente = turmaRepository.findByName(turma.getNome());
            if (existente == null) {
                throw new RuntimeException("Turma não encontrada para exclusão.");
            }
            turmaRepository.delete(existente);
        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação delete: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Turma> findAll() {
        return turmaRepository.findAll();
    }

    public Turma findByName(String nome) {
        // No Repository, garanta que a query use LOWER(t.nome) = LOWER(:nome)
        return turmaRepository.findByName(nome.trim());
    }
}