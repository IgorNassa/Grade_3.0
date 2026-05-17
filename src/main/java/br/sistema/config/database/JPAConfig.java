package br.sistema.config.database;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAConfig {
    private static EntityManagerFactory entityManagerFactory;

    public static void init(java.util.Map<String, String> properties) {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("myPU", properties);
        }
    }

    public static EntityManager getEntityManager(){
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("myPU");
        }
        return entityManagerFactory.createEntityManager();
    }
}
