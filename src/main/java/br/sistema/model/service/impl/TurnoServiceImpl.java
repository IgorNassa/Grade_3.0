package br.sistema.model.service.impl;

import br.sistema.model.entity.Turno;
import br.sistema.model.repository.interfaces.TurnoRepository;
import br.sistema.model.service.interfaces.TurnoService;
import br.sistema.model.exception.DuplicateResourceExecption;
import br.sistema.model.exception.BusinessException;
import br.sistema.model.exception.ValidationException;
import br.sistema.model.exception.NotFoundException;
import java.util.List;

public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepository turnoRepository;

    public TurnoServiceImpl(TurnoRepository turnoRepository) {
        this.turnoRepository = turnoRepository;
    }

    @Override
    public void save(Turno turno) {
        try {
            validarTurno(turno);

            String nomeStr = turno.getNomeTurno().name();

            Turno existe = turnoRepository.findByName(nomeStr);

            if (existe != null) {
                throw new DuplicateResourceExecption("Já existe um turno cadastrado como " + nomeStr);
            }

            turnoRepository.save(turno);

        } catch (BusinessException e) {
            System.err.println("[ERRO SERVICE] " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Erro inesperado ao salvar turno: " + e.getMessage());
            throw new BusinessException("Erro inesperado ao salvar turno.", e);
        }
    }

    @Override
    public Turno findById(Long id) {
        try {
            if (id == null) {
                throw new ValidationException("ID do turno obrigatório.");
            }

            Turno turno = turnoRepository.findById(id);

            if (turno == null) {
                throw new NotFoundException("Turno não encontrado.");
            }

            return turno;

        } catch (BusinessException e) {
            System.err.println("[ERRO SERVICE] " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Erro inesperado ao buscar turno por ID: " + e.getMessage());
            throw new BusinessException("Erro inesperado ao buscar turno por ID.", e);
        }
    }

    @Override
    public void update(Turno turno) {
        try {
            validarTurno(turno);

            if (turno.getId() == null) {
                throw new ValidationException("ID do turno obrigatório para atualização.");
            }

            Turno turnoExiste = turnoRepository.findById(turno.getId());

            if (turnoExiste == null) {
                throw new NotFoundException("Turno não encontrado para atualização.");
            }

            Turno turnoComMesmoNome = turnoRepository.findByName(turno.getNomeTurno().name());

            if (turnoComMesmoNome != null && !turnoComMesmoNome.getId().equals(turno.getId())) {
                throw new DuplicateResourceExecption("Já existe outro turno cadastrado com este nome.");
            }

            turnoExiste.setNomeTurno(turno.getNomeTurno());
            turnoExiste.setInicioTurno(turno.getInicioTurno());
            turnoExiste.setFimTurno(turno.getFimTurno());
            turnoExiste.setTempoAula(turno.getTempoAula());
            turnoExiste.setAulasTurno(turno.getAulasTurno());

            turnoRepository.update(turnoExiste);

        } catch (BusinessException e) {
            System.err.println("[ERRO SERVICE] " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Erro inesperado ao atualizar turno: " + e.getMessage());
            throw new BusinessException("Erro inesperado ao atualizar turno.", e);
        }
    }

    @Override
    public void delete(Turno turno) {
        try {
            if (turno == null) {
                throw new ValidationException("Turno inválido para exclusão.");
            }

            Turno turnoNoBanco = null;

            if (turno.getId() != null) {
                turnoNoBanco = turnoRepository.findById(turno.getId());
            }

            if (turnoNoBanco == null && turno.getNomeTurno() != null) {
                turnoNoBanco = turnoRepository.findByName(turno.getNomeTurno().name());
            }

            if (turnoNoBanco == null) {
                throw new NotFoundException("Turno não encontrado para exclusão.");
            }

            turnoRepository.delete(turnoNoBanco);

        } catch (BusinessException e) {
            System.err.println("[ERRO SERVICE] " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Erro inesperado ao excluir turno: " + e.getMessage());
            throw new BusinessException("Erro inesperado ao excluir turno.", e);
        }
    }

    @Override
    public List<Turno> findAll() {
        try {
            return turnoRepository.findAll();

        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Erro inesperado ao listar turnos: " + e.getMessage());
            throw new BusinessException("Erro inesperado ao listar turnos.", e);
        }
    }

    @Override
    public Turno findByName(String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                return null;
            }

            return turnoRepository.findByName(nome.trim().toUpperCase());

        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Erro inesperado ao buscar turno por nome: " + e.getMessage());
            return null;
        }
    }

    private void validarTurno(Turno turno) {
        if (turno == null) {
            throw new ValidationException("Turno inválido.");
        }

        if (turno.getNomeTurno() == null) {
            throw new ValidationException("Nome inválido.");
        }

        if (turno.getInicioTurno() == null || turno.getFimTurno() == null) {
            throw new ValidationException("Horário vazio.");
        }

        if (turno.getInicioTurno().isAfter(turno.getFimTurno())) {
            throw new ValidationException("Início após o fim.");
        }

        if (turno.getTempoAula() == null || turno.getTempoAula() <= 0) {
            throw new ValidationException("Tempo de aula inválido.");
        }

        if (turno.getAulasTurno() == null || turno.getAulasTurno() <= 0) {
            throw new ValidationException("Qtd aulas inválida.");
        }
    }
}