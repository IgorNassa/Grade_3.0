package br.sistema;
import br.sistema.config.database.JPAConfig;

import br.sistema.view.MenuPrincipal;
import jakarta.persistence.EntityManager;
import org.flywaydb.core.Flyway;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" Inicializando o Sistema (SGDG)...");
        System.out.println("========================================");

        String dbUrl = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/grade");
        String dbUser = System.getenv().getOrDefault("DB_USER", "postgres");
        String dbPass = System.getenv().getOrDefault("DB_PASS", "1234");

        System.out.println("Verificando e executando migrações do banco de dados (Flyway)...");
        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(dbUrl, dbUser, dbPass)
                    .baselineOnMigrate(true)
                    .baselineVersion("0")
                    .load();

            flyway.migrate();

            System.out.println("Migrações concluídas com sucesso!");
        } catch (Exception e) {
            System.err.println("[ERRO CRÍTICO] Falha ao rodar o Flyway: " + e.getMessage());
            System.err.println("Verifique se o PostgreSQL está ligado e as credenciais estão corretas.");
            return;
        }

        System.out.println("Conectando ao banco de dados...");
        EntityManager em = null;

        try {
            em = JPAConfig.getEntityManager();
        } catch (Exception e) {
            System.err.println("[ERRO] Falha ao conectar o JPA: " + e.getMessage());
            return;
        }

        try {
            DisciplinaRepository disciplinaRepo = new DisciplinaRepository(em);
            ProfessorRepository professorRepo = new ProfessorRepository(em);
            TurnoRepository turnoRepo = new TurnoRepository(em);
            TurmaRepository turmaRepo = new TurmaRepository(em);
            AulaRepository aulaRepo = new AulaRepository(em);

            DisciplinaService disciplinaService = new DisciplinaService(disciplinaRepo);
            ProfessorService professorService = new ProfessorService(professorRepo);
            TurnoService turnoService = new TurnoService(turnoRepo);
            TurmaService turmaService = new TurmaService(turmaRepo);
            AulaService aulaService = new AulaService(aulaRepo, em);

            System.out.println("Iniciando a interface do usuário...");

            System.out.print("\033[H\033[2J");
            System.out.flush();

            MenuPrincipal menu = new MenuPrincipal(
                    disciplinaService,
                    professorService,
                    turnoService,
                    turmaService,
                    aulaService
            );

            menu.iniciar();

        } catch (Exception e) {
            System.err.println("[ERRO] Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            System.out.println("\nBase de dados desconectada. Sistema encerrado corretamente.");
        }
    }
}