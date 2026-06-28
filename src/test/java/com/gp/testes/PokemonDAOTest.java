package com.gp.testes;

import com.gp.Pokemon;
import com.gp.persistencia.EntidadeDAO;
import com.gp.persistencia.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PokemonDAOTest {

    private EntidadeDAO<Pokemon> dao;

    @BeforeEach
    void setUp() {
        dao = new EntidadeDAO<>(Pokemon.class);
    }

    private Pokemon criarPikachu(int id) {
        return new Pokemon(id, "Pikachu", 25, 0.4, 6.0, 320, "Pokémon elétrico");
    }

    // ---------- salvar ----------

    @Test
    void salvar_comIdNovo_deveInserirComSucesso() throws PersistenceException {
        Pokemon pikachu = criarPikachu(1);

        dao.salvar(pikachu);

        assertTrue(dao.existsById(1));
        assertEquals(1, dao.size());
    }

    @Test
    void salvar_comIdJaExistente_deveLancarExcecaoENaoInserirDeNovo() throws PersistenceException {
        Pokemon pikachu = criarPikachu(1);
        dao.salvar(pikachu);

        Pokemon outroComMesmoId = new Pokemon(1, "Raichu", 26, 0.8, 30.0, 480, "Evolução do Pikachu");

        assertThrows(PersistenceException.class, () -> dao.salvar(outroComMesmoId));
        assertEquals(1, dao.size());
    }

    // ---------- atualizar ----------

    @Test
    void atualizar_comIdNaoExistente_deveLancarExcecaoENaoAlterarNada() {
        Pokemon pikachu = criarPikachu(1);

        assertThrows(PersistenceException.class, () -> dao.atualizar(pikachu));
        assertEquals(0, dao.size());
    }

    @Test
    void atualizar_comIdExistente_deveAlterarComSucesso() throws PersistenceException {
        Pokemon pikachu = criarPikachu(1);
        dao.salvar(pikachu);

        Pokemon pikachuAlterado = new Pokemon(1, "Pikachu", 25, 0.4, 6.0, 400, "Descrição nova");
        dao.atualizar(pikachuAlterado);

        Pokemon recuperado = dao.carregar(1);
        assertEquals(400, recuperado.getStats());
        assertEquals(1, dao.size());
    }

    // ---------- apagar ----------

    @Test
    void apagar_comIdNaoExistente_deveLancarExcecaoENaoRemoverNada() {
        assertThrows(PersistenceException.class, () -> dao.apagar(99));
    }

    @Test
    void apagar_comIdExistente_deveRemoverEDevolverOObjetoRemovido() throws PersistenceException {
        Pokemon pikachu = criarPikachu(1);
        dao.salvar(pikachu);

        Pokemon removido = dao.apagar(1);

        assertEquals(pikachu, removido);
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
        Pokemon pikachu = criarPikachu(1);
        dao.salvar(pikachu);

        Pokemon carregado = dao.carregar(1);

        assertEquals("Pikachu", carregado.getNome());
        assertEquals(25, carregado.getNumeroPokedex());
    }
}