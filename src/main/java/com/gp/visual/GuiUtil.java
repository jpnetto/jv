package com.gp.visual;

import com.gp.Entidade;
import com.gp.persistencia.EntidadeDAO;
import com.gp.persistencia.PersistenceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Pequenas funções repetidas pelos painéis para conversar com os DAOs existentes. */
public class GuiUtil {

    /** Converte o array de carregarTodos() em List, tratando o conjunto vazio. */
    public static <E extends Entidade> List<E> listar(EntidadeDAO<E> dao) {
        if (dao.size() == 0) {
            return new ArrayList<>();
        }
        try {
            return new ArrayList<>(Arrays.asList(dao.carregarTodos()));
        } catch (PersistenceException e) {
            return new ArrayList<>();
        }
    }
}
