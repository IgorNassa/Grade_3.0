CREATE TABLE IF NOT EXISTS turma (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(20) NOT NULL,
    e_medio BOOLEAN NOT NULL
    );

CREATE TABLE IF NOT EXISTS turma_disciplina (
    turma_disciplina_id SERIAL PRIMARY KEY,
    turma_id INT NOT NULL,
    disciplina_id INT NOT NULL,

    CONSTRAINT fk_turma
    FOREIGN KEY (turma_id) REFERENCES turma (id) ON DELETE CASCADE,

    CONSTRAINT fk_disciplina
    FOREIGN KEY (disciplina_id) REFERENCES disciplina (id) ON DELETE CASCADE
    );