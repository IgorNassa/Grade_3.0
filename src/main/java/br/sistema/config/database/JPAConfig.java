package br.sistema.config.database;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAConfig {
    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("myPU");
    public static EntityManager getEntityManager(){
        return entityManagerFactory.createEntityManager();
    }

}
