package com.gp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Criando tipos
        Tipo agua = new Tipo(1, "Água", "Tipo baseado em características de água");
        Tipo fogo = new Tipo(2, "Fogo", "Tipo baseado em características de fogo");
        Tipo planta = new Tipo(3, "Planta", "Tipo baseado em características da natureza");
        Tipo eletrico = new Tipo(4, "Elétrico", "Tipo baseado em características da eletricidade");

        // Criando Pokémons
        Pokemon squirtle = new Pokemon(7, "Squirtle", 7, 0.5, 9, 314, "Pequena Tartaruga");
        Pokemon charmander = new Pokemon(4, "Charmander", 4, 0.6, 8.5, 309, "Lagarto de fogo");
        Pokemon bulbasaur = new Pokemon(1, "Bulbasaur", 1, 0.7, 6.9, 318, "Pokémon Semente");
        Pokemon pikachu = new Pokemon(25, "Pikachu", 25, 0.5, 6, 320, "Pokémon Rato");

        // Adicionando tipos
        squirtle.adicionarTipo(agua);
        charmander.adicionarTipo(fogo);
        bulbasaur.adicionarTipo(planta);
        pikachu.adicionarTipo(eletrico);

        
        // Definindo fraquezas
        fogo.adicionarFraquezas(agua);
        agua.adicionarFraquezas(planta);
        planta.adicionarFraquezas(fogo);
        agua.adicionarFraquezas(eletrico);

        // Testando getters
        System.out.println("\nNome: " + bulbasaur.getNome());
        System.out.println("Número na Pokédex: " + bulbasaur.getNumeroPokedex());
        System.out.println("Descrição: " + bulbasaur.getDescricao());

        // Batalhas
        Pokemon p = Servicos.compararPokemons(squirtle, charmander);
        System.out.println("\nEm uma batalha pokémon, " + p.getNome() + " leva a vantagem!");

        // Criando treinadores
        Treinador ash = new Treinador(1, "Ash Ketchum","Kanto", 8);
        Treinador misty = new Treinador(2, "Misty","Galar", 7);

        ash.addPokemon(squirtle);
        ash.addPokemon(pikachu);
        misty.addPokemon(charmander);
        misty.addPokemon(bulbasaur);

        // Batalha entre treinadores
        Treinador vencedor = Servicos.compararTreinadores(ash, misty);
        System.out.println("\nNa batalha entre treinadores, " + vencedor.getNome() + " leva a vantagem!");

        // Teste com JSON (Jackson)     
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Serializar treinador Ash para JSON
            ash.salvar("treinadores.json", Treinador.class);
            misty.salvar("treinadores.json", Treinador.class);

            // Desserializar de volta
            Treinador t = new Treinador();
            List<Treinador> treinadores = t.carregarTodos();

            // Exibindo os treinadores carregados
            for (Treinador tr : treinadores) {
                System.out.println("Treinador: " + tr.getNome());
                for (Pokemon pk : tr.getPokemons()) {
                    System.out.println(" - Pokémon: " + pk.getNome());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
