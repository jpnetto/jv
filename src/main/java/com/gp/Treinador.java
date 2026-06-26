package com.gp;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Treinador extends Entidade {

    private String nome;
    private String regiao;
    private List<Pokemon> pokemons;
    private int insignias;

    @JsonCreator
    public Treinador(@JsonProperty("id") int id,
                      @JsonProperty("nome") String nome,
                      @JsonProperty("regiao") String regiao,
                      @JsonProperty("insignias") int insignias) {
        super(id);
        this.nome = nome;
        this.regiao = regiao;
        this.pokemons = new ArrayList<>();
        this.insignias = insignias;
    }

    public Treinador() {

    }

    public void addPokemon(Pokemon pokemon) {
        pokemons.add(pokemon);
    }

    public void removerPokemon(Pokemon pokemon) {
        pokemons.remove(pokemon);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public int getInsignias() {
        return insignias;
    }

    public void setInsignias(int insignias) {
        this.insignias = insignias;
    }

    // Função para buscar um treinador por nome em uma lista de treinadores
    public static Treinador buscarTreinadorPorNome(List<Treinador> treinadores, String nome) {
        for (Treinador treinador : treinadores) {
            if (treinador.getNome().equalsIgnoreCase(nome)) {
                return treinador;
            }
        }
        return null;
    }

    // Apenas constrói o objeto a partir da entrada do usuário.
    // Quem decide salvar (via EntidadeDAO) é quem chama este método.
    public static Treinador criarTreinador(int id, Scanner scanner) {
        System.out.print("Digite o nome do Treinador: ");
        String nome = scanner.nextLine();

        System.out.print("Digite a região do Treinador: ");
        String regiao = scanner.nextLine();

        System.out.print("Digite o número de insígnias do Treinador: ");
        int insignias = scanner.nextInt();

        Treinador treinador = new Treinador(id, nome, regiao, insignias);
        System.out.println("Treinador criado com sucesso!");
        return treinador;
    }

}