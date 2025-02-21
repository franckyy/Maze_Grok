package com.francky.projet.maze_Grok.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.francky.projet.maze_Grok.controller.MazeController;
import com.francky.projet.maze_Grok.model.MazeModel;
import com.francky.projet.maze_Grok.utils.SoundManager;

public class MazeView extends JPanel {
	private MazeModel model;
    private MazeController controller; // Référence au contrôleur
    private SoundManager soundManager; // Ajout pour gérer les sons
    private int CELL_SIZE;
    private final int PLAYER_SIZE_RATIO = 2;
    private JButton replayButton;
    private JButton quitButton;
    private boolean gameWon = false;
    private float offsetX = 0, offsetY = 0;

    public MazeView(MazeModel model) {
        this(model, 10);
    }

    public MazeView(MazeModel model, int mazeSize) {
        this.model = model;
        this.soundManager = new SoundManager(); // Initialisation
        setPreferredSize(new Dimension(600, 600));
        initButtons();
    }

    // Ajouter une méthode pour définir le contrôleur
    public void setController(MazeController controller) {
        this.controller = controller;
    }

    public int getCellSize() {
        return CELL_SIZE;
    }

    public void setPlayerOffset(float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    private void initButtons() {
        replayButton = new JButton("Rejouer");
        quitButton = new JButton("Quitter");
        replayButton.setVisible(false);
        quitButton.setVisible(false);
        add(replayButton);
        add(quitButton);

        replayButton.addActionListener(e -> restartGame());
        quitButton.addActionListener(e -> System.exit(0));
    }

    private void restartGame() {
        soundManager.stopBackgroundMusic(); // Arrêter la musique actuelle
        model = new MazeModel(model.getMazeWidth() / 2 - 1, model.getMazeHeight() / 2 - 1);
        if (controller != null) {
            controller.setModel(model); // Mettre à jour le modèle dans le contrôleur
        }
        gameWon = false;
        replayButton.setVisible(false);
        quitButton.setVisible(false);
        soundManager.playBackgroundMusic("king_tubby_01.wav"); // Redémarrer la musique
        repaint();
        requestFocusInWindow(); // S’assurer que la vue a le focus
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        CELL_SIZE = Math.min(getWidth() / model.getMazeWidth(), getHeight() / model.getMazeHeight());
        int PLAYER_SIZE = CELL_SIZE - PLAYER_SIZE_RATIO;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        for (int y = 0; y < model.getMazeHeight(); y++) {
            for (int x = 0; x < model.getMazeWidth(); x++) {
                if (model.getMazeCell(y, x) == 1) {
                    drawBrickWall(g2d, x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE);
                }
            }
        }

        g2d.setColor(Color.GREEN);
        int exitX = model.getExitX() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2;
        int exitY = model.getExitY() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2;
        g2d.fillOval(exitX, exitY, PLAYER_SIZE, PLAYER_SIZE);

        g2d.setColor(Color.RED);
        int playerX = model.getPlayerX() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2 + (int) offsetX;
        int playerY = model.getPlayerY() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2 + (int) offsetY;
        g2d.fillOval(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);

        if (model.getPlayerX() == model.getExitX() && model.getPlayerY() == model.getExitY()) {
            gameWon = true;
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            int rectWidth = 120 + CELL_SIZE;
            int rectHeight = 40 + CELL_SIZE;
            int rectX = getWidth() / 2 - rectWidth / 2;
            int rectY = getHeight() / 2 - rectHeight / 2 - CELL_SIZE;
            g2d.setColor(Color.GREEN);
            g2d.fillRect(rectX, rectY, rectWidth, rectHeight);

            g2d.setColor(Color.BLUE);
            int textX = rectX + (rectWidth - 100) / 2 - 5;
            int textY = rectY + (rectHeight + 10) / 2 + 5;
            g2d.drawString("Gagné !", textX, textY);

            replayButton.setBounds(getWidth() / 2 - 100, getHeight() / 2 + 20, 80, 30);
            quitButton.setBounds(getWidth() / 2 + 20, getHeight() / 2 + 20, 80, 30);
            replayButton.setVisible(true);
            quitButton.setVisible(true);
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
