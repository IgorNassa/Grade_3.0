package br.sistema.model.service.impl;

import br.sistema.model.entity.Turno;
import br.sistema.model.repository.interfaces.TurnoRepository;
import br.sistema.model.service.interfaces.TurnoService;

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
                throw new RuntimeException("Já existe um turno cadastrado como " + nomeStr);
            }

            turnoRepository.save(turno);

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação salvar: " + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Turno findById(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("ID do turno obrigatório.");
            }

            Turno turno = turnoRepository.findById(id);

            if (turno == null) {
                throw new RuntimeException("Turno não encontrado.");
            }

            return turno;

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação findById: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar turno pelo ID.", e);
        }
    }

    @Override
    public void update(Turno turno) {
        try {
            validarTurno(turno);

            if (turno.getId() == null) {
                throw new IllegalArgumentException("ID do turno obrigatório para atualização.");
            }

            Turno turnoExiste = turnoRepository.findById(turno.getId());

            if (turnoExiste == null) {
                throw new RuntimeException("Turno não encontrado para atualização.");
            }

            Turno turnoComMesmoNome = turnoRepository.findByName(turno.getNomeTurno().name());

            if (turnoComMesmoNome != null && !turnoComMesmoNome.getId().equals(turno.getId())) {
                throw new RuntimeException("Já existe outro turno cadastrado com este nome.");
            }

            turnoExiste.setNomeTurno(turno.getNomeTurno());
            turnoExiste.setInicioTurno(turno.getInicioTurno());
            turnoExiste.setFimTurno(turno.getFimTurno());
            turnoExiste.setTempoAula(turno.getTempoAula());
            turnoExiste.setAulasTurno(turno.getAulasTurno());

            turnoRepository.update(turnoExiste);

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação update: " + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(Turno turno) {
        try {
            if (turno == null) {
                throw new IllegalArgumentException("Turno inválido para exclusão.");
            }

            Turno turnoNoBanco = null;

            if (turno.getId() != null) {
                turnoNoBanco = turnoRepository.findById(turno.getId());
            }

            if (turnoNoBanco == null && turno.getNomeTurno() != null) {
                turnoNoBanco = turnoRepository.findByName(turno.getNomeTurno().name());
            }

            if (turnoNoBanco == null) {
                throw new RuntimeException("Turno não encontrado para exclusão.");
            }

            turnoRepository.delete(turnoNoBanco);

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação delete: " + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<Turno> findAll() {
        try {
            return turnoRepository.findAll();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar a lista de turnos.", e);
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
            return null;
        }
    }

    private void validarTurno(Turno turno) {
        if (turno == null) {
            throw new IllegalArgumentException("Turno inválido.");
        }

        if (turno.getNomeTurno() == null) {
            throw new IllegalArgumentException("Nome inválido.");
        }

        if (turno.getInicioTurno() == null || turno.getFimTurno() == null) {
            throw new IllegalArgumentException("Horário vazio.");
        }

        if (turno.getInicioTurno().isAfter(turno.getFimTurno())) {
            throw new IllegalArgumentException("Início após o fim.");
        }

        if (turno.getTempoAula() == null || turno.getTempoAula() <= 0) {
            throw new IllegalArgumentException("Tempo de aula inválido.");
        }

        if (turno.getAulasTurno() == null || turno.getAulasTurno() <= 0) {
            throw new IllegalArgumentException("Qtd aulas inválida.");
        }
    }
}