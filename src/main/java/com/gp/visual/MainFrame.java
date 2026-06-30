package com.gp.visual;

import com.gp.*;
import com.gp.persistencia.DAOFactory;
import com.gp.persistencia.EntidadeDAO;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private static final String ARQUIVO_TIPOS = "tipos.json";
    private static final String ARQUIVO_POKEMONS = "pokemons.json";
    private static final String ARQUIVO_TREINADORES = "treinadores.json";
    private static final String ARQUIVO_BATALHAS = "batalhas.json";

    private final EntidadeDAO<Tipo> tipoDAO = DAOFactory.getDAO(Tipo.class);
    private final EntidadeDAO<Pokemon> pokemonDAO = DAOFactory.getDAO(Pokemon.class);
    private final EntidadeDAO<Treinador> treinadorDAO = DAOFactory.getDAO(Treinador.class);
    private final EntidadeDAO<Batalha> batalhaDAO = DAOFactory.getDAO(Batalha.class);

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel painelConteudo = new JPanel(cardLayout);

    private TipoPanel tipoPanel;
    private PokemonPanel pokemonPanel;
    private TreinadorPanel treinadorPanel;
    private BatalhaPanel batalhaPanel;

    public MainFrame() {
        super("Pokecentro — Tipos, Pokémons, Treinadores e Batalhas");

        carregarDados();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);

        Sidebar sidebar = new Sidebar(this::trocarTela);

        // Cada painel recebe um "aoMudar" para avisar os demais quando algo for
        // criado/editado/excluído (ex.: novo Tipo deve aparecer no combo de Pokémon).
        tipoPanel = new TipoPanel(tipoDAO, ARQUIVO_TIPOS, this::atualizarTodos);
        pokemonPanel = new PokemonPanel(pokemonDAO, tipoDAO, ARQUIVO_POKEMONS, this::atualizarTodos);
        treinadorPanel = new TreinadorPanel(treinadorDAO, pokemonDAO, ARQUIVO_TREINADORES, this::atualizarTodos);
        batalhaPanel = new BatalhaPanel(batalhaDAO, treinadorDAO, ARQUIVO_BATALHAS, ARQUIVO_TREINADORES, this::atualizarTodos);

        painelConteudo.add(tipoPanel, "TIPOS");
        painelConteudo.add(pokemonPanel, "POKEMONS");
        painelConteudo.add(treinadorPanel, "TREINADORES");
        painelConteudo.add(batalhaPanel, "BATALHA");

        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);
        add(painelConteudo, BorderLayout.CENTER);

        cardLayout.show(painelConteudo, "TIPOS");
    }

    private void carregarDados() {
        try {
            tipoDAO.recuperar(ARQUIVO_TIPOS);
            pokemonDAO.recuperar(ARQUIVO_POKEMONS);
            treinadorDAO.recuperar(ARQUIVO_TREINADORES);
            batalhaDAO.recuperar(ARQUIVO_BATALHAS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados salvos: " + e.getMessage(),
                    "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Atualiza todos os painéis (tabelas e combos dependentes entre si). */
    private void atualizarTodos() {
        tipoPanel.atualizar();
        pokemonPanel.atualizar();
        treinadorPanel.atualizar();
        batalhaPanel.atualizar();
    }

    private void trocarTela(String chave) {
        atualizarTodos();
        cardLayout.show(painelConteudo, chave);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
