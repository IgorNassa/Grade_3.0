package br.sistema;
import br.sistema.config.database.JPAConfig;
import br.sistema.model.repository.impl.AulaRepositoryImpl;
import br.sistema.model.repository.impl.DisciplinaRepositoryImpl;
import br.sistema.model.repository.impl.ProfessorRepositoryImpl;
import br.sistema.model.repository.impl.TurmaRepositoryImpl;
import br.sistema.model.repository.impl.TurnoRepositoryImpl;
import br.sistema.model.repository.interfaces.AulaRepository;
import br.sistema.model.repository.interfaces.DisciplinaRepository;
import br.sistema.model.repository.interfaces.ProfessorRepository;
import br.sistema.model.repository.interfaces.TurmaRepository;
import br.sistema.model.repository.interfaces.TurnoRepository;
import br.sistema.model.service.impl.AulaServiceImpl;
import br.sistema.model.service.impl.DisciplinaServiceImpl;
import br.sistema.model.service.impl.ProfessorServiceImpl;
import br.sistema.model.service.impl.TurmaServiceImpl;
import br.sistema.model.service.impl.TurnoServiceImpl;
import br.sistema.model.service.interfaces.AulaService;
import br.sistema.model.service.interfaces.DisciplinaService;
import br.sistema.model.service.interfaces.ProfessorService;
import br.sistema.model.service.interfaces.TurmaService;
import br.sistema.model.service.interfaces.TurnoService;
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

        EntityManager em;

        try {
            em = JPAConfig.getEntityManager();
        } catch (Exception e) {
            System.err.println("[ERRO] Falha ao conectar o JPA: " + e.getMessage());
            return;
        }

        try {
            DisciplinaRepository disciplinaRepo = new DisciplinaRepositoryImpl(em);
            ProfessorRepository professorRepo = new ProfessorRepositoryImpl(em);
            TurnoRepository turnoRepo = new TurnoRepositoryImpl(em);
            TurmaRepository turmaRepo = new TurmaRepositoryImpl(em);
            AulaRepository aulaRepo = new AulaRepositoryImpl(em);

            DisciplinaService disciplinaService = new DisciplinaServiceImpl(disciplinaRepo);
            ProfessorService professorService = new ProfessorServiceImpl(professorRepo);
            TurnoService turnoService = new TurnoServiceImpl(turnoRepo);
            TurmaService turmaService = new TurmaServiceImpl(turmaRepo);
            AulaService aulaService = new AulaServiceImpl(aulaRepo, em);

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
            if (em.isOpen()) {
                em.close();
            }

            System.out.println("\nBase de dados desconectada. Sistema encerrado corretamente.");
        }
    }
}