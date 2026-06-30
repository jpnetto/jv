package com.gp.visual;

import com.gp.Pokemon;
import com.gp.Tipo;
import com.gp.persistencia.EntidadeDAO;
import com.gp.persistencia.PersistenceException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PokemonPanel extends JPanel {

    private final EntidadeDAO<Pokemon> pokemonDAO;
    private final EntidadeDAO<Tipo> tipoDAO;
    private final String arquivoPokemons;
    private final Runnable aoMudar;

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField campoNome, campoPokedex, campoAltura, campoPeso, campoStats;
    private JTextArea campoDescricao;
    private JList<Tipo> listaTipos;
    private DefaultListModel<Tipo> listModelTipos;
    private Pokemon pokemonEmEdicao = null;

    public PokemonPanel(EntidadeDAO<Pokemon> pokemonDAO, EntidadeDAO<Tipo> tipoDAO,
                         String arquivoPokemons, Runnable aoMudar) {
        this.pokemonDAO = pokemonDAO;
        this.tipoDAO = tipoDAO;
        this.arquivoPokemons = arquivoPokemons;
        this.aoMudar = aoMudar;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(Tema.FUNDO);

        JLabel titulo = new JLabel("Pokémons");
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
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Pokédex", "Tipos", "Stats"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(32);
        table.setAutoCreateRowSorter(true); // permite ordenar clicando no cabeçalho de qualquer coluna
        table.setFont(Tema.FONTE_NORMAL);
        table.getTableHeader().setFont(Tema.FONTE_SUB);
        table.getTableHeader().setBackground(Tema.AZUL);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(Tema.AMARELO);
        table.getColumnModel().getColumn(0).setMaxWidth(50);

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int viewRow = table.getSelectedRow();
                if (viewRow >= 0) {
                    int modelRow = table.convertRowIndexToModel(viewRow); // necessário por causa da ordenação
                    carregarParaEdicao((int) tableModel.getValueAt(modelRow, 0));
                }
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

        JScrollPane scrollForm = new JScrollPane(form);
        scrollForm.setBorder(BorderFactory.createLineBorder(Tema.BORDA));
        scrollForm.setPreferredSize(new Dimension(320, 0));

        form.add(secao("Cadastrar / Editar"));
        form.add(Box.createVerticalStrut(15));

        form.add(rotulo("Nome"));
        campoNome = new JTextField();
        estilizarCampo(campoNome);
        form.add(campoNome);
        form.add(Box.createVerticalStrut(10));

        form.add(rotulo("Número na Pokédex"));
        campoPokedex = new JTextField();
        estilizarCampo(campoPokedex);
        form.add(campoPokedex);
        form.add(Box.createVerticalStrut(10));

        form.add(rotulo("Altura (m)"));
        campoAltura = new JTextField();
        estilizarCampo(campoAltura);
        form.add(campoAltura);
        form.add(Box.createVerticalStrut(10));

        form.add(rotulo("Peso (kg)"));
        campoPeso = new JTextField();
        estilizarCampo(campoPeso);
        form.add(campoPeso);
        form.add(Box.createVerticalStrut(10));

        form.add(rotulo("Stats"));
        campoStats = new JTextField();
        estilizarCampo(campoStats);
        form.add(campoStats);
        form.add(Box.createVerticalStrut(10));

        form.add(rotulo("Descrição"));
        campoDescricao = new JTextArea(3, 0);
        campoDescricao.setLineWrap(true);
        campoDescricao.setWrapStyleWord(true);
        campoDescricao.setFont(Tema.FONTE_NORMAL);
        campoDescricao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDA),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        JScrollPane scrollDesc = new JScrollPane(campoDescricao);
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(scrollDesc);
        form.add(Box.createVerticalStrut(10));

        form.add(rotulo("Tipos (Ctrl+clique para vários)"));
        listModelTipos = new DefaultListModel<>();
        listaTipos = new JList<>(listModelTipos);
        listaTipos.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaTipos.setFont(Tema.FONTE_NORMAL);
        JScrollPane scrollLista = new JScrollPane(listaTipos);
        scrollLista.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
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

        return scrollForm;
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

    /** Chamado ao trocar de tela: recarrega tabela e lista de tipos disponíveis. */
    public void atualizar() {
        List<Tipo> tipos = GuiUtil.listar(tipoDAO);
        listModelTipos.clear();
        for (Tipo t : tipos) listModelTipos.addElement(t);

        List<Pokemon> pokemons = GuiUtil.listar(pokemonDAO);
        tableModel.setRowCount(0);
        for (Pokemon p : pokemons) {
            StringBuilder nomesTipos = new StringBuilder();
            for (Tipo t : p.getTipos()) {
                if (nomesTipos.length() > 0) nomesTipos.append(", ");
                nomesTipos.append(t.getNome());
            }
            tableModel.addRow(new Object[]{p.getId(), p.getNome(), p.getNumeroPokedex(), nomesTipos.toString(), p.getStats()});
        }
    }

    private void carregarParaEdicao(int id) {
        Pokemon p = buscarPorId(id);
        if (p == null) return;
        pokemonEmEdicao = p;
        campoNome.setText(p.getNome());
        campoPokedex.setText(String.valueOf(p.getNumeroPokedex()));
        campoAltura.setText(String.valueOf(p.getAltura()));
        campoPeso.setText(String.valueOf(p.getPeso()));
        campoStats.setText(String.valueOf(p.getStats()));
        campoDescricao.setText(p.getDescricao());

        listaTipos.clearSelection();
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < listModelTipos.size(); i++) {
            if (p.getTipos().contains(listModelTipos.get(i))) indices.add(i);
        }
        listaTipos.setSelectedIndices(indices.stream().mapToInt(Integer::intValue).toArray());
    }

    private Pokemon buscarPorId(int id) {
        for (Pokemon p : GuiUtil.listar(pokemonDAO)) if (p.getId() == id) return p;
        return null;
    }

    private void limparFormulario() {
        pokemonEmEdicao = null;
        campoNome.setText("");
        campoPokedex.setText("");
        campoAltura.setText("");
        campoPeso.setText("");
        campoStats.setText("");
        campoDescricao.setText("");
        listaTipos.clearSelection();
        table.clearSelection();
    }

    private void salvar() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do Pokémon.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int numeroPokedex, stats;
        double altura, peso;
        try {
            numeroPokedex = Integer.parseInt(campoPokedex.getText().trim());
            altura = Double.parseDouble(campoAltura.getText().trim().replace(",", "."));
            peso = Double.parseDouble(campoPeso.getText().trim().replace(",", "."));
            stats = Integer.parseInt(campoStats.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Pokédex, Altura, Peso e Stats devem ser números válidos.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String descricao = campoDescricao.getText().trim();
        List<Tipo> tiposSelecionados = listaTipos.getSelectedValuesList();

        try {
            if (pokemonEmEdicao == null) {
                int novoId = proximoId();
                Pokemon novo = new Pokemon(novoId, nome, numeroPokedex, altura, peso, stats, descricao);
                for (Tipo t : tiposSelecionados) novo.adicionarTipo(t);
                pokemonDAO.salvar(novo);
            } else {
                pokemonEmEdicao.setNome(nome);
                pokemonEmEdicao.setNumeroPokedex(numeroPokedex);
                pokemonEmEdicao.setAltura(altura);
                pokemonEmEdicao.setPeso(peso);
                pokemonEmEdicao.setStats(stats);
                pokemonEmEdicao.setDescricao(descricao);
                for (Tipo t : new ArrayList<>(pokemonEmEdicao.getTipos())) {
                    pokemonEmEdicao.removerTipo(t);
                }
                for (Tipo t : tiposSelecionados) pokemonEmEdicao.adicionarTipo(t);
                pokemonDAO.atualizar(pokemonEmEdicao);
            }
            pokemonDAO.persistir(arquivoPokemons);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar Pokémon: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        atualizar();
        limparFormulario();
        if (aoMudar != null) aoMudar.run();
    }

    private void excluir() {
        if (pokemonEmEdicao == null) {
            JOptionPane.showMessageDialog(this, "Selecione um Pokémon na tabela para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            pokemonDAO.apagar(pokemonEmEdicao.getId());
            pokemonDAO.persistir(arquivoPokemons);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir Pokémon: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        atualizar();
        limparFormulario();
        if (aoMudar != null) aoMudar.run();
    }

    private int proximoId() {
        int max = 0;
        for (Pokemon p : GuiUtil.listar(pokemonDAO)) max = Math.max(max, p.getId());
        return max + 1;
    }
}