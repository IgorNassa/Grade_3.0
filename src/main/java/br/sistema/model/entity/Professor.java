package br.sistema.model.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "professor")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // O banco usa SERIAL PRIMARY KEY

    @Column(name = "nome", nullable = false)
    private String nome;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "professor_disciplina", // Nome da tabela intermédia definida no V3
            joinColumns = @JoinColumn(name = "professor_id"), // FK que aponta para Professor
            inverseJoinColumns = @JoinColumn(name = "disciplina_id") // FK que aponta para Disciplina
    )
    private List<Disciplina> disciplinas;

    public Professor() {
    }

    public Professor(Long id, String nome, List<Disciplina> disciplinas) {
        this.id = id;
        this.nome = nome;
        this.disciplinas = disciplinas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
}