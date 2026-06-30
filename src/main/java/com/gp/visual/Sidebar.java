package com.gp.visual;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class Sidebar extends JPanel {

    private JButton botaoSelecionado;

    public Sidebar(Consumer<String> aoSelecionar) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Tema.VERMELHO);
        setPreferredSize(new Dimension(220, 0));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        add(criarLogo());
        add(Box.createVerticalStrut(30));

        JButton b1 = criarBotao("🔥  Tipos", "TIPOS", aoSelecionar);
        JButton b2 = criarBotao("⚡  Pokémons", "POKEMONS", aoSelecionar);
        JButton b3 = criarBotao("🎽  Treinadores", "TREINADORES", aoSelecionar);
        JButton b4 = criarBotao("⚔  Batalha", "BATALHA", aoSelecionar);

        add(b1);
        add(b2);
        add(b3);
        add(b4);
        add(Box.createVerticalGlue());

        marcarSelecionado(b1);
    }

    private JComponent criarLogo() {
        JPanel topo = new JPanel();
        topo.setOpaque(false);
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.setBorder(BorderFactory.createEmptyBorder(25, 20, 10, 20));

        JLabel titulo = new JLabel("PokeCentro");
        titulo.setForeground(Tema.BRANCO);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Tipos · Pokémons · Treinadores");
        sub.setForeground(Color.LIGHT_GRAY);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        topo.add(titulo);
        topo.add(sub);
        return topo;
    }

    private JButton criarBotao(String texto, String chave, Consumer<String> aoSelecionar) {
        JButton botao = new JButton(texto);
        botao.setFont(Tema.FONTE_SIDEBAR);
        botao.setForeground(Color.WHITE);
        botao.setBackground(Tema.SIDEBAR);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setContentAreaFilled(true);
        botao.setHorizontalAlignment(SwingConstants.LEFT);
        botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        botao.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 10));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setAlignmentX(Component.LEFT_ALIGNMENT);

        botao.addActionListener(e -> {
            marcarSelecionado(botao);
            aoSelecionar.accept(chave);
        });
        botao.addChangeListener(e -> {
            if (botao != botaoSelecionado) {
                botao.setBackground(botao.getModel().isRollover() ? Tema.SIDEBAR_HOVER : Tema.SIDEBAR);
            }
        });
        return botao;
    }

    private void marcarSelecionado(JButton botao) {
        if (botaoSelecionado != null) botaoSelecionado.setBackground(Tema.SIDEBAR);
        botaoSelecionado = botao;
        botao.setBackground(Tema.VERMELHO);
    }
}
