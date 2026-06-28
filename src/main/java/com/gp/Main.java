package com.gp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.gp.persistencia.DAOFactory;
import com.gp.persistencia.EntidadeDAO;
import com.gp.persistencia.PersistenceException;

public class Main {

    // DAOs únicos por entidade, obtidos via DAOFactory (garante uma única instância por tipo)
    private static final EntidadeDAO<Tipo> tipoDAO = DAOFactory.getDAO(Tipo.class);
    private static final EntidadeDAO<Pokemon> pokemonDAO = DAOFactory.getDAO(Pokemon.class);
    private static final EntidadeDAO<Treinador> treinadorDAO = DAOFactory.getDAO(Treinador.class);
    private static final EntidadeDAO<Batalha> batalhaDAO = DAOFactory.getDAO(Batalha.class);

    private static final String ARQUIVO_TIPOS = "tipos.json";
    private static final String ARQUIVO_POKEMONS = "pokemons.json";
    private static final String ARQUIVO_TREINADORES = "treinadores.json";
    private static final String ARQUIVO_BATALHAS = "batalhas.json";

    // Converte o array retornado por carregarTodos() em List, tratando o caso de conjunto vazio
    private static <E extends Entidade> List<E> listar(EntidadeDAO<E> dao) {
        if (dao.size() == 0) {
            return new ArrayList<>();
        }
        try {
            return new ArrayList<>(Arrays.asList(dao.carregarTodos()));
        } catch (PersistenceException e) {
            // Não deveria acontecer, já que checamos size() == 0 antes
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {

        // Carrega o estado salvo em disco (se existir) para dentro de cada DAO
        try {
            tipoDAO.recuperar(ARQUIVO_TIPOS);
            pokemonDAO.recuperar(ARQUIVO_POKEMONS);
            treinadorDAO.recuperar(ARQUIVO_TREINADORES);
            batalhaDAO.recuperar(ARQUIVO_BATALHAS);
        } catch (PersistenceException e) {
            System.out.println("Erro ao carregar dados salvos: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            int totalEntidades = tipoDAO.size() + pokemonDAO.size() + treinadorDAO.size() + batalhaDAO.size();
            List<Tipo> todosTipos = listar(tipoDAO);
            List<Pokemon> todosPokemons = listar(pokemonDAO);
            List<Treinador> todosTreinadores = listar(treinadorDAO);

            System.out.println("O que deseja fazer?");
            System.out.println("1. Alterar/remover/criar Tipo:");
            System.out.println("2. Alterar/remover/criar Pokémon:");
            System.out.println("3. Alterar/remover/criar Treinador");
            System.out.println("4. Criar uma batalha entre dois treinadores");
            System.out.println("5. Sair");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    System.out.println("Tipos disponíveis:");
                    for (Tipo tipo : todosTipos) {
                        System.out.println("- " + tipo.getNome());
                    }
                    System.out.println("O que deseja fazer?");
                    System.out.println("1. Criar Tipo");
                    System.out.println("2. Alterar Tipo");
                    System.out.println("3. Remover Tipo");
                    System.out.println("6. Voltar");
                    int tipoOpcao = scanner.nextInt();
                    scanner.nextLine(); // Consumir a nova linha
                    switch (tipoOpcao) {
                        case 1: {
                            Tipo novoTipo = Tipo.criarTipo(totalEntidades + 1, scanner);
                            try {
                                tipoDAO.salvar(novoTipo);
                                tipoDAO.persistir(ARQUIVO_TIPOS);
                                System.out.println("Tipo criado e salvo com sucesso!");
                            } catch (PersistenceException e) {
                                System.out.println("Erro ao salvar Tipo: " + e.getMessage());
                            }
                            break;
                        }
                        case 2: {
                            System.out.print("Digite o nome do tipo que deseja alterar: ");
                            String nomeTipo = scanner.nextLine();
                            Tipo tipoAlterar = Tipo.buscarTipoPorNome(todosTipos, nomeTipo);
                            if (tipoAlterar != null) {
                                System.out.println("Tipo encontrado com sucesso!");
                            } else {
                                System.out.println("Tipo não encontrado.");
                                break;
                            }
                            System.out.println("O que deseja alterar no Tipo " + tipoAlterar.getNome() + "?");
                            System.out.println("1. Nome");
                            System.out.println("2. Descrição");
                            System.out.println("3. Fraquezas");
                            System.out.println("4. Voltar");
                            int alterarTipoOpcao = scanner.nextInt();
                            switch (alterarTipoOpcao) {
                                case 1:
                                    scanner.nextLine();
                                    System.out.println("Digite o novo nome do Tipo:");
                                    String novoNome = scanner.nextLine();
                                    tipoAlterar.setNome(novoNome);
                                    try {
                                        tipoDAO.atualizar(tipoAlterar);
                                        tipoDAO.persistir(ARQUIVO_TIPOS);
                                        System.out.println("Nome do Tipo alterado com sucesso!");
                                    } catch (PersistenceException e) {
                                        System.out.println("Erro ao atualizar Tipo: " + e.getMessage());
                                    }
                                    break;
                                case 2:
                                    scanner.nextLine(); // Limpar o buffer
                                    System.out.println("Digite a nova descrição do Tipo:");
                                    String novaDescricao = scanner.nextLine();
                                    tipoAlterar.setDescricao(novaDescricao);
                                    try {
                                        tipoDAO.atualizar(tipoAlterar);
                                        tipoDAO.persistir(ARQUIVO_TIPOS);
                                        System.out.println("Descrição do Tipo alterada com sucesso!");
                                    } catch (PersistenceException e) {
                                        System.out.println("Erro ao atualizar Tipo: " + e.getMessage());
                                    }
                                    break;
                                case 3:
                                    System.out.println("O que deseja fazer com as fraquezas do Tipo " + tipoAlterar.getNome() + "?");
                                    System.out.println("1. Adicionar fraqueza");
                                    System.out.println("2. Remover fraqueza");
                                    int fraquezaTipoOpcao = scanner.nextInt();
                                    switch (fraquezaTipoOpcao) {
                                        case 1:
                                            scanner.nextLine(); // Limpar o buffer
                                            System.out.println("Digite o nome da fraqueza a ser adicionada:");
                                            String nomeFraqueza = scanner.nextLine();
                                            Tipo tipoFraqueza = Tipo.buscarTipoPorNome(todosTipos, nomeFraqueza);
                                            if (tipoFraqueza != null) {
                                                tipoAlterar.adicionarFraquezas(tipoFraqueza);
                                                try {
                                                    tipoDAO.atualizar(tipoAlterar);
                                                    tipoDAO.persistir(ARQUIVO_TIPOS);
                                                    System.out.println("Fraqueza adicionada com sucesso!");
                                                } catch (PersistenceException e) {
                                                    System.out.println("Erro ao atualizar Tipo: " + e.getMessage());
                                                }
                                            } else {
                                                System.out.println("Tipo de fraqueza não encontrado.");
                                            }
                                            break;
                                        case 2:
                                            scanner.nextLine(); // Limpar o buffer
                                            System.out.println("Digite o nome da fraqueza a ser removida:");
                                            String nomeFraquezaRemover = scanner.nextLine();
                                            Tipo tipoFraquezaRemover = Tipo.buscarTipoPorNome(todosTipos, nomeFraquezaRemover);
                                            if (tipoFraquezaRemover != null) {
                                                tipoAlterar.removerFraquezas(tipoFraquezaRemover);
                                                try {
                                                    tipoDAO.atualizar(tipoAlterar);
                                                    tipoDAO.persistir(ARQUIVO_TIPOS);
                                                    System.out.println("Fraqueza removida com sucesso!");
                                                } catch (PersistenceException e) {
                                                    System.out.println("Erro ao atualizar Tipo: " + e.getMessage());
                                                }
                                            } else {
                                                System.out.println("Tipo de fraqueza não encontrado.");
                                            }
                                            break;
                                        default:
                                            System.out.println("Opção inválida. Tente novamente.");
                                            break;
                                    }
                                    break;
                                case 4:
                                    break;
                                default:
                                    System.out.println("Opção inválida. Tente novamente.");
                                    break;
                            }
                            break;
                        }
                        case 3: {
                            System.out.print("Digite o nome do Tipo: ");
                            String nomeTipo = scanner.nextLine();
                            Tipo tipoRemover = Tipo.buscarTipoPorNome(todosTipos, nomeTipo);
                            if (tipoRemover != null) {
                                System.out.println("Tipo encontrado com sucesso!");
                                try {
                                    tipoDAO.apagar(tipoRemover.getId());
                                    tipoDAO.persistir(ARQUIVO_TIPOS);
                                    System.out.println("Tipo removido com sucesso!");
                                } catch (PersistenceException e) {
                                    System.out.println("Erro ao remover Tipo: " + e.getMessage());
                                }
                            } else {
                                System.out.println("Tipo não encontrado.");
                            }
                            break;
                        }
                        case 6:
                            break;
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
                    scanner.nextLine(); // Consumir a nova linha
                    switch (pokemonOpcao) {
                        case 1: {
                            Pokemon novoPokemon = Pokemon.criarPokemon(totalEntidades + 1, scanner);
                            try {
                                pokemonDAO.salvar(novoPokemon);
                                pokemonDAO.persistir(ARQUIVO_POKEMONS);
                                System.out.println("Pokémon criado e salvo com sucesso!");
                            } catch (PersistenceException e) {
                                System.out.println("Erro ao salvar Pokémon: " + e.getMessage());
                            }
                            break;
                        }
                        case 2: {
                            System.out.print("Digite o nome do Pokémon: ");
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
                            System.out.println("7. Tipos");
                            System.out.println("8. Voltar");
                            int alterarPokemonOpcao = scanner.nextInt();
                            switch (alterarPokemonOpcao) {
                                case 1:
                                    scanner.nextLine();
                                    System.out.println("Digite o novo nome do Pokémon:");
                                    String novoNome = scanner.nextLine();
                                    pokemonAlterar.setNome(novoNome);
                                    atualizarPokemon(pokemonAlterar, "Nome do Pokémon alterado com sucesso!");
                                    break;
                                case 2:
                                    System.out.println("Digite o novo número na Pokédex do Pokémon:");
                                    int novoNumeroPokedex = scanner.nextInt();
                                    pokemonAlterar.setNumeroPokedex(novoNumeroPokedex);
                                    atualizarPokemon(pokemonAlterar, "Número na Pokédex do Pokémon alterado com sucesso!");
                                    break;
                                case 3:
                                    System.out.println("Digite a nova altura do Pokémon:");
                                    double novaAltura = scanner.nextDouble();
                                    pokemonAlterar.setAltura(novaAltura);
                                    atualizarPokemon(pokemonAlterar, "Altura do Pokémon alterada com sucesso!");
                                    break;
                                case 4:
                                    System.out.println("Digite o novo peso do Pokémon:");
                                    double novoPeso = scanner.nextDouble();
                                    pokemonAlterar.setPeso(novoPeso);
                                    atualizarPokemon(pokemonAlterar, "Peso do Pokémon alterado com sucesso!");
                                    break;
                                case 5:
                                    System.out.println("Digite os novos stats do Pokémon:");
                                    int novosStats = scanner.nextInt();
                                    pokemonAlterar.setStats(novosStats);
                                    atualizarPokemon(pokemonAlterar, "Stats do Pokémon alterados com sucesso!");
                                    break;
                                case 6:
                                    scanner.nextLine(); // Limpar o buffer
                                    System.out.println("Digite a nova descrição do Pokémon:");
                                    String novaDescricao = scanner.nextLine();
                                    pokemonAlterar.setDescricao(novaDescricao);
                                    atualizarPokemon(pokemonAlterar, "Descrição do Pokémon alterada com sucesso!");
                                    break;
                                case 7:
                                    scanner.nextLine(); // Limpar o buffer
                                    System.out.println("O que deseja alterar nos tipos do Pokémon?");
                                    System.out.println("1. Adicionar tipo");
                                    System.out.println("2. Remover tipo");
                                    int tipoPokemonOpcao = scanner.nextInt();
                                    switch (tipoPokemonOpcao) {
                                        case 1:
                                            scanner.nextLine(); // Limpar o buffer
                                            System.out.println("Digite o nome do Tipo para adicionar ao Pokémon:");
                                            String nomeTipoAdicionar = scanner.nextLine();
                                            Tipo tipoAdicionar = Tipo.buscarTipoPorNome(todosTipos, nomeTipoAdicionar);
                                            if (tipoAdicionar != null) {
                                                pokemonAlterar.adicionarTipo(tipoAdicionar);
                                                atualizarPokemon(pokemonAlterar, "Tipo adicionado ao Pokémon com sucesso!");
                                            } else {
                                                System.out.println("Tipo não encontrado.");
                                            }
                                            break;
                                        case 2:
                                            scanner.nextLine(); // Limpar o buffer
                                            System.out.println("Digite o nome do Tipo para remover do Pokémon:");
                                            String nomeTipoRemover = scanner.nextLine();
                                            Tipo tipoRemover = Tipo.buscarTipoPorNome(todosTipos, nomeTipoRemover);
                                            if (tipoRemover != null) {
                                                pokemonAlterar.removerTipo(tipoRemover);
                                                atualizarPokemon(pokemonAlterar, "Tipo removido do Pokémon com sucesso!");
                                            } else {
                                                System.out.println("Tipo não encontrado.");
                                            }
                                            break;
                                        default:
                                            System.out.println("Opção inválida. Tente novamente.");
                                            break;
                                    }
                                    break;
                                case 8:
                                    break;
                                default:
                                    System.out.println("Opção inválida. Tente novamente.");
                                    break;
                            }
                            break;
                        }
                        case 3: {
                            System.out.print("Digite o nome do Pokémon: ");
                            String nomePk = scanner.nextLine();
                            Pokemon pkAlterar = Pokemon.buscarPokemonPorNome(todosPokemons, nomePk);
                            if (pkAlterar != null) {
                                System.out.println("Pokémon encontrado!");
                                try {
                                    pokemonDAO.apagar(pkAlterar.getId());
                                    pokemonDAO.persistir(ARQUIVO_POKEMONS);
                                    System.out.println("Pokémon removido com sucesso!");
                                } catch (PersistenceException e) {
                                    System.out.println("Erro ao remover Pokémon: " + e.getMessage());
                                }
                            } else {
                                System.out.println("Pokémon não encontrado.");
                            }
                            break;
                        }
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
                    scanner.nextLine(); // Consumir a nova linha
                    switch (treinadorOpcao) {
                        case 1: {
                            Treinador novoTreinador = Treinador.criarTreinador(totalEntidades + 1, scanner);
                            try {
                                treinadorDAO.salvar(novoTreinador);
                                treinadorDAO.persistir(ARQUIVO_TREINADORES);
                                System.out.println("Treinador criado e salvo com sucesso!");
                            } catch (PersistenceException e) {
                                System.out.println("Erro ao salvar Treinador: " + e.getMessage());
                            }
                            break;
                        }
                        case 2: {
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
                            System.out.println("4. Pokémons");
                            System.out.println("5. Voltar");
                            int alterarTreinadorOpcao = scanner.nextInt();
                            switch (alterarTreinadorOpcao) {
                                case 1:
                                    scanner.nextLine();
                                    System.out.println("Digite o novo nome do Treinador:");
                                    String novoNome = scanner.nextLine();
                                    treinadorAlterar.setNome(novoNome);
                                    atualizarTreinador(treinadorAlterar, "Nome do Treinador alterado com sucesso!");
                                    break;
                                case 2:
                                    scanner.nextLine(); // Limpar o buffer
                                    System.out.println("Digite a nova região do Treinador:");
                                    String novaRegiao = scanner.nextLine();
                                    treinadorAlterar.setRegiao(novaRegiao);
                                    atualizarTreinador(treinadorAlterar, "Região do Treinador alterada com sucesso!");
                                    break;
                                case 3:
                                    System.out.println("Digite o novo número de insígnias do Treinador:");
                                    int novasInsignias = scanner.nextInt();
                                    treinadorAlterar.setInsignias(novasInsignias);
                                    atualizarTreinador(treinadorAlterar, "Número de insígnias do Treinador alterado com sucesso!");
                                    break;
                                case 4:
                                    System.out.println("O que deseja fazer com os Pokémons do Treinador?");
                                    System.out.println("1. Adicionar Pokémon");
                                    System.out.println("2. Remover Pokémon");
                                    int pokemonTreinadorOpcao = scanner.nextInt();
                                    switch (pokemonTreinadorOpcao) {
                                        case 1:
                                            scanner.nextLine(); // Limpar o buffer
                                            System.out.println("Digite o nome do Pokémon para adicionar ao Treinador:");
                                            String nomePokemonAdicionar = scanner.nextLine();
                                            Pokemon pokemonAdicionar = Pokemon.buscarPokemonPorNome(todosPokemons, nomePokemonAdicionar);
                                            if (pokemonAdicionar != null) {
                                                treinadorAlterar.addPokemon(pokemonAdicionar);
                                                atualizarTreinador(treinadorAlterar, "Pokémon adicionado ao Treinador com sucesso!");
                                            } else {
                                                System.out.println("Pokémon não encontrado.");
                                            }
                                            break;
                                        case 2:
                                            scanner.nextLine(); // Limpar o buffer
                                            System.out.println("Digite o nome do Pokémon para remover do Treinador:");
                                            String nomePokemonRemover = scanner.nextLine();
                                            Pokemon pokemonRemover = Pokemon.buscarPokemonPorNome(todosPokemons, nomePokemonRemover);
                                            if (pokemonRemover != null) {
                                                treinadorAlterar.removerPokemon(pokemonRemover);
                                                atualizarTreinador(treinadorAlterar, "Pokémon removido do Treinador com sucesso!");
                                            } else {
                                                System.out.println("Pokémon não encontrado.");
                                            }
                                            break;
                                        default:
                                            System.out.println("Opção inválida. Tente novamente.");
                                            break;
                                    }
                                    break;
                                case 5:
                                    break;
                                default:
                                    System.out.println("Opção inválida. Tente novamente.");
                            }
                            break;
                        }
                        case 3: {
                            System.out.print("Digite o nome do Treinador: ");
                            String nomeTreinador = scanner.nextLine();
                            Treinador treinadorRemover = Treinador.buscarTreinadorPorNome(todosTreinadores, nomeTreinador);
                            if (treinadorRemover != null) {
                                System.out.println("Treinador encontrado!");
                                try {
                                    treinadorDAO.apagar(treinadorRemover.getId());
                                    treinadorDAO.persistir(ARQUIVO_TREINADORES);
                                    System.out.println("Treinador removido com sucesso!");
                                } catch (PersistenceException e) {
                                    System.out.println("Erro ao remover Treinador: " + e.getMessage());
                                }
                            } else {
                                System.out.println("Treinador não encontrado.");
                            }
                            break;
                        }
                        case 4:
                            break;
                        default:
                            System.out.println("Opção inválida. Tente novamente.");
                    }
                    break;

                case 4: {
                    System.out.println("Digite o nome do primeiro treinador:");
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
                    Batalha batalha = Servicos.realizarBatalha(totalEntidades + 1, treinador1, treinador2);
                    try {
                        batalhaDAO.salvar(batalha);
                        batalhaDAO.persistir(ARQUIVO_BATALHAS);
                        treinadorDAO.atualizar(batalha.getVencedor());
                        treinadorDAO.persistir(ARQUIVO_TREINADORES);
                        System.out.println("Resultado da batalha salvo com sucesso!");
                    } catch (PersistenceException e) {
                        System.out.println("Erro ao salvar resultado da batalha: " + e.getMessage());
                    }
                    break;
                }

                case 5:
                    System.out.println("Saindo do programa. Até mais!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }

    // Atualiza um Pokémon existente no DAO e persiste imediatamente no arquivo
    private static void atualizarPokemon(Pokemon pokemon, String mensagemSucesso) {
        try {
            pokemonDAO.atualizar(pokemon);
            pokemonDAO.persistir(ARQUIVO_POKEMONS);
            System.out.println(mensagemSucesso);
        } catch (PersistenceException e) {
            System.out.println("Erro ao atualizar Pokémon: " + e.getMessage());
        }
    }

    // Atualiza um Treinador existente no DAO e persiste imediatamente no arquivo
    private static void atualizarTreinador(Treinador treinador, String mensagemSucesso) {
        try {
            treinadorDAO.atualizar(treinador);
            treinadorDAO.persistir(ARQUIVO_TREINADORES);
            System.out.println(mensagemSucesso);
        } catch (PersistenceException e) {
            System.out.println("Erro ao atualizar Treinador: " + e.getMessage());
        }
    }
}