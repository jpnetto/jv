package com.gp.visual;

import com.gp.Pokemon;
import com.gp.Treinador;
import com.gp.persistencia.EntidadeDAO;
import com.gp.persistencia.PersistenceException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TreinadorPanel extends JPanel {

    private final EntidadeDAO<Treinador> treinadorDAO;
    private final EntidadeDAO<Pokemon> pokemonDAO;
    private final String arquivoTreinadores;
    private final Runnable aoMudar;

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField campoNome, campoRegiao, campoInsignias;
    private JList<Pokemon> listaPokemons;
    private DefaultListModel<Pokemon> listModelPokemons;
    private Treinador treinadorEmEdicao = null;

    public TreinadorPanel(EntidadeDAO<Treinador> treinadorDAO, EntidadeDAO<Pokemon> pokemonDAO,
                           String arquivoTreinadores, Runnable aoMudar) {
        this.treinadorDAO = treinadorDAO;
        this.pokemonDAO = pokemonDAO;
        this.arquivoTreinadores = arquivoTreinadores;
        this.aoMudar = aoMudar;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(Tema.FUNDO);

        JLabel titulo = new JLabel("Treinadores");
        titulo.setFont(Tema.FONTE_TITULO);
        add(titulo, BorderLayout.NORTH);

        JPanel corpo = new JPanel(new BorderLayout(20, 0));
        corpo.setOpaque(false);
        corpo.add(criarTabela(), BorderLayout.CENTER);
        corpo.add(criarFormulario(), BorderLayout.EAST);
        add(corpo, BorderLayout.CENTER);

        atualizar();
    }

    private JComponent criarTabela() {
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Região", "Insígnias", "Qtde. Pokémons"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(32);
        table.setFont(Tema.FONTE_NORMAL);
        table.getTableHeader().setFont(Tema.FONTE_SUB);
        table.getTableHeader().setBackground(Tema.AZUL);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(Tema.AMARELO);
        table.getColumnModel().getColumn(0).setMaxWidth(50);

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) carregarParaEdicao((int) tableModel.getValueAt(row, 0));
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(Tema.BORDA));
        return scroll;
    }

    private JComponent criarFormulario() {
        JPanel form = new JPanel();
        form.setPreferredSize(new Dimension(300, 0));
        form.setBackground(Tema.FUNDO_CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDA),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        form.add(secao("Cadastrar / Editar"));
        form.add(Box.createVerticalStrut(15));

        form.add(rotulo("Nome do treinador"));
        campoNome = new JTextField();
        estilizarCampo(campoNome);
        form.add(campoNome);
        form.add(Box.createVerticalStrut(10));

        form.add(rotulo("Região"));
        campoRegiao = new JTextField();
        estilizarCampo(campoRegiao);
        form.add(campoRegiao);
        form.add(Box.createVerticalStrut(10));

        form.add(rotulo("Insígnias"));
        campoInsignias = new JTextField();
        estilizarCampo(campoInsignias);
        form.add(campoInsignias);
        form.add(Box.createVerticalStrut(12));

        form.add(rotulo("Pokémons (Ctrl+clique para vários)"));
        listModelPokemons = new DefaultListModel<>();
        listaPokemons = new JList<>(listModelPokemons);
        listaPokemons.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaPokemons.setFont(Tema.FONTE_NORMAL);
        JScrollPane scrollLista = new JScrollPane(listaPokemons);
        scrollLista.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        scrollLista.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollLista.setBorder(BorderFactory.createLineBorder(Tema.BORDA));
        form.add(scrollLista);
        form.add(Box.createVerticalStrut(20));

        FlatButton salvar = new FlatButton("Salvar", Tema.VERMELHO, Color.WHITE);
        salvar.setAlignmentX(Component.LEFT_ALIGNMENT);
        salvar.addActionListener(e -> salvar());
        form.add(salvar);
        form.add(Box.createVerticalStrut(10));

        FlatButton limpar = new FlatButton("Limpar", Tema.AZUL, Color.WHITE);
        limpar.setAlignmentX(Component.LEFT_ALIGNMENT);
        limpar.addActionListener(e -> limparFormulario());
        form.add(limpar);
        form.add(Box.createVerticalStrut(10));

        FlatButton excluir = new FlatButton("Excluir", Tema.CINZA, Color.WHITE);
        excluir.setAlignmentX(Component.LEFT_ALIGNMENT);
        excluir.addActionListener(e -> excluir());
        form.add(excluir);

        return form;
    }

    private JLabel secao(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(Tema.FONTE_SUB);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JLabel rotulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(Tema.FONTE_NORMAL);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDA),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }

    /** Chamado ao trocar de tela: recarrega tabela e lista de pokémons disponíveis. */
    public void atualizar() {
        List<Pokemon> pokemons = GuiUtil.listar(pokemonDAO);
        listModelPokemons.clear();
        for (Pokemon p : pokemons) listModelPokemons.addElement(p);

        List<Treinador> treinadores = GuiUtil.listar(treinadorDAO);
        tableModel.setRowCount(0);
        for (Treinador t : treinadores) {
            tableModel.addRow(new Object[]{t.getId(), t.getNome(), t.getRegiao(), t.getInsignias(), t.getPokemons().size()});
        }
    }

    private void carregarParaEdicao(int id) {
        Treinador t = buscarPorId(id);
        if (t == null) return;
        treinadorEmEdicao = t;
        campoNome.setText(t.getNome());
        campoRegiao.setText(t.getRegiao());
        campoInsignias.setText(String.valueOf(t.getInsignias()));

        listaPokemons.clearSelection();
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < listModelPokemons.size(); i++) {
            if (t.getPokemons().contains(listModelPokemons.get(i))) indices.add(i);
        }
        listaPokemons.setSelectedIndices(indices.stream().mapToInt(Integer::intValue).toArray());
    }

    private Treinador buscarPorId(int id) {
        for (Treinador t : GuiUtil.listar(treinadorDAO)) if (t.getId() == id) return t;
        return null;
    }

    private void limparFormulario() {
        treinadorEmEdicao = null;
        campoNome.setText("");
        campoRegiao.setText("");
        campoInsignias.setText("");
        listaPokemons.clearSelection();
        table.clearSelection();
    }

    private void salvar() {
        String nome = campoNome.getText().trim();
        String regiao = campoRegiao.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do treinador.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int insignias;
        try {
            insignias = campoInsignias.getText().trim().isEmpty() ? 0 : Integer.parseInt(campoInsignias.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Insígnias deve ser um número.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<Pokemon> selecionados = listaPokemons.getSelectedValuesList();

        try {
            if (treinadorEmEdicao == null) {
                int novoId = proximoId();
                Treinador novo = new Treinador(novoId, nome, regiao, insignias);
                for (Pokemon p : selecionados) novo.addPokemon(p);
                treinadorDAO.salvar(novo);
            } else {
                treinadorEmEdicao.setNome(nome);
                treinadorEmEdicao.setRegiao(regiao);
                treinadorEmEdicao.setInsignias(insignias);
                for (Pokemon p : new ArrayList<>(treinadorEmEdicao.getPokemons())) {
                    treinadorEmEdicao.removerPokemon(p);
                }
                for (Pokemon p : selecionados) treinadorEmEdicao.addPokemon(p);
                treinadorDAO.atualizar(treinadorEmEdicao);
            }
            treinadorDAO.persistir(arquivoTreinadores);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar Treinador: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        atualizar();
        limparFormulario();
        if (aoMudar != null) aoMudar.run();
    }

    private void excluir() {
        if (treinadorEmEdicao == null) {
            JOptionPane.showMessageDialog(this, "Selecione um treinador na tabela para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            treinadorDAO.apagar(treinadorEmEdicao.getId());
            treinadorDAO.persistir(arquivoTreinadores);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir Treinador: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        atualizar();
        limparFormulario();
        if (aoMudar != null) aoMudar.run();
    }

    private int proximoId() {
        int max = 0;
        for (Treinador t : GuiUtil.listar(treinadorDAO)) max = Math.max(max, t.getId());
        return max + 1;
    }
}
