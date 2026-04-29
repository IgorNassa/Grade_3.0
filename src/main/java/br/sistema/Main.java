package br.sistema;

import br.sistema.config.JpaUtil;
import br.sistema.model.entity.repository.interfaces.*;
import br.sistema.model.entity.service.*;
import br.sistema.view.MenuPrincipal;
import jakarta.persistence.EntityManager;
import org.flywaydb.core.Flyway;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" Inicializando o Sistema (SGDG)...");
        System.out.println("========================================");

        // 1. Executar Migrations (Flyway)
        System.out.println("Verificando e executando migrações do banco de dados (Flyway)...");
        try {
            Flyway flyway = Flyway.configure()
                    .dataSource("jdbc:postgresql://localhost:5432/grade", "postgres", "1234")
                    .baselineOnMigrate(true)  // Diz ao Flyway para criar o histórico se não existir
                    .baselineVersion("0")     // Marca o ponto de partida como versão 0
                    .load();
            flyway.repair();
            flyway.migrate();
            System.out.println("Migrações concluídas com sucesso!");
        } catch (Exception e) {
            System.err.println("[ERRO CRÍTICO] Falha ao rodar o Flyway: " + e.getMessage());
            System.err.println("Verifique se o PostgreSQL está ligado e se a senha está correta.");
            return;
        }

        // 2. Iniciar JPA (Conexão e EntityManager)
        System.out.println("Conectando ao banco de dados");
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
        } catch (Exception e) {
            System.err.println("[ERRO] Falha ao conectar o JPA: " + e.getMessage());
            return;
        }

        try {
            // 3. Instanciar todos os Repositórios
            DisciplinaRepository disciplinaRepo = new DisciplinaRepository(em);
            ProfessorRepository professorRepo = new ProfessorRepository(em);
            TurnoRepository turnoRepo = new TurnoRepository(em);
            TurmaRepository turmaRepo = new TurmaRepository(em);
            AulaRepository aulaRepo = new AulaRepository(em);

            // 4. Instanciar todos os Serviços (Injetando os Repositórios criados acima)
            DisciplinaService disciplinaService = new DisciplinaService(disciplinaRepo);
            ProfessorService professorService = new ProfessorService(professorRepo);
            TurnoService turnoService = new TurnoService(turnoRepo);
            TurmaService turmaService = new TurmaService(turmaRepo);

            // O AulaService recebe o Repo e também o EntityManager (para a transação manual da grade)
            AulaService aulaService = new AulaService(aulaRepo, em);

            // 5. Instanciar a View e arrancar o Sistema
            System.out.println("Iniciando a interface do usuário...");
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");

            MenuPrincipal menu = new MenuPrincipal(
                    disciplinaService,
                    professorService,
                    turnoService,
                    turmaService,
                    aulaService
            );

            // O programa entra em Loop aqui dentro do iniciar() até o utilizador digitar 0
            menu.iniciar();

        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado na aplicação: " + e.getMessage());
        } finally {
            // 6. Encerrar conexões com segurança ao fechar a aplicação
            if (em != null && em.isOpen()) {
                em.close();
            }
            System.out.println("\nBase de dados desconectada. Sistema encerrado corretamente.");
        }
    }
}