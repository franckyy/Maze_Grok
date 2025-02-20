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

    public MazeController(MazeModel model, MazeView view) {
        this.model = model;
        this.view = view;
        setupKeyBindings();
    }

    private void setupKeyBindings() {
        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                int currentX = model.getPlayerX();
                int currentY = model.getPlayerY();

                if (key == KeyEvent.VK_UP) model.setPlayerY(currentY - 1);
                else if (key == KeyEvent.VK_DOWN) model.setPlayerY(currentY + 1);
                else if (key == KeyEvent.VK_LEFT) model.setPlayerX(currentX - 1);
                else if (key == KeyEvent.VK_RIGHT) model.setPlayerX(currentX + 1);
                else if (key == KeyEvent.VK_R && model.getPlayerX() == model.getExitX() && model.getPlayerY() == model.getExitY()) {
                    // Relancer avec "R"
                    MazeModel newModel = new MazeModel(model.getMazeWidth() / 2 - 1, model.getMazeHeight() / 2 - 1);
                    model = newModel;
                }
                else if (key == KeyEvent.VK_Q) System.exit(0); // Quitter avec "Q"
                view.repaint();
            }
        });
    }

    private void movePlayerSmoothly(int targetX, int targetY) {
        Timer timer = new Timer(10, null); // 10ms par frame
        int steps = 10; // Nombre d’étapes pour l’animation
        int dx = (targetX - playerX) * CELL_SIZE / steps;
        int dy = (targetY - playerY) * CELL_SIZE / steps;
        int[] step = {0};
        timer.addActionListener(e -> {
            step[0]++;
            view.setPlayerOffset(dx * step[0], dy * step[0]);
            view.repaint();
            if (step[0] >= steps) {
                timer.stop();
                model.setPlayerX(targetX);
                model.setPlayerY(targetY);
                view.setPlayerOffset(0, 0);
            }
        });
        timer.start();
    }
}
