package com.gp.testes;

import com.gp.Tipo;
import com.gp.persistencia.EntidadeDAO;
import com.gp.persistencia.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TipoDAOTest {

    private EntidadeDAO<Tipo> dao;

    @BeforeEach
    void setUp() {
        // Instância nova e isolada para cada teste (não usa o DAOFactory de propósito,
        // já que ele é singleton e compartilharia estado entre os testes).
        dao = new EntidadeDAO<>(Tipo.class);
    }

    // ---------- salvar ----------

    @Test
    void salvar_comIdNovo_deveInserirComSucesso() throws PersistenceException {
        Tipo fogo = new Tipo(1, "Fogo", "Tipo de fogo");

        dao.salvar(fogo);

        assertTrue(dao.existsById(1));
        assertEquals(1, dao.size());
    }

    @Test
    void salvar_comIdJaExistente_deveLancarExcecaoENaoInserirDeNovo() throws PersistenceException {
        Tipo fogo = new Tipo(1, "Fogo", "Tipo de fogo");
        dao.salvar(fogo);

        Tipo fogoDuplicado = new Tipo(1, "Fogo2", "Outra descrição");

        assertThrows(PersistenceException.class, () -> dao.salvar(fogoDuplicado));
        assertEquals(1, dao.size()); // continua existindo só o primeiro
    }

    // ---------- atualizar ----------

    @Test
    void atualizar_comIdNaoExistente_deveLancarExcecaoENaoAlterarNada() {
        Tipo fogo = new Tipo(1, "Fogo", "Tipo de fogo");

        assertThrows(PersistenceException.class, () -> dao.atualizar(fogo));
        assertEquals(0, dao.size());
    }

    @Test
    void atualizar_comIdExistente_deveAlterarComSucesso() throws PersistenceException {
        Tipo fogo = new Tipo(1, "Fogo", "Tipo de fogo");
        dao.salvar(fogo);

        Tipo fogoAlterado = new Tipo(1, "Fogo", "Descrição atualizada");
        dao.atualizar(fogoAlterado);

        Tipo recuperado = dao.carregar(1);
        assertEquals("Descrição atualizada", recuperado.getDescricao());
        assertEquals(1, dao.size());
    }

    // ---------- apagar ----------

    @Test
    void apagar_comIdNaoExistente_deveLancarExcecaoENaoRemoverNada() {
        assertThrows(PersistenceException.class, () -> dao.apagar(99));
    }

    @Test
    void apagar_comIdExistente_deveRemoverEDevolverOObjetoRemovido() throws PersistenceException {
        Tipo fogo = new Tipo(1, "Fogo", "Tipo de fogo");
        dao.salvar(fogo);

        Tipo removido = dao.apagar(1);

        assertEquals(fogo, removido); // equals por id
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
        Tipo fogo = new Tipo(1, "Fogo", "Tipo de fogo");
        dao.salvar(fogo);

        Tipo carregado = dao.carregar(1);

        assertEquals("Fogo", carregado.getNome());
        assertEquals(1, carregado.getId());
    }
}