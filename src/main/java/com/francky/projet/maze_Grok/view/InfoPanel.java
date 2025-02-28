package com.francky.projet.maze_Grok.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;
import com.francky.projet.maze_Grok.PlayerTimes;
import com.francky.projet.maze_Grok.HighScores;

public class InfoPanel extends JPanel {
    private int level;
    private int levelTime;
    private int totalTime;
    private HighScores.HighScoreEntry highScoreLevel;
    private HighScores.HighScoreEntry highScoreTotal;

    public InfoPanel(int initialLevel, PlayerTimes playerTimes, HighScores highScores) {
        this.level = initialLevel;
        this.levelTime = 0;
        this.totalTime = playerTimes.lastTotalTime;
        this.highScoreLevel = highScores.levelHighScores.getOrDefault(initialLevel, new HighScores.HighScoreEntry(Integer.MAX_VALUE, ""));
        this.highScoreTotal = highScores.totalHighScore;
        setPreferredSize(new Dimension(0, 100));
        setBackground(Color.LIGHT_GRAY);
    }

    public void setLevel(int level) {
        this.level = level;
        repaint();
    }

    public void setTimes(int levelTime, int totalTime, HighScores.HighScoreEntry highScoreLevel, HighScores.HighScoreEntry highScoreTotal) {
        this.levelTime = levelTime;
        this.totalTime = totalTime;
        this.highScoreLevel = highScoreLevel;
        this.highScoreTotal = highScoreTotal;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

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
        String highScoreTotalText = "Meilleur total : " + (highScoreTotal.time == Integer.MAX_VALUE ? "N/A" : highScoreTotal.time + " s (" + highScoreTotal.player + ")");
        int highScoreLevelWidth = g.getFontMetrics().stringWidth(highScoreLevelText);
        int highScoreTotalWidth = g.getFontMetrics().stringWidth(highScoreTotalText);
        int highScoreLevelX = (getWidth() - highScoreLevelWidth) / 2;
        int highScoreTotalX = getWidth() - highScoreTotalWidth - 20;
        g.drawString(highScoreLevelText, highScoreLevelX, y + 30);
        g.drawString(highScoreTotalText, highScoreTotalX, y + 30);
    }
}