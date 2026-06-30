package com.gp.visual;

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

public class TipoPanel extends JPanel {

    private final EntidadeDAO<Tipo> tipoDAO;
    private final String arquivoTipos;
    private final Runnable aoMudar; // notifica outros painéis (Pokémon usa Tipo)

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField campoNome;
    private JTextArea campoDescricao;
    private JList<Tipo> listaFraquezas;
    private DefaultListModel<Tipo> listModelFraquezas;
    private Tipo tipoEmEdicao = null;

    public TipoPanel(EntidadeDAO<Tipo> tipoDAO, String arquivoTipos, Runnable aoMudar) {
        this.tipoDAO = tipoDAO;
        this.arquivoTipos = arquivoTipos;
        this.aoMudar = aoMudar;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setBackground(Tema.FUNDO);

        JLabel titulo = new JLabel("Tipos de Pokémon");
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
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Descrição", "Fraquezas"}, 0) {
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

        JLabel rotuloForm = new JLabel("Cadastrar / Editar");
        rotuloForm.setFont(Tema.FONTE_SUB);
        form.add(rotuloForm);
        form.add(Box.createVerticalStrut(15));

        form.add(rotulo("Nome do tipo"));
        campoNome = new JTextField();
        estilizarCampo(campoNome);
        form.add(campoNome);
        form.add(Box.createVerticalStrut(12));

        form.add(rotulo("Descrição"));
        campoDescricao = new JTextArea(3, 0);
        campoDescricao.setLineWrap(true);
        campoDescricao.setWrapStyleWord(true);
        campoDescricao.setFont(Tema.FONTE_NORMAL);
        campoDescricao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDA),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        JScrollPane scrollDesc = new JScrollPane(campoDescricao);
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(scrollDesc);
        form.add(Box.createVerticalStrut(12));

        form.add(rotulo("Fraquezas (Ctrl+clique para vários)"));
        listModelFraquezas = new DefaultListModel<>();
        listaFraquezas = new JList<>(listModelFraquezas);
        listaFraquezas.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaFraquezas.setFont(Tema.FONTE_NORMAL);
        JScrollPane scrollLista = new JScrollPane(listaFraquezas);
        scrollLista.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
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

    /** Chamado ao trocar de tela: recarrega tabela e lista de fraquezas. */
    public void atualizar() {
        List<Tipo> tipos = GuiUtil.listar(tipoDAO);

        listModelFraquezas.clear();
        for (Tipo t : tipos) listModelFraquezas.addElement(t);

        tableModel.setRowCount(0);
        for (Tipo t : tipos) {
            StringBuilder fraquezas = new StringBuilder();
            for (Tipo f : t.getFraquezas()) {
                if (fraquezas.length() > 0) fraquezas.append(", ");
                fraquezas.append(f.getNome());
            }
            tableModel.addRow(new Object[]{t.getId(), t.getNome(), t.getDescricao(), fraquezas.toString()});
        }
    }

    private void carregarParaEdicao(int id) {
        Tipo t = buscarPorId(id);
        if (t == null) return;
        tipoEmEdicao = t;
        campoNome.setText(t.getNome());
        campoDescricao.setText(t.getDescricao());
        listaFraquezas.clearSelection();
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < listModelFraquezas.size(); i++) {
            if (t.getFraquezas().contains(listModelFraquezas.get(i))) indices.add(i);
        }
        listaFraquezas.setSelectedIndices(indices.stream().mapToInt(Integer::intValue).toArray());
    }

    private Tipo buscarPorId(int id) {
        for (Tipo t : GuiUtil.listar(tipoDAO)) if (t.getId() == id) return t;
        return null;
    }

    private void limparFormulario() {
        tipoEmEdicao = null;
        campoNome.setText("");
        campoDescricao.setText("");
        listaFraquezas.clearSelection();
        table.clearSelection();
    }

    private void salvar() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do tipo.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String descricao = campoDescricao.getText().trim();
        List<Tipo> fraquezasSelecionadas = listaFraquezas.getSelectedValuesList();

        try {
            if (tipoEmEdicao == null) {
                int novoId = proximoId();
                Tipo novo = new Tipo(novoId, nome, descricao);
                for (Tipo f : fraquezasSelecionadas) novo.adicionarFraquezas(f);
                tipoDAO.salvar(novo);
            } else {
                tipoEmEdicao.setNome(nome);
                tipoEmEdicao.setDescricao(descricao);
                for (Tipo f : new ArrayList<>(tipoEmEdicao.getFraquezas())) {
                    tipoEmEdicao.removerFraquezas(f);
                }
                for (Tipo f : fraquezasSelecionadas) tipoEmEdicao.adicionarFraquezas(f);
                tipoDAO.atualizar(tipoEmEdicao);
            }
            tipoDAO.persistir(arquivoTipos);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar Tipo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        atualizar();
        limparFormulario();
        if (aoMudar != null) aoMudar.run();
    }

    private void excluir() {
        if (tipoEmEdicao == null) {
            JOptionPane.showMessageDialog(this, "Selecione um tipo na tabela para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            tipoDAO.apagar(tipoEmEdicao.getId());
            tipoDAO.persistir(arquivoTipos);
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir Tipo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        atualizar();
        limparFormulario();
        if (aoMudar != null) aoMudar.run();
    }

    private int proximoId() {
        int max = 0;
        for (Tipo t : GuiUtil.listar(tipoDAO)) max = Math.max(max, t.getId());
        return max + 1;
    }
}