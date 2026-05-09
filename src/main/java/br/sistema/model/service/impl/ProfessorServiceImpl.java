package br.sistema.model.service.impl;

import br.sistema.model.entity.Professor;
import br.sistema.model.repository.interfaces.ProfessorRepository;
import br.sistema.model.service.interfaces.ProfessorService;

import java.util.List;

public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorServiceImpl(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Override
    public void save(Professor professor) {
        try {
            validarProfessor(professor);

            Professor existe = professorRepository.findByName(professor.getNome());

            if (existe != null) {
                throw new RuntimeException("Professor já cadastrado!");
            }

            professorRepository.save(professor);

        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Professor findById(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("ID do professor obrigatório.");
            }

            Professor professor = professorRepository.findById(id);

            if (professor == null) {
                throw new RuntimeException("Professor não encontrado.");
            }

            return professor;

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação findById: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar professor pelo ID.", e);
        }
    }

    @Override
    public Professor findByName(String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                return null;
            }

            return professorRepository.findByName(nome.trim());

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Professor> findAll() {
        try {
            return professorRepository.findAll();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar a lista de professores.", e);
        }
    }

    @Override
    public void update(Professor professor) {
        try {
            validarProfessor(professor);

            if (professor.getId() == null) {
                throw new IllegalArgumentException("ID do professor obrigatório para atualização.");
            }

            Professor professorExiste = professorRepository.findById(professor.getId());

            if (professorExiste == null) {
                throw new RuntimeException("Professor não encontrado para atualizar.");
            }

            Professor professorComMesmoNome = professorRepository.findByName(professor.getNome());

            if (professorComMesmoNome != null && !professorComMesmoNome.getId().equals(professor.getId())) {
                throw new RuntimeException("Já existe outro professor cadastrado com este nome.");
            }

            professorExiste.setNome(professor.getNome());
            professorExiste.setDisciplinas(professor.getDisciplinas());

            professorRepository.update(professorExiste);

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação update: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar o professor.", e);
        }
    }

    @Override
    public void delete(Professor professor) {
        try {
            if (professor == null) {
                throw new IllegalArgumentException("Professor inválido para exclusão.");
            }

            Professor professorNoBanco = null;

            if (professor.getId() != null) {
                professorNoBanco = professorRepository.findById(professor.getId());
            }

            if (professorNoBanco == null && professor.getNome() != null && !professor.getNome().trim().isEmpty()) {
                professorNoBanco = professorRepository.findByName(professor.getNome());
            }

            if (professorNoBanco == null) {
                throw new RuntimeException("Professor não encontrado para exclusão.");
            }

            professorRepository.delete(professorNoBanco);

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação delete: " + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void validarProfessor(Professor professor) {
        if (professor == null) {
            throw new IllegalArgumentException("Professor inválido.");
        }

        if (professor.getNome() == null || professor.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do professor obrigatório.");
        }

        professor.setNome(professor.getNome().trim());
    }
}