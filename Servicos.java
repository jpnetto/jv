import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Servicos {
    public static Tipo criarTipo (int id){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do Tipo: ");
        String nome = scanner.nextLine();

        System.out.print("Digite a descrição do Tipo: ");
        String descricao = scanner.nextLine();

        scanner.close();

        Tipo tipo = new Tipo(id, nome, descricao);
        System.out.println("Tipo criado com sucesso!");
        return tipo;
    }

    public static Pokemon criarPokemon(int id){
        Scanner scanner = new Scanner(System.in);
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

        scanner.close();

        Pokemon pokemon = new Pokemon(id, nome, numeroPokedex, altura, peso, stats, descricao);
        System.out.println("Pokémon criado com sucesso!");

        return pokemon;
    }

    public static Treinador criarTreinador(int id){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do Treinador: ");
        String nome = scanner.nextLine();

        System.out.print("Digite a região do Treinador: ");
        String regiao = scanner.nextLine();

        System.out.print("Digite o número de insígnias do Treinador: ");
        int insignias = scanner.nextInt();

        Treinador treinador = new Treinador(id, nome, regiao, insignias);
        System.out.println("Treinador criado com sucesso!");

        scanner.close();

        return treinador;
    }
    
    public static Pokemon compararPokemons(Pokemon p1, Pokemon p2) {
        
        ArrayList<Tipo> tipo1 = p1.getTipos();
        ArrayList<Tipo> tipo2 = p2.getTipos();
        
        int cont1 = 0;
        int cont2 = 0;

        for (Tipo t1 : tipo1) {
            for (Tipo t2 : tipo2) {
                if (t1.getFraquezas().contains(t2)) {
                    cont2++;
                }
            }
        }

        // Verifica se algum tipo de p2 é fraco contra algum tipo de p1
        for (Tipo t2 : tipo2) {
            for (Tipo t1 : tipo1) {
                if (t2.getFraquezas().contains(t1)) {
                    cont1++;
                }
            }
        }
        if(cont1==cont2){
            return (p1.getStats() > p2.getStats()) ? p1 : p2;

        } else{
            return (cont1 > cont2) ? p1 : p2;
        }
    }

    public static Treinador compararTreinadores(Treinador t1, Treinador t2) {
        int cont1 = 0;
        int cont2 = 0;

        List<Pokemon> pokemonsT1 = t1.getPokemons();
        List<Pokemon> pokemonsT2 = t2.getPokemons();

        for (Pokemon p1 : pokemonsT1) {
            for (Pokemon p2 : pokemonsT2) {
                Pokemon vencedor = compararPokemons(p1, p2);
                if (vencedor == p1) {
                    cont1++;
                } else if (vencedor == p2) {
                    cont2++;
                }
            }
        }

        if(cont1==cont2){
            if(t1.getInsignias() > t2.getInsignias()){
                t1.setInsignias(t1.getInsignias() + 1);
            } else{
                t2.setInsignias(t2.getInsignias() + 1);
            }
            return (t1.getInsignias() > t2.getInsignias()) ? t1 : t2;

        } else{
            if(cont1>cont2){
                t1.setInsignias(t1.getInsignias() + 1);
            } else{
                t2.setInsignias(t2.getInsignias() + 1);
            }
            return (cont1 > cont2) ? t1 : t2;
        }
    }
}



