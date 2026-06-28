package com.gp;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entidade de transação (análoga a uma Venda/Locação/Consulta no enunciado).
 * Representa o registro de uma batalha entre dois Treinadores.
 *
 * Possui uma composição 1 para muitos com ItemBatalha (a classe intermediária):
 * cada confronto entre um Pokémon de treinador1 e um Pokémon de treinador2 gera
 * um ItemBatalha, que é salvo junto com a própria Batalha.
 *
 * Também possui associação com Treinador (treinador1, treinador2 e vencedor).
 */
public class Batalha extends Entidade {

    private Treinador treinador1;
    private Treinador treinador2;
    private Treinador vencedor;
    private List<ItemBatalha> confrontos;

    public Batalha() {
    }

    public Batalha(@JsonProperty("id") int id,
                   @JsonProperty("treinador1") Treinador treinador1,
                   @JsonProperty("treinador2") Treinador treinador2,
                   @JsonProperty("vencedor") Treinador vencedor,
                   @JsonProperty("confrontos") List<ItemBatalha> confrontos) {
        super(id);
        this.treinador1 = treinador1;
        this.treinador2 = treinador2;
        this.vencedor = vencedor;
        this.confrontos = (confrontos != null) ? confrontos : new ArrayList<>();
    }

    // Construtor de conveniência para criar uma batalha ainda sem confrontos/vencedor definidos
    public Batalha(int id, Treinador treinador1, Treinador treinador2) {
        super(id);
        this.treinador1 = treinador1;
        this.treinador2 = treinador2;
        this.confrontos = new ArrayList<>();
    }

    public Treinador getTreinador1() {
        return treinador1;
    }

    public void setTreinador1(Treinador treinador1) {
        this.treinador1 = treinador1;
    }

    public Treinador getTreinador2() {
        return treinador2;
    }

    public void setTreinador2(Treinador treinador2) {
        this.treinador2 = treinador2;
    }

    public Treinador getVencedor() {
        return vencedor;
    }

    public void setVencedor(Treinador vencedor) {
        this.vencedor = vencedor;
    }

    public List<ItemBatalha> getConfrontos() {
        return confrontos;
    }

    // Métodos para adicionar/remover objetos da classe intermediária, exigidos pelo enunciado
    public void adicionarConfronto(ItemBatalha confronto) {
        this.confrontos.add(confronto);
    }

    public void removerConfronto(ItemBatalha confronto) {
        this.confrontos.remove(confronto);
    }

    @Override
    public String toString() {
        String nomeVencedor = (vencedor != null) ? vencedor.getNome() : "indefinido";
        return "Batalha [id=" + id + ", " + treinador1.getNome() + " vs " + treinador2.getNome()
                + ", vencedor=" + nomeVencedor + ", confrontos=" + confrontos.size() + "]";
    }
}