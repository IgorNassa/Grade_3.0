package br.sistema.model.service.impl;

import br.sistema.model.entity.Professor;
import br.sistema.model.repository.impl.ProfessorRepositoryImpl;
import br.sistema.model.service.interfaces.ProfessorService;

import java.util.List;

public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepositoryImpl professorRepositoryImpl;

    public ProfessorServiceImpl(ProfessorRepositoryImpl professorRepositoryImpl) {
        this.professorRepositoryImpl = professorRepositoryImpl;
    }

    public void save(Professor professor) {
        try {
            if (professor.getNome() == null || professor.getNome().trim().isEmpty()) {
                throw new IllegalArgumentException("Nome do professor obrigatório.");
            }

            Professor existe = professorRepositoryImpl.findByName(professor.getNome());
            if (existe != null) {
                throw new RuntimeException("Professor já cadastrado!");
            }

            professorRepositoryImpl.save(professor);
        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] " + e.getMessage());
            throw e;
        }
    }

    public void update(Professor professor) {
        try {
            if (professor.getNome() == null || professor.getNome().trim().isEmpty()) {
                throw new IllegalArgumentException("Nome inválido para atualização.");
            }

            Professor professorExiste = professorRepositoryImpl.findByName(professor.getNome().toLowerCase().trim());
            if (professorExiste == null) {
                throw new RuntimeException("Professor não encontrado para atualizar.");
            }

            professorRepositoryImpl.update(professor);

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação update: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar o professor.", e);
        }
    }

    public void delete(Professor professor) {
        try {
            // 1. Busca o professor real no banco pelo nome (usando o ignore case que aplicamos no repo)
            Professor professorNoBanco = professorRepositoryImpl.findByName(professor.getNome().trim());

            if (professorNoBanco == null) {
                throw new RuntimeException("Professor não encontrado para exclusão: " + professor.getNome());
            }

            // 2. Passa o objeto "anexado" (managed) para o repositório deletar
            professorRepositoryImpl.delete(professorNoBanco);

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação delete: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Professor> findAll() {
        try {
            return professorRepositoryImpl.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar a lista de professores.", e);
        }
    }

    public Professor findByName(String nome) {
        try {
            return professorRepositoryImpl.findByName(nome.toLowerCase().trim());
        } catch (Exception e) {
            return null;
        }
    }
}