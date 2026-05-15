package br.sistema.util;

import br.sistema.controller.impl.ProfessorControllerImpl;
import br.sistema.controller.interfaces.ProfessorController;
import br.sistema.model.service.interfaces.*;

import java.util.HashMap;
import java.util.Map;

public class ServiceRegistry {
    private static final ServiceRegistry INSTANCE = new ServiceRegistry();
    private final Map<Class<?>, Object> services = new HashMap<>();

    private ServiceRegistry() {}

    public static ServiceRegistry getInstance() {
        return INSTANCE;
    }

    public <T> void register(Class<T> serviceClass, T serviceInstance) {
        services.put(serviceClass, serviceInstance);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> serviceClass) {
        return (T) services.get(serviceClass);
    }
}
