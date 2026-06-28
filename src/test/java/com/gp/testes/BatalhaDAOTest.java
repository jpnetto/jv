package com.gp.testes;

import com.gp.Batalha;
import com.gp.Treinador;
import com.gp.persistencia.EntidadeDAO;
import com.gp.persistencia.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BatalhaDAOTest {

    private EntidadeDAO<Batalha> dao;
    private Treinador treinador1;
    private Treinador treinador2;

    @BeforeEach
    void setUp() {
        dao = new EntidadeDAO<>(Batalha.class);
        treinador1 = new Treinador(1, "Ash", "Kanto", 0);
        treinador2 = new Treinador(2, "Misty", "Kanto", 0);
    }

    // ---------- salvar ----------

    @Test
    void salvar_comIdNovo_deveInserirComSucesso() throws PersistenceException {
        Batalha batalha = new Batalha(1, treinador1, treinador2);

        dao.salvar(batalha);

        assertTrue(dao.existsById(1));
        assertEquals(1, dao.size());
    }

    @Test
    void salvar_comIdJaExistente_deveLancarExcecaoENaoInserirDeNovo() throws PersistenceException {
        Batalha batalha = new Batalha(1, treinador1, treinador2);
        dao.salvar(batalha);

        Batalha outraComMesmoId = new Batalha(1, treinador2, treinador1);

        assertThrows(PersistenceException.class, () -> dao.salvar(outraComMesmoId));
        assertEquals(1, dao.size());
    }

    // ---------- atualizar ----------

    @Test
    void atualizar_comIdNaoExistente_deveLancarExcecaoENaoAlterarNada() {
        Batalha batalha = new Batalha(1, treinador1, treinador2);

        assertThrows(PersistenceException.class, () -> dao.atualizar(batalha));
        assertEquals(0, dao.size());
    }

    @Test
    void atualizar_comIdExistente_deveAlterarComSucesso() throws PersistenceException {
        Batalha batalha = new Batalha(1, treinador1, treinador2);
        dao.salvar(batalha);

        Batalha batalhaAlterada = new Batalha(1, treinador1, treinador2);
        batalhaAlterada.setVencedor(treinador1);
        dao.atualizar(batalhaAlterada);

        Batalha recuperada = dao.carregar(1);
        assertEquals(treinador1, recuperada.getVencedor());
        assertEquals(1, dao.size());
    }

    // ---------- apagar ----------

    @Test
    void apagar_comIdNaoExistente_deveLancarExcecaoENaoRemoverNada() {
        assertThrows(PersistenceException.class, () -> dao.apagar(99));
    }

    @Test
    void apagar_comIdExistente_deveRemoverEDevolverOObjetoRemovido() throws PersistenceException {
        Batalha batalha = new Batalha(1, treinador1, treinador2);
        dao.salvar(batalha);

        Batalha removida = dao.apagar(1);

        assertEquals(batalha, removida);
        assertEquals(0, dao.size());
        assertFalse(dao.existsById(1));
    }

    // ---------- carregar ----------

    @Test
    void carregar_comIdNaoExistente_deveLancarExcecao() {
        assertThrows(PersistenceException.class, () -> dao.carregar(42));
    }

    @Test
    void carregar_comIdExistente_deveRetornarObjetoCorreto() throws PersistenceException {
        Batalha batalha = new Batalha(1, treinador1, treinador2);
        dao.salvar(batalha);

        Batalha carregada = dao.carregar(1);

        assertEquals(treinador1, carregada.getTreinador1());
        assertEquals(treinador2, carregada.getTreinador2());
    }
}