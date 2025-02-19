package com.francky.projet.maze_Grok.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.francky.projet.maze_Grok.model.MazeModel;

public class MazeView extends JPanel{
	private MazeModel model;
    private final int CELL_SIZE; // Plus static pour être défini par le constructeur
    private final int PLAYER_SIZE;

    // Constructeur avec un seul paramètre, appelle le deuxième avec une taille par défaut
    public MazeView(MazeModel model) {
        this(model, 10); // Par défaut, taille 10x10
    }

    // Constructeur avec deux paramètres pour ajuster CELL_SIZE
    public MazeView(MazeModel model, int mazeSize) {
        this.model = model;
        // Ajuster CELL_SIZE en fonction de la taille du labyrinthe
        if (mazeSize == 30) {
            CELL_SIZE = 15; // Plus petit pour 30x30
        } else if (mazeSize == 20) {
            CELL_SIZE = 18; // Moyen pour 20x20
        } else {
            CELL_SIZE = 20; // Standard pour 10x10 ou autre
        }
        PLAYER_SIZE = CELL_SIZE - 2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);

        // Dessiner les murs
        for (int y = 0; y < model.getMazeHeight(); y++) {
            for (int x = 0; x < model.getMazeWidth(); x++) {
                if (model.getMazeCell(y, x) == 1) {
                    g2d.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        // Dessiner la sortie (en vert)
        g2d.setColor(Color.GREEN);
        int exitX = model.getExitX() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2;
        int exitY = model.getExitY() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2;
        g2d.fillOval(exitX, exitY, PLAYER_SIZE, PLAYER_SIZE);

        // Dessiner le joueur (en rouge)
        g2d.setColor(Color.RED);
        int playerX = model.getPlayerX() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2;
        int playerY = model.getPlayerY() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2;
        g2d.fillOval(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);

        // Vérifier la victoire et afficher "Gagné !" en bleu sur fond vert
        if (model.getPlayerX() == model.getExitX() && model.getPlayerY() == model.getExitY()) {
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            int rectWidth = 120 + CELL_SIZE;
            int rectHeight = 40 + CELL_SIZE;
            int rectX = getWidth() / 2 - 60;
            int rectY = getHeight() / 2 - 25 - CELL_SIZE;
            g2d.setColor(Color.GREEN);
            g2d.fillRect(rectX, rectY, rectWidth, rectHeight);

            g2d.setColor(Color.BLUE);
            int textX = rectX + (rectWidth - 100) / 2 - 5;
            int textY = rectY + (rectHeight + 10) / 2 + 5;
            g2d.drawString("Gagné !", textX, textY);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(model.getMazeWidth() * CELL_SIZE, model.getMazeHeight() * CELL_SIZE);
    }
}
