package br.sistema.model.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "turno")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nome_turno", nullable = false)
    private TipoTurno nomeTurno;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime inicioTurno;

    @Column(name = "hora_fim", nullable = false)
    private LocalTime fimTurno;

    @Column(name = "tempo_aula", nullable = false)
    private Integer tempoAula;

    @Column(name = "quantidade_aulas", nullable = false)
    private Integer aulasTurno;

    @Column(name = "aulas_antes_intervalo", nullable = false)
    private Integer aulasAntesIntervalo;

    public Turno() {
    }

    public Turno(TipoTurno nomeTurno, LocalTime inicioTurno, LocalTime fimTurno,
                 Integer tempoAula, Integer aulasTurno, Integer aulasAntesIntervalo) {
        this.nomeTurno = nomeTurno;
        this.inicioTurno = inicioTurno;
        this.fimTurno = fimTurno;
        this.tempoAula = tempoAula;
        this.aulasTurno = aulasTurno;
        this.aulasAntesIntervalo = aulasAntesIntervalo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoTurno getNomeTurno() {
        return nomeTurno;
    }

    public void setNomeTurno(TipoTurno nomeTurno) {
        this.nomeTurno = nomeTurno;
    }

    public LocalTime getInicioTurno() {
        return inicioTurno;
    }

    public void setInicioTurno(LocalTime inicioTurno) {
        this.inicioTurno = inicioTurno;
    }

    public LocalTime getFimTurno() {
        return fimTurno;
    }

    public void setFimTurno(LocalTime fimTurno) {
        this.fimTurno = fimTurno;
    }

    public Integer getTempoAula() {
        return tempoAula;
    }

    public void setTempoAula(Integer tempoAula) {
        this.tempoAula = tempoAula;
    }

    public Integer getAulasTurno() {
        return aulasTurno;
    }

    public void setAulasTurno(Integer aulasTurno) {
        this.aulasTurno = aulasTurno;
    }

    public Integer getAulasAntesIntervalo() {
        return aulasAntesIntervalo;
    }

    public void setAulasAntesIntervalo(Integer aulasAntesIntervalo) {
        this.aulasAntesIntervalo = aulasAntesIntervalo;
    }
}