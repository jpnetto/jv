package com.gp.testes;

import com.gp.Treinador;
import com.gp.persistencia.EntidadeDAO;
import com.gp.persistencia.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TreinadorDAOTest {

    private EntidadeDAO<Treinador> dao;

    @BeforeEach
    void setUp() {
        dao = new EntidadeDAO<>(Treinador.class);
    }

    private Treinador criarAsh(int id) {
        return new Treinador(id, "Ash", "Kanto", 0);
    }

    // ---------- salvar ----------

    @Test
    void salvar_comIdNovo_deveInserirComSucesso() throws PersistenceException {
        Treinador ash = criarAsh(1);

        dao.salvar(ash);

        assertTrue(dao.existsById(1));
        assertEquals(1, dao.size());
    }

    @Test
    void salvar_comIdJaExistente_deveLancarExcecaoENaoInserirDeNovo() throws PersistenceException {
        Treinador ash = criarAsh(1);
        dao.salvar(ash);

        Treinador outroComMesmoId = new Treinador(1, "Misty", "Kanto", 0);

        assertThrows(PersistenceException.class, () -> dao.salvar(outroComMesmoId));
        assertEquals(1, dao.size());
    }

    // ---------- atualizar ----------

    @Test
    void atualizar_comIdNaoExistente_deveLancarExcecaoENaoAlterarNada() {
        Treinador ash = criarAsh(1);

        assertThrows(PersistenceException.class, () -> dao.atualizar(ash));
        assertEquals(0, dao.size());
    }

    @Test
    void atualizar_comIdExistente_deveAlterarComSucesso() throws PersistenceException {
        Treinador ash = criarAsh(1);
        dao.salvar(ash);

        Treinador ashAlterado = new Treinador(1, "Ash", "Kanto", 5);
        dao.atualizar(ashAlterado);

        Treinador recuperado = dao.carregar(1);
        assertEquals(5, recuperado.getInsignias());
        assertEquals(1, dao.size());
    }

    // ---------- apagar ----------

    @Test
    void apagar_comIdNaoExistente_deveLancarExcecaoENaoRemoverNada() {
        assertThrows(PersistenceException.class, () -> dao.apagar(99));
    }

    @Test
    void apagar_comIdExistente_deveRemoverEDevolverOObjetoRemovido() throws PersistenceException {
        Treinador ash = criarAsh(1);
        dao.salvar(ash);

        Treinador removido = dao.apagar(1);

        assertEquals(ash, removido);
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
        Treinador ash = criarAsh(1);
        dao.salvar(ash);

        Treinador carregado = dao.carregar(1);

        assertEquals("Ash", carregado.getNome());
        assertEquals("Kanto", carregado.getRegiao());
    }
}