package br.sistema.model.service.impl;

import br.sistema.model.entity.Turno;
import br.sistema.model.repository.impl.TurnoRepositoryImpl;
import java.util.List;

public class TurnoServiceImpl {

    private final TurnoRepositoryImpl turnoRepositoryImpl;

    public TurnoServiceImpl(TurnoRepositoryImpl turnoRepositoryImpl) {
        this.turnoRepositoryImpl = turnoRepositoryImpl;
    }

    public void save(Turno turno) {
        try {
            validarTurno(turno);

            String nomeStr = turno.getNomeTurno().toString();
            Turno existe = turnoRepositoryImpl.findByName(nomeStr);
            if (existe != null) {
                throw new RuntimeException("Já existe um turno cadastrado como " + nomeStr);
            }

            turnoRepositoryImpl.save(turno);
        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação salvar: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void update(Turno turno) {
        try {
            validarTurno(turno);
            turnoRepositoryImpl.update(turno);
        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação update: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(Turno turno) {
        try {
            Turno existe = turnoRepositoryImpl.findByName(turno.getNomeTurno().toString());
            if (existe == null) {
                throw new RuntimeException("Turno não encontrado para exclusão.");
            }
            turnoRepositoryImpl.delete(existe);
        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação delete: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Turno> findAll() {
        return turnoRepositoryImpl.findAll();
    }

    public Turno findByName(String nome) {
        return turnoRepositoryImpl.findByName(nome.toUpperCase().trim());
    }

    private void validarTurno(Turno t) {
        if (t.getNomeTurno() == null) throw new IllegalArgumentException("Nome inválido.");
        if (t.getInicioTurno() == null || t.getFimTurno() == null) throw new IllegalArgumentException("Horário vazio.");
        if (t.getInicioTurno().isAfter(t.getFimTurno())) throw new IllegalArgumentException("Início após o fim.");
        if (t.getTempoAula() == null || t.getTempoAula() <= 0) throw new IllegalArgumentException("Tempo de aula inválido.");
        if (t.getAulasTurno() == null || t.getAulasTurno() <= 0) throw new IllegalArgumentException("Qtd aulas inválida.");
    }
}