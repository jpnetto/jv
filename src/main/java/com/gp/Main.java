package com.gp;
import java.util.List;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) {
        while (true){
            int totalEntidades = new Tipo().carregarTodos().size() + new Pokemon().carregarTodos().size() + new Treinador().carregarTodos().size();
            List<Tipo> todosTipos = new Tipo().carregarTodos();
            List<Pokemon> todosPokemons = new Pokemon().carregarTodos();   
            List<Treinador> todosTreinadores = new Treinador().carregarTodos();
            System.out.println("O que deseja fazer?");
            System.out.println("1. Alterar/remover/criar Tipo:");
            System.out.println("2. Alterar/remover/criar Pokémon:");
            System.out.println("3. Alterar/remover/criar Treinador");
            System.out.println("4. Criar uma batalha entre dois treinadores");
            System.out.println("5. Sair");

            Scanner scanner = new Scanner(System.in);
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    System.out.println("Tipos disponíveis:");
                    for (Tipo tipo : todosTipos) {
                        System.out.println("- " + tipo.getNome());
                    };
                    System.out.println("O que deseja fazer com o Tipo?");
                    System.out.println("1. Criar Tipo");
                    System.out.println("2. Alterar Tipo");
                    System.out.println("3. Remover Tipo");
                    System.out.println("4. Adicionar fraqueza a um Tipo");
                    System.out.println("5. Voltar");
                    int tipoOpcao = scanner.nextInt();
                        switch (tipoOpcao) {
                            case (1):{
                                Tipo tipo = Servicos.criarTipo(totalEntidades+1, scanner); // ID fixo para exemplo
                                break;
                            }
                            case (2):{
                                System.out.print("Digite o nome do Tipo: ");
                                scanner.nextLine(); // Consumir a nova linha
                                String nomeTipo = scanner.nextLine();
                                Tipo tipoAlterar = Tipo.buscarTipoPorNome(todosTipos, nomeTipo);
                                if (tipoAlterar != null) {
                                    System.out.println("Tipo encontrado com sucesso!");
                                } else {
                                    System.out.println("Tipo não encontrado.");
                                    break;
                                }
                                System.out.println("O que deseja alterar no Tipo?");
                                System.out.println("1. Nome");
                                System.out.println("2. Descrição");
                                System.out.println("3. Fraquezas");
                                System.out.println("4. Voltar");
                                int alterarTipoOpcao = scanner.nextInt();
                                    switch (alterarTipoOpcao) {
                                        case 1:
                                            System.out.println("Digite o novo nome do Tipo:");
                                            String novoNome = scanner.nextLine();
                                            tipoAlterar.setNome(novoNome);
                                            tipoAlterar.salvar("tipos.json", Tipo.class);
                                            System.out.println("Nome do Tipo alterado com sucesso!");
                                            break;
                                        case 2:
                                            scanner.nextLine(); // Limpar o buffer
                                            System.out.println("Digite a nova descrição do Tipo:");
                                            String novaDescricao = scanner.nextLine();
                                            tipoAlterar.setDescricao(novaDescricao);
                                            tipoAlterar.salvar("tipos.json", Tipo.class);
                                            System.out.println("Descrição do Tipo alterada com sucesso!");
                                            break;
                                        case 3:
                                            scanner.nextLine(); // Limpar o buffer
                                            System.out.println("Digite o nome da fraqueza a ser adicionada:");
                                            String nomeFraqueza = scanner.nextLine();
                                            boolean sucesso = Tipo.adicionarFraquezaPorNome(tipoAlterar, todosTipos, nomeFraqueza);
                                            if (sucesso) {
                                                tipoAlterar.salvar("tipos.json", Tipo.class);
                                                System.out.println("Fraqueza adicionada com sucesso!");
                                            } else {
                                                System.out.println("Tipo de fraqueza não encontrado.");
                                            }
                                            break;
                                        case 4:
                                            break;
                                        default:
                                            System.out.println("Opção inválida. Tente novamente.");
                                            break;
                                        }
                            }
                            case (3):{
                                System.out.print("Digite o nome do Tipo: ");
                                scanner.nextLine(); // Consumir a nova linha
                                String nomeTipo = scanner.nextLine();
                                Tipo tipoAlterar = Tipo.buscarTipoPorNome(todosTipos, nomeTipo);
                                if (tipoAlterar != null) {
                                    System.out.println("Tipo encontrado com sucesso!");
                                } else {
                                    System.out.println("Tipo não encontrado.");
                                    break;
                                }
                                tipoAlterar.apagar("tipos.json", Tipo.class);
                                System.out.println("Tipo removido com sucesso!");
                                break;
                            }
                            case (4):{
                                System.out.print("Digite o nome do Tipo: ");
                                scanner.nextLine(); // Consumir a nova linha
                                String nomeTipo = scanner.nextLine();
                                Tipo tipoAlterar = Tipo.buscarTipoPorNome(todosTipos, nomeTipo);
                                if (tipoAlterar != null) {
                                    System.out.println("Tipo encontrado com sucesso!");
                                } else {
                                    System.out.println("Tipo não encontrado.");
                                    break;
                                }
                                // Adicionar fraqueza a um Tipo
                                scanner.nextLine(); // Limpar o buffer
                                System.out.println("Digite o nome do Tipo para adicionar a fraqueza:");
                                String nomeTipoFraqueza = scanner.nextLine();
                                tipoAlterar.adicionarFraquezas(Tipo.buscarTipoPorNome(todosTipos, nomeTipoFraqueza));
                                tipoAlterar.salvar("tipos.json", Tipo.class);
                                System.out.println("Fraqueza adicionada com sucesso!");
                                break;
                            }
                            case (5):{
                                break;
                            }
                            default:
                                System.out.println("Opção inválida. Tente novamente.");
                        }   
                    
                    break;
                case 2:
                    System.out.println("O que deseja fazer com o Pokémon?");
                    System.out.println("1. Criar Pokémon");
                    System.out.println("2. Alterar Pokémon");
                    System.out.println("3. Remover Pokémon");
                    System.out.println("4. Voltar");
                    int pokemonOpcao = scanner.nextInt();
                        switch (pokemonOpcao) {
                            case 1:
                                Pokemon pokemon = Servicos.criarPokemon(totalEntidades+1, scanner); // ID fixo para exemplo
                                break;
                            case 2:
                                System.out.print("Digite o nome do Pokémon: ");
                            scanner.nextLine(); // Consumir a nova linha
                            String nomePokemon = scanner.nextLine();
                            Pokemon pokemonAlterar = Pokemon.buscarPokemonPorNome(todosPokemons, nomePokemon);
                            if (pokemonAlterar != null) {
                                System.out.println("Pokémon encontrado!");
                            } else {
                                System.out.println("Pokémon não encontrado.");
                                break;
                            }
                                System.out.println("O que deseja alterar no Pokémon?");
                                System.out.println("1. Nome");
                                System.out.println("2. Número na Pokédex");
                                System.out.println("3. Altura");
                                System.out.println("4. Peso");
                                System.out.println("5. Stats");
                                System.out.println("6. Descrição");
                                System.out.println("7. Voltar");
                                int alterarPokemonOpcao = scanner.nextInt();
                                    switch (alterarPokemonOpcao) {
                                        case 1:
                                            System.out.println("Digite o novo nome do Pokémon:");
                                            String novoNome = scanner.nextLine();
                                            pokemonAlterar.setNome(novoNome);
                                            pokemonAlterar.salvar("pokemons.json", Pokemon.class);
                                            System.out.println("Nome do Pokémon alterado com sucesso!");
                                            break;
                                        case 2:
                                            System.out.println("Digite o novo número na Pokédex do Pokémon:");
                                            int novoNumeroPokedex = scanner.nextInt();
                                            pokemonAlterar.setNumeroPokedex(novoNumeroPokedex);
                                            pokemonAlterar.salvar("pokemons.json", Pokemon.class);
                                            System.out.println("Número na Pokédex do Pokémon alterado com sucesso!");
                                            break;
                                        case 3:
                                            System.out.println("Digite a nova altura do Pokémon:");
                                            double novaAltura = scanner.nextDouble();
                                            pokemonAlterar.setAltura(novaAltura);
                                            pokemonAlterar.salvar("pokemons.json", Pokemon.class);
                                            System.out.println("Altura do Pokémon alterada com sucesso!");
                                            break;
                                        case 4:
                                            System.out.println("Digite o novo peso do Pokémon:");
                                            double novoPeso = scanner.nextDouble();
                                            pokemonAlterar.setPeso(novoPeso);
                                            pokemonAlterar.salvar("pokemons.json", Pokemon.class);
                                            System.out.println("Peso do Pokémon alterado com sucesso!");
                                            break;
                                        case 5:
                                            System.out.println("Digite os novos stats do Pokémon:");
                                            int novosStats = scanner.nextInt();
                                            pokemonAlterar.setStats(novosStats);
                                            pokemonAlterar.salvar("pokemons.json", Pokemon.class);
                                            System.out.println("Stats do Pokémon alterados com sucesso!");
                                            break;
                                        case 6:
                                            scanner.nextLine(); // Limpar o buffer
                                            System.out.println("Digite a nova descrição do Pokémon:");
                                            String novaDescricao = scanner.nextLine();
                                            pokemonAlterar.setDescricao(novaDescricao);
                                            pokemonAlterar.salvar("pokemons.json", Pokemon.class);
                                            System.out.println("Descrição do Pokémon alterada com sucesso!");
                                            break;
                                        case 7:
                                            break;
                                        default:
                                            System.out.println("Opção inválida. Tente novamente.");
                                        break;
                                        }
                                    break;
                            case 3:
                                System.out.print("Digite o nome do Pokémon: ");
                                scanner.nextLine(); // Consumir a nova linha
                                String nomePk = scanner.nextLine();
                                Pokemon pkAlterar = Pokemon.buscarPokemonPorNome(todosPokemons, nomePk);
                                if (pkAlterar != null) {
                                    System.out.println("Pokémon encontrado!");
                                } else {
                                    System.out.println("Pokémon não encontrado.");
                                    break;
                                }
                                pkAlterar.apagar("pokemons.json", Pokemon.class);
                                System.out.println("Pokémon removido com sucesso!");
                                break;
                            case 4:
                                break;
                            default:
                                System.out.println("Opção inválida. Tente novamente.");
                        }
                    break;
                                
                case 3:
                    System.out.println("O que deseja fazer com o Treinador?");
                    System.out.println("1. Criar Treinador");
                    System.out.println("2. Alterar Treinador");
                    System.out.println("3. Remover Treinador");
                    System.out.println("4. Voltar");
                    int treinadorOpcao = scanner.nextInt();
                    
                        switch (treinadorOpcao) {
                            case 1:
                                Treinador treinador = Servicos.criarTreinador(totalEntidades + 1, scanner); // ID fixo para exemplo
                                break;
                            case 2:
                                scanner.nextLine(); // Consumir a nova linha
                                System.out.print("Digite o nome do Treinador: ");
                                String nomeTreinador = scanner.nextLine();
                                Treinador treinadorAlterar = Treinador.buscarTreinadorPorNome(todosTreinadores, nomeTreinador);
                                if (treinadorAlterar != null) {
                                    System.out.println("Treinador encontrado!");
                                } else {
                                    System.out.println("Treinador não encontrado.");
                                    break;
                                }
                                System.out.println("O que deseja alterar no Treinador?");
                                System.out.println("1. Nome");
                                System.out.println("2. Região");
                                System.out.println("3. Insígnias");
                                System.out.println("4. Voltar");
                                int alterarTreinadorOpcao = scanner.nextInt();
                                switch (alterarTreinadorOpcao) {
                                    case 1:
                                        System.out.println("Digite o novo nome do Treinador:");
                                        String novoNome = scanner.nextLine();
                                        treinadorAlterar.setNome(novoNome);
                                        treinadorAlterar.salvar("treinadores.json", Treinador.class);
                                        System.out.println("Nome do Treinador alterado com sucesso!");
                                        break;
                                    case 2:
                                        scanner.nextLine(); // Limpar o buffer
                                        System.out.println("Digite a nova região do Treinador:");
                                        String novaRegiao = scanner.nextLine();
                                        treinadorAlterar.setRegiao(novaRegiao);
                                        treinadorAlterar.salvar("treinadores.json", Treinador.class);
                                        System.out.println("Região do Treinador alterada com sucesso!");
                                        break;
                                    case 3:
                                        System.out.println("Digite o novo número de insígnias do Treinador:");
                                        int novasInsignias = scanner.nextInt();
                                        treinadorAlterar.setInsignias(novasInsignias);
                                        treinadorAlterar.salvar("treinadores.json", Treinador.class);
                                        System.out.println("Número de insígnias do Treinador alterado com sucesso!");
                                        break;
                                    case 4:
                                        break;
                                    default:
                                        System.out.println("Opção inválida. Tente novamente.");
                                }
                                break;
                            
                            case 3:
                                System.out.print("Digite o nome do Treinador: ");
                                scanner.nextLine(); // Consumir a nova linha
                                nomeTreinador = scanner.nextLine();
                                treinadorAlterar = Treinador.buscarTreinadorPorNome(todosTreinadores, nomeTreinador);
                                if (treinadorAlterar != null) {
                                    System.out.println("Treinador encontrado!");
                                } else {
                                    System.out.println("Treinador não encontrado.");
                                    break;
                                }
                                treinadorAlterar.apagar("treinadores.json", Treinador.class);
                                System.out.println("Treinador removido com sucesso!");
                                break;
                            case 4:
                                break;
                            default:
                                System.out.println("Opção inválida. Tente novamente.");
                        }
                    break;
                case 4:
                    System.out.println("Digite o nome do primeiro treinador:");
                    scanner.nextLine(); // Consumir a nova linha
                    String nomeTreinador1 = scanner.nextLine();
                    Treinador treinador1 = Treinador.buscarTreinadorPorNome(todosTreinadores, nomeTreinador1);
                    if (treinador1 != null) {
                        System.out.println("Treinador encontrado!");
                    } else {
                        System.out.println("Treinador não encontrado.");
                        break;
                    }
                    
                    System.out.println("Digite o nome do segundo treinador:");
                    String nomeTreinador2 = scanner.nextLine();
                    Treinador treinador2 = Treinador.buscarTreinadorPorNome(todosTreinadores, nomeTreinador2);
                    if (treinador2 != null) {
                        System.out.println("Treinador encontrado!");
                    } else {
                        System.out.println("Treinador não encontrado.");
                        break;
                    }
                    Treinador vencedor = Servicos.compararTreinadores(treinador1, treinador2);
                    vencedor.salvar("treinadores.json", Treinador.class);
                    break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        break;
                case 5:
                    System.out.println("Saindo do programa. Até mais!");
                    scanner.close();
                    return;
        }
        // Teste com JSON (Jackson)     
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Serializar treinador Ash para JSON
            for (Treinador t : todosTreinadores) {
                t.salvar("treinadores.json", Treinador.class);
            }
            for (Pokemon p : todosPokemons) {
                p.salvar("pokemons.json", Pokemon.class);
            }
            for (Tipo tp : todosTipos) {
                tp.salvar("tipos.json", Tipo.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 }
}