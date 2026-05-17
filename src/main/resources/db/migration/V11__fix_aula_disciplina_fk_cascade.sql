-- Alterar o comportamento de deleção da chave estrangeira de disciplina na tabela aula
-- De SET NULL para CASCADE, pois a coluna foi definida como NOT NULL em V9

ALTER TABLE aula DROP CONSTRAINT IF EXISTS fk_aula_disciplina;

ALTER TABLE aula ADD CONSTRAINT fk_aula_disciplina 
FOREIGN KEY (disciplina_id) REFERENCES disciplina (id) 
ON DELETE CASCADE;
