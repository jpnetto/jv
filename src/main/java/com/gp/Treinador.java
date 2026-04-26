package com.gp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Override
    public List<Treinador> carregarTodos() {
        ObjectMapper mapper = new ObjectMapper();
        List<Treinador> treinadores = new ArrayList<>();

        try {
            treinadores = mapper.readValue(
                new File("treinadores.json"),
                new com.fasterxml.jackson.core.type.TypeReference<List<Treinador>>() {}
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return treinadores;
    }


    

}
