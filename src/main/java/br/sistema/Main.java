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
import br.sistema.model.service.impl.*;
import br.sistema.model.service.interfaces.*;
import br.sistema.view.MenuPrincipal;
import br.sistema.util.ServiceRegistry;
import br.sistema.controller.impl.*;
import br.sistema.controller.interfaces.*;
import br.sistema.view.frame.LoginScreen;
import jakarta.persistence.EntityManager;
import org.flywaydb.core.Flyway;

import javax.swing.SwingUtilities;

public class Main {

    private static EntityManager em;

    public static void main(String[] args) {

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

        try {
            em = JPAConfig.getEntityManager();

            DisciplinaRepository disciplinaRepo = new DisciplinaRepositoryImpl(em);
            ProfessorRepository professorRepo = new ProfessorRepositoryImpl(em);
            TurnoRepository turnoRepo = new TurnoRepositoryImpl(em);
            TurmaRepository turmaRepo = new TurmaRepositoryImpl(em);
            AulaRepository aulaRepo = new AulaRepositoryImpl(em);

            DisciplinaService disciplinaServiceCore = new DisciplinaServiceImpl(disciplinaRepo);
            ProfessorService professorServiceCore = new ProfessorServiceImpl(professorRepo);
            TurnoService turnoServiceCore = new TurnoServiceImpl(turnoRepo);
            TurmaService turmaServiceCore = new TurmaServiceImpl(turmaRepo);

            DisciplinaService disciplinaService = new DisciplinaServiceTxDecorator(disciplinaServiceCore, em);
            ProfessorService professorService = new ProfessorServiceTxDecorator(professorServiceCore, em);
            TurnoService turnoService = new TurnoServiceTxDecorator(turnoServiceCore, em);
            TurmaService turmaService = new TurmaServiceTxDecorator(turmaServiceCore, em);

            AulaService aulaServiceCore = new AulaServiceImpl(aulaRepo, professorRepo);
            AulaService aulaService = new AulaServiceTxDecorator(aulaServiceCore, em);

            // Register Services
            ServiceRegistry registry = ServiceRegistry.getInstance();
            registry.register(DisciplinaService.class, disciplinaService);
            registry.register(ProfessorService.class, professorService);
            registry.register(TurnoService.class, turnoService);
            registry.register(TurmaService.class, turmaService);
            registry.register(AulaService.class, aulaService);

            // Register Controllers
            registry.register(ProfessorController.class, new ProfessorControllerImpl(professorService, disciplinaService));
            registry.register(DisciplinaController.class, new DisciplinaControllerImpl(disciplinaService));
            registry.register(TurnoController.class, new TurnoControllerImpl(turnoService));
            registry.register(TurmaController.class, new TurmaControllerImpl(turmaService));
            registry.register(AulaController.class, new AulaControllerImpl(aulaService));

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (em != null && em.isOpen()) {
                    em.close();
                    System.out.println("\nBase de dados desconectada. Sistema encerrado corretamente.");
                }
            }));

            System.out.println("Iniciando a interface do usuário...");

            SwingUtilities.invokeLater(() -> {
                LoginScreen login = new LoginScreen();
                login.setVisible(true);
            });

        } catch (Exception e) {
            System.err.println("[ERRO] Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();

            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}