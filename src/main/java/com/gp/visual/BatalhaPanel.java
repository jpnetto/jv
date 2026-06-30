package com.gp.visual;

import com.gp.Batalha;
import com.gp.ItemBatalha;
import com.gp.Servicos;
import com.gp.Treinador;
import com.gp.persistencia.EntidadeDAO;
import com.gp.persistencia.PersistenceException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BatalhaPanel extends JPanel {

    private final EntidadeDAO<Batalha> batalhaDAO;
    private final EntidadeDAO<Treinador> treinadorDAO;
    private final String arquivoBatalhas;
    private final String arquivoTreinadores;
    private final Runnable aoMudar;

    private JComboBox<Treinador> comboA, comboB;
    private JTextArea logArea;

    public BatalhaPanel(EntidadeDAO<Batalha> batalhaDAO, EntidadeDAO<Treinador> treinadorDAO,
                         String arquivoBatalhas, String arquivoTreinadores, Runnable aoMudar) {
        this.batalhaDAO = batalhaDAO;
        this.treinadorDAO = treinadorDAO;
        this.arquivoBatalhas = arquivoBatalhas;
        this.arquivoTreinadores = arquivoTreinadores;
        this.aoMudar = aoMudar;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(Tema.FUNDO);

        JLabel titulo = new JLabel("Arena de Batalha");
        titulo.setFont(Tema.FONTE_TITULO);
        add(titulo, BorderLayout.NORTH);

        JPanel conteudo = new JPanel(new BorderLayout(20, 20));
        conteudo.setOpaque(false);
        conteudo.add(criarSelecao(), BorderLayout.NORTH);
        conteudo.add(criarLog(), BorderLayout.CENTER);
        add(conteudo, BorderLayout.CENTER);

        atualizar();
    }

    private JComponent criarSelecao() {
        JPanel linha = new JPanel(new GridLayout(1, 3, 20, 0));
        linha.setOpaque(false);

        JPanel ladoA = new JPanel(new BorderLayout(8, 0));
        ladoA.setOpaque(false);
        ladoA.add(new JLabel("Treinador 1"), BorderLayout.NORTH);
        comboA = new JComboBox<>();
        ladoA.add(comboA, BorderLayout.CENTER);

        JLabel vsLabel = new JLabel("VS", SwingConstants.CENTER);
        vsLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        vsLabel.setForeground(Tema.VERMELHO);

        JPanel ladoB = new JPanel(new BorderLayout(8, 0));
        ladoB.setOpaque(false);
        ladoB.add(new JLabel("Treinador 2"), BorderLayout.NORTH);
        comboB = new JComboBox<>();
        ladoB.add(comboB, BorderLayout.CENTER);

        linha.add(ladoA);
        linha.add(vsLabel);
        linha.add(ladoB);

        JPanel container = new JPanel(new BorderLayout(0, 15));
        container.setOpaque(false);
        container.add(linha, BorderLayout.NORTH);

        FlatButton iniciar = new FlatButton("Iniciar Batalha", Tema.VERMELHO, Color.WHITE);
        JPanel botaoWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoWrap.setOpaque(false);
        botaoWrap.add(iniciar);
        iniciar.addActionListener(e -> iniciarBatalha());
        container.add(botaoWrap, BorderLayout.SOUTH);

        return container;
    }

    private JComponent criarLog() {
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        logArea.setBackground(new Color(0x1E, 0x1E, 0x2E));
        logArea.setForeground(Tema.AMARELO);
        logArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(BorderFactory.createLineBorder(Tema.BORDA));
        return scroll;
    }

    /** Chamado ao trocar de tela: recarrega lista de treinadores disponíveis. */
    public void atualizar() {
        List<Treinador> treinadores = GuiUtil.listar(treinadorDAO);
        Treinador selA = (Treinador) comboA.getSelectedItem();
        Treinador selB = (Treinador) comboB.getSelectedItem();

        comboA.setModel(new DefaultComboBoxModel<>(treinadores.toArray(new Treinador[0])));
        comboB.setModel(new DefaultComboBoxModel<>(treinadores.toArray(new Treinador[0])));

        if (selA != null) comboA.setSelectedItem(selA);
        if (selB != null) comboB.setSelectedItem(selB);
    }

    private void iniciarBatalha() {
        Treinador t1 = (Treinador) comboA.getSelectedItem();
        Treinador t2 = (Treinador) comboB.getSelectedItem();

        if (t1 == null || t2 == null) {
            JOptionPane.showMessageDialog(this, "Cadastre ao menos dois treinadores.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (t1.getId() == t2.getId()) {
            JOptionPane.showMessageDialog(this, "Escolha treinadores diferentes.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (t1.getPokemons().isEmpty() || t2.getPokemons().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ambos os treinadores precisam ter ao menos 1 Pokémon.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int novoId = proximoIdBatalha();
        Batalha batalha = Servicos.realizarBatalha(novoId, t1, t2);

        try {
            batalhaDAO.salvar(batalha);
            batalhaDAO.persistir(arquivoBatalhas);
            treinadorDAO.atualizar(batalha.getVencedor());
            treinadorDAO.persistir(arquivoTreinadores);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar resultado da batalha: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }

        exibirLog(batalha);
        atualizar();
        if (aoMudar != null) aoMudar.run();
    }

    private void exibirLog(Batalha batalha) {
        StringBuilder sb = new StringBuilder();
        sb.append("⚔ Batalha: ").append(batalha.getTreinador1().getNome())
          .append(" VS ").append(batalha.getTreinador2().getNome()).append("\n\n");

        for (ItemBatalha item : batalha.getConfrontos()) {
            String vencedorNome = (item.getVencedor() != null) ? item.getVencedor().getNome() : "empate";
            sb.append("• ").append(item.getPokemon1().getNome())
              .append(" vs ").append(item.getPokemon2().getNome())
              .append(" → vencedor: ").append(vencedorNome).append("\n");
        }

        sb.append("\n🏆 Treinador vencedor: ").append(batalha.getVencedor().getNome())
          .append(" (insígnias: ").append(batalha.getVencedor().getInsignias()).append(")\n");

        logArea.setText(sb.toString());
        logArea.setCaretPosition(0);
    }

    private int proximoIdBatalha() {
        int max = 0;
        for (Batalha b : GuiUtil.listar(batalhaDAO)) max = Math.max(max, b.getId());
        return max + 1;
    }
}
