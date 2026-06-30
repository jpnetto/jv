package com.gp;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Scanner;

public class Pokemon extends Entidade {

    //atributos da classe
    private String nome;
    private int numeroPokedex;
    private List<Tipo> tipo;
    private double altura;
    private double peso;
    private int stats;
    private String descricao;

    //getters e setters

    public Pokemon() {

    }

    public Pokemon(@JsonProperty("id") int id,
                    @JsonProperty("nome") String nome,
                    @JsonProperty("numeroPokedex") int numeroPokedex,
                    @JsonProperty("altura") double altura,
                    @JsonProperty("peso") double peso,
                    @JsonProperty("stats") int stats,
                    @JsonProperty("descricao") String descricao) {
        super(id);
        this.nome = nome;
        this.numeroPokedex = numeroPokedex;
        this.altura = altura;
        this.peso = peso;
        this.stats = stats;
        this.descricao = descricao;
        this.tipo = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumeroPokedex() {
        return numeroPokedex;
    }

    public void setNumeroPokedex(int numeroPokedex) {
        this.numeroPokedex = numeroPokedex;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getStats() {
        return stats;
    }

    public void setStats(int stats) {
        this.stats = stats;
    }

    public void adicionarTipo(Tipo tipo) {
        this.tipo.add(tipo);
    }

    public void removerTipo(Tipo tipo) {
        this.tipo.remove(tipo);
    }

    public void setTipos(List<Tipo> tipo) {
        this.tipo = tipo;
    }

    public List<Tipo> getTipos() {
        return tipo;
    }

    @Override
    public String toString() {
        return "Nome:"+ nome + " [id:" + id + "]";
    }


    // Função para buscar um pokémon por nome em uma lista de pokémons
    public static Pokemon buscarPokemonPorNome(List<Pokemon> pokemons, String nome) {
        for (Pokemon pokemon : pokemons) {
            if (pokemon.getNome().equalsIgnoreCase(nome)) {
                return pokemon;
            }
        }
        return null;
    }

    // Apenas constrói o objeto a partir da entrada do usuário.
    // Quem decide salvar (via EntidadeDAO) é quem chama este método.
    public static Pokemon criarPokemon(int id, Scanner scanner) {
        System.out.print("Digite o nome do Pokémon: ");
        String nome = scanner.nextLine();

        System.out.print("Digite o número na Pokédex: ");
        int numeroPokedex = scanner.nextInt();

        System.out.print("Digite a altura do Pokémon: ");
        double altura = scanner.nextDouble();

        System.out.print("Digite o peso do Pokémon: ");
        double peso = scanner.nextDouble();

        System.out.print("Digite os stats do Pokémon: ");
        int stats = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        System.out.print("Digite a descrição do Pokémon: ");
        String descricao = scanner.nextLine();

        Pokemon pokemon = new Pokemon(id, nome, numeroPokedex, altura, peso, stats, descricao);
        System.out.println("Pokémon criado com sucesso!");

        return pokemon;
    }

}