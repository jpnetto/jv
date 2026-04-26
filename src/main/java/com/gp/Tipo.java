package com.gp;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Tipo extends Entidade {
    
    private String nome;
    private String descricao;
    @JsonIgnore
    private List<Tipo> fraquezas;

    public Tipo(@JsonProperty("id") int id, 
                @JsonProperty("nome") String nome, 
                @JsonProperty("descricao") String descricao) {
        super(id);
        this.nome = nome;
        this.descricao = descricao;
        this.fraquezas = new ArrayList<>();
    }



    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void adicionarFraquezas(Tipo tipo) {
        this.fraquezas.add(tipo);
    }

    public void removerFraquezas(Tipo tipo) {
        this.fraquezas.remove(tipo);
    }

    public List<Tipo> getFraquezas() {
        return fraquezas;
    }

    @Override
    public String toString() {
        return "Tipo: " + nome + " - " + descricao;
    }


    @Override
    public java.util.List<Tipo> carregarTodos() {
        ObjectMapper mapper = new ObjectMapper();
        List<Tipo> tipos = new ArrayList<>();
        try {
            tipos = mapper.readValue(
                new File("tipos.json"),
                new com.fasterxml.jackson.core.type.TypeReference<List<Tipo>>() {}
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tipos;
    }

}
