package br.sistema.model.service.impl;

import br.sistema.model.entity.Turma;
import br.sistema.model.exception.BusinessException;
import br.sistema.model.exception.DuplicateResourceExecption;
import br.sistema.model.exception.NotFoundException;
import br.sistema.model.exception.ValidationException;
import br.sistema.model.repository.interfaces.TurmaRepository;
import br.sistema.model.service.interfaces.TurmaService;

import java.util.List;

public class TurmaServiceImpl implements TurmaService {

    private final TurmaRepository turmaRepository;

    public TurmaServiceImpl(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    @Override
    public void save(Turma turma) {
        try {
            validarTurma(turma);

            Turma existe = turmaRepository.findByName(turma.getNome());

            if (existe != null) {
                throw new DuplicateResourceExecption("Já existe uma turma com o nome: " + turma.getNome());
            }

            turmaRepository.save(turma);

        } catch (BusinessException e) {
            System.err.println("[ERRO SERVICE] " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Erro inesperado ao salvar turma: " + e.getMessage());
            throw new BusinessException("Erro inesperado ao salvar turma.", e);
        }
    }

    @Override
    public Turma findById(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("ID da turma obrigatório.");
            }

            Turma turma = turmaRepository.findById(id);

            if (turma == null) {
                throw new NotFoundException("Turma não encontrada.");
            }

            return turma;

        } catch (BusinessException e) {
            System.err.println("[ERRO SERVICE] " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Erro inesperado ao buscar turma por ID: " + e.getMessage());
            throw new BusinessException("Erro inesperado ao buscar turma por ID.", e);
        }
    }

    @Override
    public void update(Turma turma) {
        try {
            validarTurma(turma);

            if (turma.getId() == null) {
                throw new ValidationException("ID da turma obrigatório para atualização.");
            }

            Turma turmaExiste = turmaRepository.findById(turma.getId());

            if (turmaExiste == null) {
                throw new NotFoundException("Turma não encontrada para atualização.");
            }

            Turma turmaComMesmoNome = turmaRepository.findByName(turma.getNome());

            if (turmaComMesmoNome != null && !turmaComMesmoNome.getId().equals(turma.getId())) {
                throw new DuplicateResourceExecption("Já existe outra turma cadastrada com este nome.");
            }

            turmaExiste.setNome(turma.getNome());

            turmaRepository.update(turmaExiste);

        } catch (BusinessException e) {
            System.err.println("[ERRO SERVICE] " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Erro inesperado ao atualizar turma: " + e.getMessage());
            throw new BusinessException("Erro inesperado ao atualizar turma.", e);
        }
    }

    @Override
    public void delete(Turma turma) {
        try {
            if (turma == null) {
                throw new ValidationException("Turma inválida para exclusão.");
            }

            Turma turmaNoBanco = null;

            if (turma.getId() != null) {
                turmaNoBanco = turmaRepository.findById(turma.getId());
            }

            if (turmaNoBanco == null && turma.getNome() != null && !turma.getNome().trim().isEmpty()) {
                turmaNoBanco = turmaRepository.findByName(turma.getNome());
            }

            if (turmaNoBanco == null) {
                throw new NotFoundException("Turma não encontrada para exclusão.");
            }

            turmaRepository.delete(turmaNoBanco);

        } catch (BusinessException e) {
            System.err.println("[ERRO SERVICE] " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Erro inesperado ao excluir turma: " + e.getMessage());
            throw new BusinessException("Erro inesperado ao excluir turma.", e);
        }
    }

    @Override
    public List<Turma> findAll() {
        try {
            return turmaRepository.findAll();

        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Erro inesperado ao listar turmas: " + e.getMessage());
            throw new BusinessException("Erro inesperado ao listar turmas.", e);
        }
    }

    @Override
    public Turma findByName(String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                return null;
            }

            return turmaRepository.findByName(nome.trim());

        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Erro inesperado ao buscar turma por nome: " + e.getMessage());
            return null;
        }
    }

    private void validarTurma(Turma turma) {
        if (turma == null) {
            throw new ValidationException("Turma inválida.");
        }

        if (turma.getNome() == null || turma.getNome().trim().isEmpty()) {
            throw new ValidationException("Nome da turma é obrigatório.");
        }

        turma.setNome(turma.getNome().trim());
    }
}