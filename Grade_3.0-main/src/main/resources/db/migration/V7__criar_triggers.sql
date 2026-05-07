-- 1. TRIGGER PARA LIMPAR ESPAÇOS E CAPITALIZAR O NOME
CREATE OR REPLACE FUNCTION fn_limpar_texto() RETURNS TRIGGER AS $$
BEGIN NEW.nome := TRIM(REGEXP_REPLACE(INITCAP(NEW.nome), '\s+', ' ', 'g'));
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_limpa_nome_professor ON professor;
CREATE TRIGGER trg_limpa_nome_professor BEFORE INSERT OR UPDATE ON professor
    FOR EACH ROW EXECUTE FUNCTION fn_limpar_texto();

DROP TRIGGER IF EXISTS trg_limpa_nome_disciplina ON disciplina;
CREATE TRIGGER trg_limpa_nome_disciplina BEFORE INSERT OR UPDATE ON disciplina
    FOR EACH ROW EXECUTE FUNCTION fn_limpar_texto();

-- 2. TRIGGER PARA ATUALIZAR TIMESTAMP NA TURMA
ALTER TABLE turma ADD COLUMN IF NOT EXISTS data_ultima_modificacao TIMESTAMP;

CREATE OR REPLACE FUNCTION fn_update_timestamp() RETURNS TRIGGER AS $$
BEGIN
    NEW.data_ultima_modificacao = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_update_timestamp_turma ON turma;
CREATE TRIGGER trg_update_timestamp_turma BEFORE UPDATE ON turma
    FOR EACH ROW EXECUTE FUNCTION fn_update_timestamp();

-- 3. TRIGGER PARA AUDITORIA (LOG) DE PROFESSORES
CREATE TABLE IF NOT EXISTS log_professores (
    id_log SERIAL PRIMARY KEY,
    id_professor INT,
    nome_antigo VARCHAR(255),
    acao VARCHAR(10),
    usuario_db TEXT DEFAULT current_user,
    data_mudanca TIMESTAMP DEFAULT NOW()
    );

CREATE OR REPLACE FUNCTION fn_auditoria_professor() RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO log_professores(id_professor, nome_antigo, acao) VALUES (OLD.id, OLD.nome, 'DELETE');
RETURN OLD;
ELSE
        INSERT INTO log_professores(id_professor, nome_antigo, acao) VALUES (OLD.id, OLD.nome, 'UPDATE');
RETURN NEW;
END IF;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_log_professor ON professor;
CREATE TRIGGER trg_log_professor AFTER UPDATE OR DELETE ON professor
    FOR EACH ROW EXECUTE FUNCTION fn_auditoria_professor();