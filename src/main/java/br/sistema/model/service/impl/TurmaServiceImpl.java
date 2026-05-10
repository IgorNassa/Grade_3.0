package br.sistema.model.service.impl;

import br.sistema.controller.dtos.TurmaDTO;
import br.sistema.model.entity.Turma;
import br.sistema.util.TurmaMapper;
import br.sistema.model.repository.impl.TurmaRepositoryImpl;
import br.sistema.model.service.interfaces.TurmaService;

import java.util.List;

public class TurmaServiceImpl implements TurmaService {

    private final TurmaRepositoryImpl turmaRepositoryImpl;

    public TurmaServiceImpl(TurmaRepositoryImpl turmaRepositoryImpl) {
        this.turmaRepositoryImpl = turmaRepositoryImpl;
    }

    @Override
    public void save(TurmaDTO turmaDTO) {
        try {
            if (turmaDTO.nome() == null || turmaDTO.nome().trim().isEmpty()) {
                throw new IllegalArgumentException("Nome da turma é obrigatório.");
            }

            Turma existe = turmaRepositoryImpl.findByName(turmaDTO.nome().trim());
            if (existe != null) {
                throw new RuntimeException("Já existe uma turma com o nome: " + turmaDTO.nome());
            }

            Turma turmaEntity = TurmaMapper.INSTANCE.toEntity(turmaDTO);
            turmaRepositoryImpl.save(turmaEntity);
        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(TurmaDTO turmaDTO) {
        try {

            Turma existente = turmaRepositoryImpl.findByName(turmaDTO.nome());
            if (existente == null) {
                throw new RuntimeException("Turma não encontrada para atualização: " + turmaDTO.nome());
            }

            Turma turmaEntity = TurmaMapper.INSTANCE.toEntity(turmaDTO);

            turmaRepositoryImpl.update(turmaEntity);

        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Falha no update: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(TurmaDTO turmaDTO) {
        try {

            Turma existente = turmaRepositoryImpl.findByName(turmaDTO.nome());
            if (existente == null) {
                throw new RuntimeException("Turma não encontrada para exclusão.");
            }

            turmaRepositoryImpl.delete(existente);
        } catch (Exception e) {
            System.err.println("[ERRO SERVICE] Falha no delete: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<TurmaDTO> findAll() {
        return turmaRepositoryImpl.findAll().stream()
                .map(TurmaMapper.INSTANCE::toDTO)
                .toList();
    }

    @Override
    public TurmaDTO findByName(String nome) {
        Turma turma = turmaRepositoryImpl.findByName(nome.trim());
        return (turma != null) ? TurmaMapper.INSTANCE.toDTO(turma) : null;
    }
}