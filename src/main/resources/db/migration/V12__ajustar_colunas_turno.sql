DO $$
BEGIN
    -- Renomeia 'id' para 'turno_id' se existir
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'turno' AND column_name = 'id') THEN
        ALTER TABLE turno RENAME COLUMN id TO turno_id;
    END IF;

    -- Ajusta outros nomes de colunas que podem estar diferentes do esperado pelo JPA
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'turno' AND column_name = 'nome') THEN
        ALTER TABLE turno RENAME COLUMN nome TO nome_turno;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'turno' AND column_name = 'inicio') THEN
        ALTER TABLE turno RENAME COLUMN inicio TO hora_inicio;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'turno' AND column_name = 'fim') THEN
        ALTER TABLE turno RENAME COLUMN fim TO hora_fim;
    END IF;
END $$;
