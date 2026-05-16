-- Alterar o comportamento de deleção da chave estrangeira de professor na tabela aula
-- De SET NULL para CASCADE, pois a coluna foi definida como NOT NULL em V9

ALTER TABLE aula DROP CONSTRAINT IF EXISTS fk_aula_professor;

ALTER TABLE aula ADD CONSTRAINT fk_aula_professor 
FOREIGN KEY (professor_id) REFERENCES professor (id) 
ON DELETE CASCADE;
