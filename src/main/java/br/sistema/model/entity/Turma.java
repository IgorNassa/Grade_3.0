package br.sistema.model.entity;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "turma")
public class Turma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (length = 20, nullable = false)
    private String nome;

    @Column(name = "e_medio", nullable = false) // Mapeamento explícito para o banco
    private boolean eMedio;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "turma_disciplina",
            joinColumns = @JoinColumn(name = "turma_id"),
            inverseJoinColumns = @JoinColumn(name = "disciplina_id")
    )
    private List<Disciplina> disciplinas;

    public Turma(Long id, String nome, Boolean eMedio) {
        this.id = id;
        this.nome = nome;
        this.eMedio = eMedio;
    }

    public Turma(){}

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

    public boolean iseMedio() {
        return eMedio;
    }

    public void seteMedio(boolean eMedio) {
        this.eMedio = eMedio;
    }
    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
}
