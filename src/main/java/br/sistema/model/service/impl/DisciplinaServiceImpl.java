package br.sistema.model.service.impl;

import br.sistema.model.entity.Disciplina;
import br.sistema.model.exception.BusinessException;
import br.sistema.model.exception.NotFoundException;
import br.sistema.model.exception.ValidationException;
import br.sistema.model.exception.DuplicateResourceExecption;
import br.sistema.model.repository.interfaces.DisciplinaRepository;
import br.sistema.model.service.interfaces.DisciplinaService;

import java.util.List;

public class DisciplinaServiceImpl implements DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaServiceImpl(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    @Override
    public void save(Disciplina disciplina) {
        try {
            validarDisciplina(disciplina);

            Disciplina disciplinaExiste = disciplinaRepository.findByName(disciplina.getNome());

            if (disciplinaExiste != null) {
                throw new DuplicateResourceExecption("Já existe uma disciplina cadastrada com este nome: " + disciplina.getNome());
            }

            disciplinaRepository.save(disciplina);

        } catch (BusinessException e) {
            System.err.println("[ERRO SERVICE] " + e.getMessage());
            throw e;
        }
        catch (Exception e) {
            System.err.println("[ERRO] Falha na operação salvar: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar a disciplina.", e);
        }
    }

    @Override
    public void update(Disciplina disciplina) {
        try {
            validarDisciplina(disciplina);

            if (disciplina.getId() == null) {
                throw new ValidationException("O ID da disciplina é obrigatório para atualização.");
            }

            Disciplina disciplinaExiste = disciplinaRepository.findById(disciplina.getId());

            if (disciplinaExiste == null) {
                throw new NotFoundException("Disciplina não encontrada na base de dados para atualizar.");
            }

            Disciplina disciplinaComMesmoNome = disciplinaRepository.findByName(disciplina.getNome());

            if (disciplinaComMesmoNome != null && !disciplinaComMesmoNome.getId().equals(disciplina.getId())) {
                throw new DuplicateResourceExecption("Já existe outra disciplina cadastrada com este nome: " + disciplina.getNome());
            }

            disciplinaExiste.setNome(disciplina.getNome());

            disciplinaRepository.update(disciplinaExiste);

        } catch (BusinessException e) {
            System.err.println("[ERRO SERVICE] " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Erro inesperado ao atualizar disciplina: " + e.getMessage());
            throw new BusinessException("Erro inesperado ao atualizar disciplina.", e);
        }
    }

    @Override
    public void delete(Disciplina disciplina) {
        try {
            if (disciplina == null) {
                throw new ValidationException("Disciplina inválida para exclusão.");
            }

            Disciplina disciplinaExiste = null;

            if (disciplina.getId() != null) {
                disciplinaExiste = disciplinaRepository.findById(disciplina.getId());
            }

            if (disciplinaExiste == null && disciplina.getNome() != null && !disciplina.getNome().trim().isEmpty()) {
                disciplinaExiste = disciplinaRepository.findByName(disciplina.getNome());
            }

            if (disciplinaExiste == null) {
                throw new NotFoundException("Disciplina não encontrada na base de dados para exclusão.");
            }

            disciplinaRepository.delete(disciplinaExiste);

        } catch (BusinessException e) {
            System.err.println("[ERRO SERVICE] " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Erro inesperado ao excluir disciplina: " + e.getMessage());
            throw new BusinessException("Erro inesperado ao excluir disciplina.", e);
        }
    }

    @Override
    public List<Disciplina> findAll() {
        try {
            List<Disciplina> disciplinas = disciplinaRepository.findAll();

            if (disciplinas == null || disciplinas.isEmpty()) {
                System.out.println("Nenhuma disciplina cadastrada no momento.");
            }

            return disciplinas;

        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Erro inesperado ao listar disciplinas: " + e.getMessage());
            throw new BusinessException("Erro inesperado ao listar disciplinas.", e);
        }
    }

    @Override
    public Disciplina findById(Long id){
        try {
            if(id == null){
                throw new ValidationException("O ID da disciplina é obrigatorio para busca.");
            }
            Disciplina disciplina = disciplinaRepository.findById(id);

            if (disciplina == null) {
                throw new NotFoundException("Disciplina nao encontrada.");
            }
            return  disciplina;

        } catch (BusinessException e) {
            System.err.println("[ERRO SERVICE] " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Erro inesperado ao buscar disciplina por ID: " + e.getMessage());
            throw new BusinessException("Erro inesperado ao buscar disciplina por ID.", e);
        }
    }

    @Override
    public Disciplina findByName(String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                return null;
            }

            return disciplinaRepository.findByName(nome);

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação findByName: " + e.getMessage());
            throw new BusinessException("Erro ao buscar a disciplina pelo nome.", e);
        }
    }

    private void validarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new ValidationException("Disciplina inválida.");
        }

        if (disciplina.getNome() == null || disciplina.getNome().trim().isEmpty()) {
            throw new ValidationException("O nome da disciplina não pode ser vazio.");
        }

        disciplina.setNome(disciplina.getNome().trim());
    }
}

