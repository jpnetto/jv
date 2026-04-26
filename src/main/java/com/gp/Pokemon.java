package com.gp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Pokemon extends Entidade{


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

    public List<Tipo> getTipos() {
        return tipo;
    } 

    //funções
    public String toString(){
        return "id = " + id;
    }

    @Override
    public java.util.List<Pokemon> carregarTodos() {
        ObjectMapper mapper = new ObjectMapper();
        List<Pokemon> pokemons = new ArrayList<>();
        try {
            pokemons = mapper.readValue(
                new File("pokemons.json"),
                new com.fasterxml.jackson.core.type.TypeReference<List<Pokemon>>() {}
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pokemons;
    }
    
    
    

}