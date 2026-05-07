CREATE TABLE IF NOT EXISTS aula (
    id SERIAL PRIMARY KEY,
    turma_id INT NOT NULL,
    professor_id INT,
    disciplina_id INT,
    dia_da_semana VARCHAR(20) NOT NULL,
    slot_horario INT NOT NULL,

    CONSTRAINT fk_aula_turma
    FOREIGN KEY (turma_id)
    REFERENCES turma (id) ON DELETE CASCADE,

    CONSTRAINT fk_aula_professor
    FOREIGN KEY (professor_id)
    REFERENCES professor (id) ON DELETE SET NULL,

    CONSTRAINT fk_aula_disciplina
    FOREIGN KEY (disciplina_id)
    REFERENCES disciplina (id) ON DELETE SET NULL
);