package com.francky.projet.maze_Grok.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import javax.swing.JPanel;
import com.francky.projet.maze_Grok.PlayerTimes;
import com.francky.projet.maze_Grok.HighScores;

public class InfoPanel extends JPanel {
    private int level;
    private int levelTime;
    private int totalTime;
    private HighScores.HighScoreEntry highScoreLevel;
    private int lives; // Nouveau champ pour les vies

    public InfoPanel(int initialLevel, PlayerTimes playerTimes, HighScores highScores) {
        this.level = initialLevel;
        this.levelTime = 0;
        this.totalTime = playerTimes.lastTotalTime;
        this.highScoreLevel = highScores.levelHighScores.getOrDefault(initialLevel, new HighScores.HighScoreEntry(Integer.MAX_VALUE, ""));
        this.lives = playerTimes.lives > 0 ? playerTimes.lives : 3; // Charge ou initialise à 3
        setPreferredSize(new Dimension(0, 100));
        setBackground(Color.LIGHT_GRAY);
    }

    public void setLevel(int level) {
        this.level = level;
        repaint();
    }

    public void setTimes(int levelTime, int totalTime, HighScores.HighScoreEntry highScoreLevel, int lives) {
        this.levelTime = levelTime;
        this.totalTime = totalTime;
        this.highScoreLevel = highScoreLevel;
        this.lives = lives; // Met à jour les vies
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Font normalFont = new Font("Arial", Font.BOLD, 20);
        Font smallFont = new Font("Arial", Font.BOLD, 14);
        g.setColor(Color.BLACK);

        g.setFont(normalFont);
        String levelText = "Niveau : " + level;
        int levelX = 20;
        int y = getHeight() / 2 - 10;
        g.drawString(levelText, levelX, y);

        String levelTimeText = "Temps niveau : " + levelTime + " s";
        int levelTimeWidth = g.getFontMetrics().stringWidth(levelTimeText);
        int levelTimeX = (getWidth() - levelTimeWidth) / 2;
        g.drawString(levelTimeText, levelTimeX, y);

        String totalTimeText = "Temps total : " + totalTime + " s";
        int totalTimeWidth = g.getFontMetrics().stringWidth(totalTimeText);
        int totalTimeX = getWidth() - totalTimeWidth - 20;
        g.drawString(totalTimeText, totalTimeX, y);

        g.setFont(smallFont);
        String highScoreLevelText = "Meilleur niveau : " + (highScoreLevel.time == Integer.MAX_VALUE ? "N/A" : highScoreLevel.time + " s (" + highScoreLevel.player + ")");
        int highScoreLevelWidth = g.getFontMetrics().stringWidth(highScoreLevelText);
        int highScoreLevelX = (getWidth() - highScoreLevelWidth) / 2;
        g.drawString(highScoreLevelText, highScoreLevelX, y + 30);

        // Dessiner les cœurs pour les vies (en haut à droite)
        int heartSize = 20;
        int heartSpacing = 5;
        int startX = getWidth() - (lives * (heartSize + heartSpacing)) - 10;
        int heartY = 10;

        for (int i = 0; i < lives; i++) {
            drawHeart(g2d, startX + i * (heartSize + heartSpacing), heartY, heartSize);
        }
    }

    // Méthode pour dessiner un cœur rouge avec reflet
    private void drawHeart(Graphics2D g2d, int x, int y, int size) {
        g2d.setColor(Color.RED);
        Path2D heart = new Path2D.Double();
        double scale = size / 20.0; // Ajuste la taille du cœur
        heart.moveTo(x + 10 * scale, y + 5 * scale);
        heart.curveTo(x + 10 * scale, y + 2 * scale, x + 7 * scale, y, x + 5 * scale, y + 2 * scale);
        heart.curveTo(x + 3 * scale, y + 4 * scale, x, y + 7 * scale, x, y + 10 * scale);
        heart.curveTo(x, y + 13 * scale, x + 3 * scale, y + 16 * scale, x + 5 * scale, y + 18 * scale);
        heart.curveTo(x + 7 * scale, y + 20 * scale, x + 10 * scale, y + 18 * scale, x + 13 * scale, y + 16 * scale);
        heart.curveTo(x + 15 * scale, y + 14 * scale, x + 17 * scale, y + 11 * scale, x + 17 * scale, y + 8 * scale);
        heart.curveTo(x + 17 * scale, y + 5 * scale, x + 15 * scale, y + 2 * scale, x + 13 * scale, y);
        heart.curveTo(x + 11 * scale, y - 2 * scale, x + 10 * scale, y + 2 * scale, x + 10 * scale, y + 5 * scale);
        g2d.fill(heart);

        // Reflet de lumière (petit triangle blanc)
        g2d.setColor(Color.WHITE);
        g2d.fillPolygon(
            new int[] {(int) (x + 7 * scale), (int) (x + 9 * scale), (int) (x + 5 * scale)},
            new int[] {(int) (y + 3 * scale), (int) (y + 5 * scale), (int) (y + 7 * scale)},
            3
        );
    }
}