package br.sistema.model.service.impl;

import br.sistema.model.entity.Disciplina;
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
                throw new RuntimeException("Já existe uma disciplina cadastrada com este nome: " + disciplina.getNome());
            }

            disciplinaRepository.save(disciplina);

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação salvar: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar a disciplina.", e);
        }
    }

    @Override
    public void update(Disciplina disciplina) {
        try {
            validarDisciplina(disciplina);

            if (disciplina.getId() == null) {
                throw new IllegalArgumentException("O ID da disciplina é obrigatório para atualização.");
            }

            Disciplina disciplinaExiste = disciplinaRepository.findById(disciplina.getId());

            if (disciplinaExiste == null) {
                throw new RuntimeException("Disciplina não encontrada na base de dados para atualizar.");
            }

            Disciplina disciplinaComMesmoNome = disciplinaRepository.findByName(disciplina.getNome());

            if (disciplinaComMesmoNome != null && !disciplinaComMesmoNome.getId().equals(disciplina.getId())) {
                throw new RuntimeException("Já existe outra disciplina cadastrada com este nome: " + disciplina.getNome());
            }

            disciplinaExiste.setNome(disciplina.getNome());

            disciplinaRepository.update(disciplinaExiste);

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação update: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar a disciplina.", e);
        }
    }

    @Override
    public void delete(Disciplina disciplina) {
        try {
            if (disciplina == null) {
                throw new IllegalArgumentException("Disciplina inválida para exclusão.");
            }

            Disciplina disciplinaExiste = null;

            if (disciplina.getId() != null) {
                disciplinaExiste = disciplinaRepository.findById(disciplina.getId());
            }

            if (disciplinaExiste == null && disciplina.getNome() != null && !disciplina.getNome().trim().isEmpty()) {
                disciplinaExiste = disciplinaRepository.findByName(disciplina.getNome());
            }

            if (disciplinaExiste == null) {
                throw new RuntimeException("Disciplina não encontrada na base de dados para exclusão.");
            }

            disciplinaRepository.delete(disciplinaExiste);

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação delete: " + e.getMessage());
            throw new RuntimeException("Erro ao excluir a disciplina.", e);
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
            System.err.println("[ERRO] Falha na operação findAll: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar a lista de disciplinas.", e);
        }
    }

    @Override
    public Disciplina findById(Long id){
        try {
            if(id == null){
                throw new IllegalArgumentException("O ID da disciplina é obrigatorio para busca.");
            }
            Disciplina disciplina = disciplinaRepository.findById(id);

            if (disciplina == null) {
                throw new RuntimeException("Disciplina nao encontrada.");
            }
            return  disciplina;
        }
        catch (Exception e){
            System.err.println("[ERRO] Falha na operação findById: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar a disciplina pelo ID.", e);
        }
    }

    @Override
    public Disciplina findByName(String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                throw new IllegalArgumentException("O nome para busca não pode ser vazio.");
            }

            return disciplinaRepository.findByName(nome);

        } catch (Exception e) {
            System.err.println("[ERRO] Falha na operação findByName: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar a disciplina pelo nome.", e);
        }
    }

    private void validarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina inválida.");
        }

        if (disciplina.getNome() == null || disciplina.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da disciplina não pode ser vazio.");
        }

        disciplina.setNome(disciplina.getNome().trim());
    }
}

