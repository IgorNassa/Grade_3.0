package br.sistema.model.service.impl;

import br.sistema.controller.dtos.TurnoDTO;
import br.sistema.model.entity.Turno;
import br.sistema.util.TurnoMapper;
import br.sistema.model.repository.impl.TurnoRepositoryImpl;
import br.sistema.model.service.interfaces.TurnoService;

import java.util.List;

public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepositoryImpl turnoRepositoryImpl;

    public TurnoServiceImpl(TurnoRepositoryImpl turnoRepositoryImpl) {
        this.turnoRepositoryImpl = turnoRepositoryImpl;
    }

    @Override
    public void save(TurnoDTO turnoDTO) {
        try {
            validarTurnoDTO(turnoDTO);

            String nomeStr = turnoDTO.nomeTurno().toString();
            Turno existe = turnoRepositoryImpl.findByName(nomeStr);

            if (existe != null) {
                throw new RuntimeException("Já existe um turno cadastrado como " + nomeStr);
            }

            Turno entity = TurnoMapper.INSTANCE.toEntity(turnoDTO);
            turnoRepositoryImpl.save(entity);
        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(TurnoDTO turnoDTO) {
        try {
            validarTurnoDTO(turnoDTO);

            Turno entity = TurnoMapper.INSTANCE.toEntity(turnoDTO);
            turnoRepositoryImpl.update(entity);
        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Falha no update: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(TurnoDTO turnoDTO) {
        try {
            Turno existe = turnoRepositoryImpl.findByName(turnoDTO.nomeTurno().toString());
            if (existe == null) {
                throw new RuntimeException("Turno não encontrado para exclusão.");
            }
            turnoRepositoryImpl.delete(existe);
        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Falha no delete: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<TurnoDTO> findAll() {
        return turnoRepositoryImpl.findAll().stream()
                .map(TurnoMapper.INSTANCE::toDTO)
                .toList();
    }

    @Override
    public TurnoDTO findByName(String nome) {
        Turno t = turnoRepositoryImpl.findByName(nome.toUpperCase().trim());
        return (t != null) ? TurnoMapper.INSTANCE.toDTO(t) : null;
    }

    private void validarTurnoDTO(TurnoDTO t) {
        if (t.nomeTurno() == null) throw new IllegalArgumentException("Nome do turno inválido.");
        if (t.inicioTurno() == null || t.fimTurno() == null) throw new IllegalArgumentException("Horários não podem ser vazios.");
        if (t.inicioTurno().isAfter(t.fimTurno())) throw new IllegalArgumentException("Início não pode ser após o fim.");
        if (t.tempoAula() == null || t.tempoAula() <= 0) throw new IllegalArgumentException("Tempo de aula inválido.");
        if (t.aulasTurno() == null || t.aulasTurno() <= 0) throw new IllegalArgumentException("Quantidade de aulas inválida.");
    }
}