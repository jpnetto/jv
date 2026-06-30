package com.gp;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    // Apenas constrói o objeto a partir da entrada do usuário.
    // Quem decide salvar (via EntidadeDAO) é quem chama este método.
    public static Tipo criarTipo(int id, Scanner scanner) {
        System.out.print("Digite o nome do Tipo: ");
        String nome = scanner.nextLine();

        System.out.print("Digite a descrição do Tipo: ");
        String descricao = scanner.nextLine();

        Tipo tipo = new Tipo(id, nome, descricao);
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
        return "Nome:"+ nome + " [id:" + id + "]";
    }


}