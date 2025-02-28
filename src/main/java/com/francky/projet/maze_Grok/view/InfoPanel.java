package com.francky.projet.maze_Grok.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

public class InfoPanel extends JPanel {
    private int level;

    public InfoPanel(int initialLevel) {
        this.level = initialLevel;
        setPreferredSize(new Dimension(0, 100)); // Hauteur fixe de 100 pixels, largeur s'adapte
        setBackground(Color.LIGHT_GRAY);
    }

    public void setLevel(int level) {
        this.level = level;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.BLACK);
        String info = "Niveau : " + level;
        int textWidth = g.getFontMetrics().stringWidth(info);
        int textX = (getWidth() - textWidth) / 2;
        int textY = getHeight() / 2 + g.getFontMetrics().getAscent() / 2;
        g.drawString(info, textX, textY);
    }
}