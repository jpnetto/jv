package com.gp.persistencia;

import java.util.HashMap;
import java.util.Map;

import com.gp.Entidade;


public class DAOFactory {

    private static final Map<Class<?>, EntidadeDAO<?>> instancias = new HashMap<>();

    // Impede instanciação
    private DAOFactory() {
    }

    @SuppressWarnings("unchecked")
    public static synchronized <E extends Entidade> EntidadeDAO<E> getDAO(Class<E> clazz) {
        EntidadeDAO<?> dao = instancias.get(clazz);
        if (dao == null) {
            dao = new EntidadeDAO<>(clazz);
            instancias.put(clazz, dao);
        }
        return (EntidadeDAO<E>) dao;
    }
}