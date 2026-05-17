package br.sistema.model.service.impl;

import br.sistema.model.entity.Professor;
import br.sistema.model.exception.ValidationException;
import br.sistema.model.exception.NotFoundException;
import br.sistema.model.exception.DuplicateResourceExecption;
import br.sistema.model.exception.BusinessException;
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
                throw new DuplicateResourceExecption("Professor já cadastrado!");
            }

            professorRepository.save(professor);

        } catch (BusinessException e){
            System.err.println("[ERRO SERVICE]" + e.getMessage());
            throw e;
        }
        catch (Exception e){
            System.err.println("[ERRO SERVICE] Erro inesperado ao salvar professor: " + e.getMessage());
            throw new BusinessException("Erro inesperado ao salvar professor.", e);
        }
    }

    @Override
    public Professor findById(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("ID do professor obrigatório.");
            }

            Professor professor = professorRepository.findById(id);

            if (professor == null) {
                throw new NotFoundException("Professor não encontrado.");
            }

            return professor;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação findById: " + e.getMessage());
            throw new BusinessException("Erro ao buscar professor pelo ID.", e);
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
                throw new ValidationException("ID do professor obrigatório para atualização.");
            }

            Professor professorExiste = professorRepository.findById(professor.getId());

            if (professorExiste == null) {
                throw new NotFoundException("Professor não encontrado para atualizar.");
            }

            Professor professorComMesmoNome = professorRepository.findByName(professor.getNome());

            if (professorComMesmoNome != null && !professorComMesmoNome.getId().equals(professor.getId())) {
                throw new DuplicateResourceExecption("Já existe outro professor cadastrado com este nome.");
            }

            professorExiste.setNome(professor.getNome());
            professorExiste.setDisciplinas(professor.getDisciplinas());

            professorRepository.update(professorExiste);

        } catch (BusinessException e) {
            System.err.println("[ERRO] Falha na operação update: " + e.getMessage());
            throw e;
        }catch (Exception e){
            System.err.println("[ERRO SERVICE] Erro inesperado ao atualizar professor: " + e.getMessage());
            throw new BusinessException("Erro inesperado ao atualizar professor.", e);
        }
    }

    @Override
    public void delete(Professor professor) {
        try {
            if (professor == null) {
                throw new ValidationException("Professor inválido para exclusão.");
            }

            Professor professorNoBanco = null;

            if (professor.getId() != null) {
                professorNoBanco = professorRepository.findById(professor.getId());
            }

            if (professorNoBanco == null && professor.getNome() != null && !professor.getNome().trim().isEmpty()) {
                professorNoBanco = professorRepository.findByName(professor.getNome());
            }

            if (professorNoBanco == null) {
                throw new NotFoundException("Professor não encontrado para exclusão.");
            }

            professorRepository.delete(professorNoBanco);

        } catch (BusinessException e){
            System.err.println("[ERRO SERVICE] " + e.getMessage());
            throw e;
        }catch (Exception e){
            System.err.println("[ERRO SERVICE] Erro inesperado ao excluir professor: " + e.getMessage());
            throw new BusinessException("Erro inesperado ao excluir professor.", e);
        }
    }

    private void validarProfessor(Professor professor) {
        if (professor == null) {
            throw new ValidationException("Professor inválido.");
        }

        if (professor.getNome() == null || professor.getNome().trim().isEmpty()) {
            throw new ValidationException("Nome do professor obrigatório.");
        }

        professor.setNome(professor.getNome().trim());
    }
}