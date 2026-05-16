package br.sistema.model.entity;

import jakarta.persistence.*;

import java.time.DayOfWeek;

@Entity
@Table(name = "professor_disponibilidade")
public class ProfessorDisponibilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_da_semana", nullable = false)
    private DayOfWeek diaDaSemana;

    @Column(name = "slot_horario", nullable = false)
    private Integer slotHorario;

    @Column(name = "disponivel", nullable = false)
    private boolean disponivel = true;

    public ProfessorDisponibilidade() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public DayOfWeek getDiaDaSemana() {
        return diaDaSemana;
    }

    public void setDiaDaSemana(DayOfWeek diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    public Integer getSlotHorario() {
        return slotHorario;
    }

    public void setSlotHorario(Integer slotHorario) {
        this.slotHorario = slotHorario;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}
