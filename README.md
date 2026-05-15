<div align="center">
  
# 🏫 SGDG - Sistema de Geração de Grade

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

> Um sistema robusto de gestão escolar desenhado para a geração automatizada de horários de aulas. Resolve de forma inteligente os conflitos complexos de horários entre professores e turmas, utilizando um poderoso algoritmo de backtracking.

</div>

<br>

## 📋 Gestão do Projeto (Board Notion)

Todo o planeamento, divisão do esquadrão, sprints e acompanhamento de tarefas (CRUDs, Motor Back-end, UI/UX) estão centralizados no nosso board oficial.

🔗 **[Aceder ao Board do SGDG no Notion](https://www.notion.so/SGDG-2-0-com-front-356ecf7c32fd808fb380e6ae5c040e8a?source=copy_link)**

---

## ✨ Principais Funcionalidades

- 🗂️ **Gestão de Entidades (CRUD):** Controlo total e seguro de Professores, Disciplinas, Turnos e Turmas, com validações rigorosas.
- 🧠 **Motor de Backtracking:** Algoritmo recursivo que aloca as aulas de forma inteligente. Garante que não existem conflitos de horário (ex: um professor não pode dar aulas a duas turmas em simultâneo).
- 🎨 **Interface Intuitiva (UI):** Dashboard moderno construído em Java Swing utilizando `CardLayout`, mantendo as regras de negócio totalmente isoladas da camada visual.
- ⚡ **Processamento Assíncrono:** Utilização de `SwingWorker` no Controller para assegurar que a interface gráfica não bloqueia durante o cálculo pesado das combinações de horários.
- 🚀 **Persistência Otimizada (Batch Inserts):** Gravação de toda a grade gerada numa única transação via JPA/Hibernate, evitando estrangulamentos (I/O) na base de dados.

---

## 🛠️ Tecnologias e Arquitetura

O sistema foi desenhado a pensar na **Clean Architecture**, alta coesão e baixo acoplamento (princípios SOLID).

### Stack Tecnológica
* **Linguagem:** Java (JDK 25)
* **Interface Gráfica:** Java Swing (MVC)
* **Base de Dados:** PostgreSQL (porta `5432`)
* **ORM:** Jakarta Persistence API (JPA) + Hibernate
* **Migrações:** Flyway Database Migrations
* **Build Tool:** Apache Maven

### Divisão de Domínios
1. **Motor Back-end:** Lógica central matemática isolada na interface `GeradorGradeEngine`.
2. **Ponte e Integração:** Uso de **DTOs (Records)** imutáveis para a transição segura de dados entre as views e a base de dados através de Controllers.
3. **Camada Visual:** Formulários Swing interativos com tratamento global de erros capturados em `JOptionPane`.
4. **Camada de Persistência:** Repositories otimizados com queries JPQL e validações ACID estritas.

---

## 🚀 Guia de Execução

### Passo 0: Pré-requisitos
Antes de iniciar, certifique-se de que tem instalado na sua máquina:
* Java JDK 25 ou superior
* PostgreSQL a correr localmente na porta `5432`
* Maven instalado

### Passo 1: Clonar o Repositório
Abra o seu terminal e faça o clone do projeto:
```bash
git clone [https://github.com/seu-usuario/sgd-grade.git](https://github.com/seu-usuario/sgd-grade.git)
cd sgd-grade
Passo 2: Configuração da Base de Dados
Certifique-se de que tem uma base de dados vazia criada no PostgreSQL com o nome grade. O sistema utiliza as seguintes credenciais padrão (configuradas via persistence.xml ou variáveis de ambiente):


URL: jdbc:postgresql://localhost:5432/grade 


User: postgres 


Password: 1234 

Passo 3: Iniciar a Aplicação
Ao iniciar, o Flyway irá detetar e executar as migrações automaticamente (criação das tabelas e triggers). Para compilar e executar o sistema, utilize o Maven na raiz do projeto:

mvn clean install
mvn exec:java -Dexec.mainClass="br.sistema.Main"
