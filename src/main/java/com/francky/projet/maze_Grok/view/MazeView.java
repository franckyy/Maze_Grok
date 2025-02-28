package com.francky.projet.maze_Grok.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import javax.swing.Timer;
import com.francky.projet.maze_Grok.controller.MazeController;
import com.francky.projet.maze_Grok.model.MazeModel;
import com.francky.projet.maze_Grok.utils.SoundManager;

public class MazeView extends JPanel {
    private MazeModel model;
    private MazeController controller;
    private SoundManager soundManager;
    private int CELL_SIZE;
    private final int PLAYER_SIZE_RATIO = 2;
    private boolean gameWon = false;
    private float offsetX = 0, offsetY = 0;
    private int animationFrame = 0;
    private boolean trapAnimation = false;
    private int trapAnimationStep = 0;
    private LadybugRenderer ladybugRenderer;

    public MazeView(MazeModel model) {
        this.model = model;
        this.soundManager = new SoundManager();
        this.ladybugRenderer = new LadybugRenderer();
        setPreferredSize(new Dimension(600, 600));
    }

    public void setController(MazeController controller) {
        this.controller = controller;
    }

    public int getCellSize() {
        return CELL_SIZE;
    }

    public void setPlayerOffset(float offsetX, float offsetY, int animationFrame) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.animationFrame = animationFrame;
    }

    public void startTrapAnimation(Runnable onFinished) {
        trapAnimation = true;
        trapAnimationStep = 10;
        Timer animationTimer = new Timer(50, e -> {
            trapAnimationStep--;
            repaint();
            if (trapAnimationStep <= 0) {
                ((Timer) e.getSource()).stop();
                trapAnimation = false;
                onFinished.run();
            }
        });
        animationTimer.start();
    }

    public void resetView() {
        gameWon = false;
    }

    public void setGameWon(boolean won) {
        gameWon = won;
        repaint();
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void setModel(MazeModel newModel) {
        this.model = newModel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        CELL_SIZE = Math.min(getWidth() / model.getMazeWidth(), getHeight() / model.getMazeHeight());
        int PLAYER_SIZE = Math.max(CELL_SIZE - PLAYER_SIZE_RATIO, 20);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        for (int y = 0; y < model.getMazeHeight(); y++) {
            for (int x = 0; x < model.getMazeWidth(); x++) {
                if (model.getMazeCell(y, x) == 1) {
                    drawBrickWall(g2d, x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE);
                }
            }
        }

        if (model.getTrapX() != -1 && model.getTrapY() != -1) {
            g2d.setColor(model.isTrapOpen() ? Color.BLACK : Color.WHITE);
            int trapX = model.getTrapX() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2;
            int trapY = model.getTrapY() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2;
            g2d.fillRect(trapX, trapY, PLAYER_SIZE, PLAYER_SIZE);
        }

        g2d.setColor(Color.GREEN);
        int exitX = model.getExitX() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2;
        int exitY = model.getExitY() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2;
        g2d.fillOval(exitX, exitY, PLAYER_SIZE, PLAYER_SIZE);

        int playerX = model.getPlayerX() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2 + (int) offsetX;
        int playerY = model.getPlayerY() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2 + (int) offsetY;
        int playerSize = trapAnimation ? PLAYER_SIZE * trapAnimationStep / 10 : PLAYER_SIZE;
        int direction = (controller != null) ? controller.getCurrentDirection() : 3;
        ladybugRenderer.draw(g2d, playerX, playerY, playerSize, animationFrame, direction);

        if (gameWon) {
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            String message = "Clic 'space' to continue ou 'q' to exit game";
            int textWidth = g2d.getFontMetrics().stringWidth(message);
            int textHeight = g2d.getFontMetrics().getHeight();
            int textX = (getWidth() - textWidth) / 2;
            int textY = getHeight() / 2;

            // Rectangle de fond clair
            g2d.setColor(new Color(200, 200, 200)); // Gris clair
            int padding = 10; // Marge autour du texte
            g2d.fillRect(textX - padding, textY - textHeight + padding / 2, textWidth + 2 * padding, textHeight + padding);

            // Texte en vert foncé
            g2d.setColor(new Color(0, 100, 0));
            g2d.drawString(message, textX, textY);
        }
    }

    private void drawBrickWall(Graphics2D g2d, int x, int y, int size) {
        g2d.setColor(new Color(139, 69, 19));
        g2d.fillRect(x, y, size, size);
        int brickWidth = size / 2;
        int brickHeight = size / 4;
        g2d.setColor(Color.LIGHT_GRAY);
        for (int h = 0; h <= size; h += brickHeight) {
            g2d.drawLine(x, y + h, x + size, y + h);
        }
        for (int row = 0; row < size / brickHeight; row++) {
            int yPos = y + row * brickHeight;
            int offset = (row % 2 == 0) ? 0 : brickWidth / 2;
            for (int col = offset; col < size; col += brickWidth) {
                g2d.drawLine(x + col, yPos, x + col, yPos + brickHeight);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }
}