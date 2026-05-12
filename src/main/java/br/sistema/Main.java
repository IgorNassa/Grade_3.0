package br.sistema;
import br.sistema.config.database.JPAConfig;
import br.sistema.model.service.impl.*;
import br.sistema.model.service.interfaces.*;
import br.sistema.view.MenuPrincipal;
import br.sistema.view.frame.DashFrame;
import jakarta.persistence.EntityManager;
import org.flywaydb.core.Flyway;
import br.sistema.model.repository.impl.DisciplinaRepositoryImpl;
import br.sistema.model.repository.impl.ProfessorRepositoryImpl;
import br.sistema.model.repository.impl.TurnoRepositoryImpl;
import br.sistema.model.repository.impl.TurmaRepositoryImpl;
import br.sistema.model.repository.impl.AulaRepositoryImpl;


public class Main {
    public static void main(String[] args) {
     new DashFrame();

    }
}                             