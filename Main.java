public class Main {
    public static void main(String[] args) {
        // Criando um tipo
        Tipo agua = new Tipo(1, "Água", "Tipo baseado em características de água");

        // Criando um pokemon (squirtle brabo)
        Pokemon squirtle = new Pokemon(7, "Squirtle", 4, 0.6, 0.5, "Pequena Tartaruga");

        // Adicionando tipos ao Pokémon
        squirtle.adicionarTipo(agua);

        // Testando getters
        System.out.println("Nome: " + squirtle.getNome());
        System.out.println("Número na Pokédex: " + squirtle.getNumeroPokedex());
        System.out.println("Descrição: " + squirtle.getDescricao());

        // Listando tipos
        System.out.println("Tipos do " + squirtle.getNome() + ":");
        for (Tipo t : squirtle.getTipo()) {
            System.out.println("- " + t.getNome() + " (" + t.getDescricao() + ")");
        }

    }
}
