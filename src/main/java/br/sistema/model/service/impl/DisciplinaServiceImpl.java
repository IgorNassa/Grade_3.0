package br.sistema.model.service.impl;

import br.sistema.model.entity.Disciplina;
import br.sistema.model.repository.impl.DisciplinaRepositoryImpl;

import java.util.List;

public class DisciplinaServiceImpl {

    private final DisciplinaRepositoryImpl disciplinaRepositoryImpl;

    public DisciplinaServiceImpl(DisciplinaRepositoryImpl disciplinaRepositoryImpl){
        this.disciplinaRepositoryImpl = disciplinaRepositoryImpl;
    }

    public void save(Disciplina disciplina) {
        try {
            if (disciplina.getNome() == null || disciplina.getNome().trim().isEmpty()){
                throw new IllegalArgumentException("O nome da disciplina não pode ser vazio.");
            }

            Disciplina disciplinaExiste = disciplinaRepositoryImpl.findByName(disciplina.getNome());

            if (disciplinaExiste != null) {
                throw new RuntimeException("Já existe uma disciplina cadastrada com este nome: " + disciplina.getNome());
            }

            disciplinaRepositoryImpl.save(disciplina);

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação salvar: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar a disciplina.", e);
        }
    }

    public void update(Disciplina disciplina) {
        try {
            if (disciplina.getNome() == null || disciplina.getNome().trim().isEmpty()){
                throw new IllegalArgumentException("Nome inválido para atualização.");
            }

            Disciplina disciplinaExiste = disciplinaRepositoryImpl.findByName(disciplina.getNome());

            if (disciplinaExiste == null) {
                throw new RuntimeException("Disciplina não encontrada na base de dados para atualizar.");
            }

            disciplinaRepositoryImpl.update(disciplina);

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação update: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar a disciplina.", e);
        }
    }

    public void delete(Disciplina disciplina) {
        try {
            Disciplina disciplinaExiste = disciplinaRepositoryImpl.findByName(disciplina.getNome());

            if (disciplinaExiste == null) {
                throw new RuntimeException("Disciplina não encontrada na base de dados para exclusão.");
            }

            disciplinaRepositoryImpl.delete(disciplina);

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação delete: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir a disciplina.", e);
        }
    }

    public List<Disciplina> findAll() {
        try {
            List<Disciplina> disciplinas = disciplinaRepositoryImpl.findAll();

            if (disciplinas == null || disciplinas.isEmpty()) {
                System.out.println("Nenhuma disciplina cadastrada no momento.");
            }

            return disciplinas;

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação findAll: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar a lista de disciplinas.", e);
        }
    }

    // ==========================================
    // NOVO MÉTODO: FIND BY NAME
    // ==========================================
    public Disciplina findByName(String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                throw new IllegalArgumentException("O nome para busca não pode ser vazio.");
            }

            return disciplinaRepositoryImpl.findByName(nome);

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação findByName: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar a disciplina pelo nome.", e);
        }
    }
}