import java.util.Scanner;

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
        squirtle.adicionarTipo(eletrico);

        // Definindo fraquezas
        fogo.adicionarFraquezas(agua); // fogo é fraco contra água
        agua.adicionarFraquezas(planta); // água é fraco contra planta
        planta.adicionarFraquezas(fogo); // planta é fraco contra fogo
        agua.adicionarFraquezas(eletrico); // água é fraco contra elétrico

        // Testando getters
        //Bulbasaur
        System.out.println("\nNome: " + bulbasaur.getNome());
        System.out.println("Número na Pokédex: " + bulbasaur.getNumeroPokedex());
        System.out.println("Descrição: " + bulbasaur.getDescricao());

        //Charmander
        System.out.println("\nNome: " + charmander.getNome());
        System.out.println("Número na Pokédex: " + charmander.getNumeroPokedex());
        System.out.println("Descrição: " + charmander.getDescricao());
        
        //Squirtle
        System.out.println("\nNome: " + squirtle.getNome());
        System.out.println("Número na Pokédex: " + squirtle.getNumeroPokedex());
        System.out.println("Descrição: " + squirtle.getDescricao());

        //Pikachu
        System.out.println("\nNome: " + pikachu.getNome());
        System.out.println("Número na Pokédex: " + pikachu.getNumeroPokedex());
        System.out.println("Descrição: " + pikachu.getDescricao());

        //Batalhas
        Pokemon p = Servicos.compararPokemons(squirtle, charmander);
        System.out.println("\nEm uma batalha pokémon, " + p.getNome() + " leva a vantagem e sai vitorioso!");

        p = Servicos.compararPokemons(squirtle, bulbasaur);
        System.out.println("\nEm uma batalha pokémon, " + p.getNome() + " leva a vantagem e sai vitorioso!");
        
        p = Servicos.compararPokemons(bulbasaur, pikachu);
        System.out.println("\nEm uma batalha pokémon, " + p.getNome() + " leva a vantagem e sai vitorioso!");

        // Criando treinadores
        Treinador ash = new Treinador(1, "Ash Ketchum","Kanto", 8);
        System.err.println("\nTreinador: " + ash.getNome() + "\nRegião: " + ash.getRegiao() + "\nInsígnias: " + ash.getInsignias() + "\n");
        Treinador misty = new Treinador(2, "Misty","Galar", 7);
        System.err.println("\nTreinador: " + misty.getNome() + "\nRegião: " + misty.getRegiao() + "\nInsígnias: " + misty.getInsignias() + "\n");   

        // Adicionando pokémons aos treinadores
        ash.addPokemon(squirtle);
        ash.addPokemon(pikachu);
        System.out.println("\nPokémons de " + ash.getNome() + ":");
        for (Pokemon pokemon : ash.getPokemons()) {
            System.out.println("- " + pokemon.getNome());
        }
        misty.addPokemon(charmander);
        misty.addPokemon(bulbasaur);
        System.out.println("\nPokémons de " + misty.getNome() + ":");
        for (Pokemon pokemon : misty.getPokemons()) {
            System.out.println("- " + pokemon.getNome());
        }

        // Batalha entre treinadores
        Treinador vencedor = Servicos.compararTreinadores(ash, misty);
        System.out.println("\nNa batalha entre treinadores, " + vencedor.getNome() + " leva a vantagem e sai vitorioso!");
    }
}
