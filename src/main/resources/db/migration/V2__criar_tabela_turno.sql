CREATE TABLE IF NOT EXISTS turno (
    turno_id SERIAL PRIMARY KEY,
    nome_turno VARCHAR(20) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    tempo_aula INTEGER NOT NULL,
    quantidade_aulas INTEGER NOT NULL,
    aulas_antes_intervalo INTEGER NOT NULL
    );