package br.sistema.model.service.impl;

import br.sistema.controller.dtos.ProfessorDTO;
import br.sistema.model.entity.Professor;
import br.sistema.util.ProfessorMapper;
import br.sistema.model.repository.impl.ProfessorRepositoryImpl;
import br.sistema.model.service.interfaces.ProfessorService;

import java.util.List;

public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepositoryImpl professorRepositoryImpl;

    public ProfessorServiceImpl(ProfessorRepositoryImpl professorRepositoryImpl) {
        this.professorRepositoryImpl = professorRepositoryImpl;
    }

    @Override
    public void save(ProfessorDTO professorDTO) {
        try {
            if (professorDTO.nome() == null || professorDTO.nome().trim().isEmpty()) {
                throw new IllegalArgumentException("Nome do professor é obrigatório.");
            }

            Professor existe = professorRepositoryImpl.findByName(professorDTO.nome().trim());
            if (existe != null) {
                throw new RuntimeException("Professor já cadastrado!");
            }

            Professor entity = ProfessorMapper.INSTANCE.toEntity(professorDTO);
            professorRepositoryImpl.save(entity);
        } catch (Exception e) {
            System.err.println("[ERRO]" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(ProfessorDTO professorDTO) {
        try {
            Professor noBanco = professorRepositoryImpl.findByName(professorDTO.nome());
            if (noBanco == null) {
                throw new RuntimeException("Professor não encontrado para ID: " + professorDTO.id());
            }

            Professor entity = ProfessorMapper.INSTANCE.toEntity(professorDTO);
            professorRepositoryImpl.update(entity);
        } catch (Exception e) {
            System.err.println("Falha no update:" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(ProfessorDTO professorDTO) {
        try {
            Professor noBanco = professorRepositoryImpl.findByName(professorDTO.nome());
            if (noBanco == null) {
                throw new RuntimeException("Professor não encontrado para exclusão.");
            }
            professorRepositoryImpl.delete(noBanco);
        } catch (Exception e) {
            System.err.println("Falha no delete:" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<ProfessorDTO> findAll() {
        return professorRepositoryImpl.findAll().stream()
                .map(ProfessorMapper.INSTANCE::toDTO)
                .toList();
    }

    @Override
    public ProfessorDTO findByName(String nome) {
        Professor p = professorRepositoryImpl.findByName(nome);
        return (p != null) ? ProfessorMapper.INSTANCE.toDTO(p) : null;
    }
}