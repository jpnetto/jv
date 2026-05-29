package com.gp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Scanner;


public class Tipo extends Entidade {
    
    private String nome;
    private String descricao;
    @JsonIgnore
    private List<Tipo> fraquezas;

    public Tipo() {
        
    }

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
        if (!this.fraquezas.contains(tipo)) {
            this.fraquezas.add(tipo);
        }
    }

    // Função para buscar um tipo por nome em uma lista de tipos
    public static Tipo buscarTipoPorNome(List<Tipo> tipos, String nome) {
        for (Tipo tipo : tipos) {
            if (tipo.getNome().equalsIgnoreCase(nome)) {
                return tipo;
            }
        }
        return null;
    }

    public static Tipo criarTipo (int id, Scanner scanner){
        scanner = new Scanner(System.in);
        System.out.print("Digite o nome do Tipo: ");
        String nome = scanner.nextLine();

        System.out.print("Digite a descrição do Tipo: ");
        String descricao = scanner.nextLine();

        Tipo tipo = new Tipo(id, nome, descricao);
        tipo.salvar("tipos.json", Tipo.class);
        System.out.println("Tipo criado com sucesso!");
        return tipo;
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
    public List<Tipo> carregarTodos() {
        ObjectMapper mapper = new ObjectMapper();
        File arquivo = new File("tipos.json");

        if (!arquivo.exists() || arquivo.length() == 0) {
            System.err.println("Não há tipos por enquanto!");
            return Collections.emptyList();
        }

        try {
            return mapper.readValue(
                arquivo,
                new com.fasterxml.jackson.core.type.TypeReference<List<Tipo>>() {}
            );
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


}
