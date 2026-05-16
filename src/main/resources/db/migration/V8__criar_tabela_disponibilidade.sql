CREATE TABLE IF NOT EXISTS professor_disponibilidade (
    id SERIAL PRIMARY KEY,
    professor_id INT NOT NULL,
    dia_da_semana VARCHAR(20) NOT NULL,
    slot_horario INT NOT NULL,
    disponivel BOOLEAN DEFAULT TRUE NOT NULL,

    CONSTRAINT fk_disponibilidade_professor
    FOREIGN KEY (professor_id)
    REFERENCES professor (id) ON DELETE CASCADE
);
