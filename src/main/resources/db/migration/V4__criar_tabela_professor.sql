CREATE TABLE IF NOT EXISTS professor (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS professor_disciplina (
    professor_id INT NOT NULL,
    disciplina_id INT NOT NULL,
    PRIMARY KEY (professor_id, disciplina_id),

    CONSTRAINT fk_professor
    FOREIGN KEY (professor_id)
    REFERENCES professor (id) ON DELETE CASCADE,

    CONSTRAINT fk_disciplina
    FOREIGN KEY (disciplina_id)
    REFERENCES disciplina (id) ON DELETE CASCADE
    );