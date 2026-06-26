package com.gp.persistencia;

import java.util.HashMap;
import java.util.Map;

import com.gp.Entidade;

/**
 * Garante que exista apenas uma única instância de EntidadeDAO
 * por classe de entidade em todo o sistema, evitando ambiguidades
 * na persistência (duas DAOs diferentes manipulando o mesmo conjunto
 * de dados de uma mesma entidade).
 *
 * Uso:
 *   EntidadeDAO<Cliente> dao = DAOFactory.getDAO(Cliente.class);
 */
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