package com.francky.projet.maze_Grok.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;

import com.francky.projet.maze_Grok.model.MazeModel;
import com.francky.projet.maze_Grok.view.MazeView;

//TODO
/*
 * Ajouter un effet visuel aux déplacements du joueur pour plus de fluidité
 * Ajouter du son aux déplacements et à la victoire
 * Ajouter un mode de difficulté (taille variable du labyrinthe)
 * Ajouter des missions, ou des challenges en cours de chemin
 * Ajouter des ennemis ou des bonus
 * Lorsque l'on a réussi un labyrinthe, l'arrière plan des boutons "Rejouer" et "Arrêter" doit être transparent mais coloré (grisâtre ?)
 */

public class MazeController {
	private MazeModel model;
    private MazeView view;
    private int cellSize; // Ajouté pour calculer les offsets
    private Timer moveTimer; // Timer pour l’animation

    public MazeController(MazeModel model, MazeView view) {
        this.model = model;
        this.view = view;
        this.cellSize = view.getCellSize(); // Méthode à ajouter dans MazeView
        setupKeyBindings();
    }

    private void setupKeyBindings() {
        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                int currentX = model.getPlayerX();
                int currentY = model.getPlayerY();

                // Arrêter toute animation en cours
                if (moveTimer != null && moveTimer.isRunning()) {
                    return; // Ignore les nouvelles touches pendant l’animation
                }

                if (key == KeyEvent.VK_UP && model.getMazeCell(currentY - 1, currentX) == 0) {
                    movePlayerSmoothly(currentX, currentY - 1);
                } else if (key == KeyEvent.VK_DOWN && model.getMazeCell(currentY + 1, currentX) == 0) {
                    movePlayerSmoothly(currentX, currentY + 1);
                } else if (key == KeyEvent.VK_LEFT && model.getMazeCell(currentY, currentX - 1) == 0) {
                    movePlayerSmoothly(currentX - 1, currentY);
                } else if (key == KeyEvent.VK_RIGHT && model.getMazeCell(currentY, currentX + 1) == 0) {
                    movePlayerSmoothly(currentX + 1, currentY);
                } else if (key == KeyEvent.VK_R && model.getPlayerX() == model.getExitX() && model.getPlayerY() == model.getExitY()) {
                    MazeModel newModel = new MazeModel(model.getMazeWidth() / 2 - 1, model.getMazeHeight() / 2 - 1);
                    model = newModel;
                    view.repaint();
                } else if (key == KeyEvent.VK_Q) {
                    System.exit(0);
                }
            }
        });
    }

    private void movePlayerSmoothly(int targetX, int targetY) {
        int steps = 10; // Nombre d’étapes pour l’animation
        int currentX = model.getPlayerX();
        int currentY = model.getPlayerY();
        float dx = (targetX - currentX) * cellSize / (float) steps; // Déplacement par étape en pixels
        float dy = (targetY - currentY) * cellSize / (float) steps;

        moveTimer = new Timer(10, null); // 10ms par frame
        int[] step = {0}; // Compteur d’étapes
        moveTimer.addActionListener(e -> {
            step[0]++;
            view.setPlayerOffset(dx * step[0], dy * step[0]); // Décalage temporaire
            view.repaint();
            if (step[0] >= steps) {
                moveTimer.stop();
                model.setPlayerX(targetX); // Position finale dans le modèle
                model.setPlayerY(targetY);
                view.setPlayerOffset(0, 0); // Réinitialiser le décalage
                view.repaint();
            }
        });
        moveTimer.start();
    }
}
