package com.francky.projet.maze_Grok.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

public class InfoPanel extends JPanel {
    private int level;
    private int levelTime;
    private int totalTime;

    public InfoPanel(int initialLevel) {
        this.level = initialLevel;
        this.levelTime = 0;
        this.totalTime = 0;
        setPreferredSize(new Dimension(0, 100));
        setBackground(Color.LIGHT_GRAY);
    }

    public void setLevel(int level) {
        this.level = level;
        repaint();
    }

    public void setTimes(int levelTime, int totalTime) {
        this.levelTime = levelTime;
        this.totalTime = totalTime;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.BLACK);

        // Niveau à gauche
        String levelText = "Niveau : " + level;
        int levelX = 20; // Marge gauche
        int textY = getHeight() / 2 + g.getFontMetrics().getAscent() / 2;
        g.drawString(levelText, levelX, textY);

        // Temps niveau au centre
        String levelTimeText = "Temps niveau : " + levelTime + " s";
        int levelTimeWidth = g.getFontMetrics().stringWidth(levelTimeText);
        int levelTimeX = (getWidth() - levelTimeWidth) / 2;
        g.drawString(levelTimeText, levelTimeX, textY);

        // Temps total à droite
        String totalTimeText = "Temps total : " + totalTime + " s";
        int totalTimeWidth = g.getFontMetrics().stringWidth(totalTimeText);
        int totalTimeX = getWidth() - totalTimeWidth - 20; // Marge droite
        g.drawString(totalTimeText, totalTimeX, textY);
    }
}