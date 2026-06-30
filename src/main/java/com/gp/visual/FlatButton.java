package com.gp.visual;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class FlatButton extends JButton {

    private final Color corFundo;
    private final Color corHover;

    public FlatButton(String texto, Color corFundo, Color corTexto) {
        super(texto);
        this.corFundo = corFundo;
        this.corHover = corFundo.darker();
        setFont(Tema.FONTE_SUB);
        setForeground(corTexto);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color cor = getModel().isRollover() ? corHover : corFundo;
        g2.setColor(cor);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 14, 14));
        g2.dispose();
        super.paintComponent(g);
    }
}
