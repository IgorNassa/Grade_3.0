package br.sistema.model.service.impl;

import br.sistema.controller.dtos.DisciplinaDTO;
import br.sistema.model.entity.Disciplina;
import br.sistema.util.DisciplinaMapper;
import br.sistema.model.repository.impl.DisciplinaRepositoryImpl;
import br.sistema.model.service.interfaces.DisciplinaService;

import java.util.List;

public class DisciplinaServiceImpl implements DisciplinaService {

    private final DisciplinaRepositoryImpl disciplinaRepositoryImpl;

    public DisciplinaServiceImpl(DisciplinaRepositoryImpl disciplinaRepositoryImpl) {
        this.disciplinaRepositoryImpl = disciplinaRepositoryImpl;
    }

    @Override
    public void save(DisciplinaDTO disciplinaDTO) {
        try {
            if (disciplinaDTO.nome() == null || disciplinaDTO.nome().trim().isEmpty()) {
                throw new IllegalArgumentException("Nome da disciplina é obrigatório.");
            }

            Disciplina existe = disciplinaRepositoryImpl.findByName(disciplinaDTO.nome().trim());
            if (existe != null) {
                throw new RuntimeException("Disciplina já cadastrada!");
            }

            Disciplina entity = DisciplinaMapper.INSTANCE.toEntity(disciplinaDTO);
            disciplinaRepositoryImpl.save(entity);
        } catch (Exception e) {
            System.err.println("[ERRO] " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(DisciplinaDTO disciplinaDTO) {
        try {
            Disciplina noBanco = disciplinaRepositoryImpl.findByName(disciplinaDTO.nome());
            if (noBanco == null) {
                throw new RuntimeException("Disciplina não encontrada para ID: " + disciplinaDTO.id());
            }

            Disciplina entity = DisciplinaMapper.INSTANCE.toEntity(disciplinaDTO);
            disciplinaRepositoryImpl.update(entity);
        } catch (Exception e) {
            System.err.println("[ERRO] Falha no update: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(DisciplinaDTO disciplinaDTO) {
        try {
            Disciplina noBanco = disciplinaRepositoryImpl.findByName(disciplinaDTO.nome());
            if (noBanco == null) {
                throw new RuntimeException("Disciplina não encontrada para exclusão.");
            }
            disciplinaRepositoryImpl.delete(noBanco);
        } catch (Exception e) {
            System.err.println("[ERRO] Falha no delete: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<DisciplinaDTO> findAll() {
        return disciplinaRepositoryImpl.findAll().stream()
                .map(DisciplinaMapper.INSTANCE::toDTO)
                .toList();
    }

    @Override
    public DisciplinaDTO findByName(String nome) {
        Disciplina d = disciplinaRepositoryImpl.findByName(nome);
        return (d != null) ? DisciplinaMapper.INSTANCE.toDTO(d) : null;
    }
}