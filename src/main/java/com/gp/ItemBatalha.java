package com.gp;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe intermediária da composição 1.* entre Batalha e Pokemon.
 * Cada ItemBatalha representa um confronto entre dois Pokémons
 * dentro de uma Batalha.
 *
 * Não é uma subclasse de Entidade: não possui id próprio nem é armazenada em um
 * EntidadeDAO separado, ela só existe "dentro" de uma Batalha e é salva junto com ela.
 */
public class ItemBatalha {

    private Pokemon pokemon1;
    private Pokemon pokemon2;
    private Pokemon vencedor; // null em caso de empate no confronto

    public ItemBatalha() {
    }

    public ItemBatalha(@JsonProperty("pokemon1") Pokemon pokemon1,
                        @JsonProperty("pokemon2") Pokemon pokemon2,
                        @JsonProperty("vencedor") Pokemon vencedor) {
        this.pokemon1 = pokemon1;
        this.pokemon2 = pokemon2;
        this.vencedor = vencedor;
    }

    public Pokemon getPokemon1() {
        return pokemon1;
    }

    public void setPokemon1(Pokemon pokemon1) {
        this.pokemon1 = pokemon1;
    }

    public Pokemon getPokemon2() {
        return pokemon2;
    }

    public void setPokemon2(Pokemon pokemon2) {
        this.pokemon2 = pokemon2;
    }

    public Pokemon getVencedor() {
        return vencedor;
    }

    public void setVencedor(Pokemon vencedor) {
        this.vencedor = vencedor;
    }

    @Override
    public String toString() {
        String nomeVencedor = (vencedor != null) ? vencedor.getNome() : "empate";
        return pokemon1.getNome() + " vs " + pokemon2.getNome() + " -> vencedor: " + nomeVencedor;
    }
}