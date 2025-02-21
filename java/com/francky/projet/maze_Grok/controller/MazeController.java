package com.francky.projet.maze_Grok.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Timer;

import com.francky.projet.maze_Grok.model.MazeModel;
import com.francky.projet.maze_Grok.utils.SoundManager;
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
    private Timer moveTimer;
    private boolean[] directions;
    private static final int[] DIRECTION_KEYS = {KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT};
    private SoundManager soundManager;

    public MazeController(MazeModel model, MazeView view) {
        this.model = model;
        this.view = view;
        this.directions = new boolean[4];
        this.soundManager = new SoundManager();
        setupKeyBindings();
        soundManager.playBackgroundMusic("king_tubby_01.wav");
    }

    // Nouvelle méthode pour mettre à jour le modèle
    public void setModel(MazeModel newModel) {
        this.model = newModel;
        stopMovement(); // Réinitialiser l’état du déplacement
        view.setPlayerOffset(0, 0); // Réinitialiser les offsets visuels
        view.repaint(); // Redessiner avec le nouveau modèle
        view.requestFocusInWindow(); // S’assurer que la vue a le focus
    }

    private void setupKeyBindings() {
        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                int currentX = model.getPlayerX();
                int currentY = model.getPlayerY();

                if (key == KeyEvent.VK_UP) directions[0] = true;
                else if (key == KeyEvent.VK_DOWN) directions[1] = true;
                else if (key == KeyEvent.VK_LEFT) directions[2] = true;
                else if (key == KeyEvent.VK_RIGHT) directions[3] = true;
                else if (key == KeyEvent.VK_R && model.getPlayerX() == model.getExitX() && model.getPlayerY() == model.getExitY()) {
                    soundManager.stopBackgroundMusic();
                    MazeModel newModel = new MazeModel(model.getMazeWidth() / 2 - 1, model.getMazeHeight() / 2 - 1);
                    setModel(newModel); // Utiliser la nouvelle méthode
                    soundManager.playBackgroundMusic("king_tubby_01.wav");
                    return;
                } else if (key == KeyEvent.VK_Q) {
                    soundManager.stopBackgroundMusic();
                    System.exit(0);
                }

                if (moveTimer == null || !moveTimer.isRunning()) {
                    startContinuousMovement();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_UP) directions[0] = false;
                else if (key == KeyEvent.VK_DOWN) directions[1] = false;
                else if (key == KeyEvent.VK_LEFT) directions[2] = false;
                else if (key == KeyEvent.VK_RIGHT) directions[3] = false;

                if (!isAnyDirectionPressed() && moveTimer != null && moveTimer.isRunning()) {
                    moveTimer.stop();
                    view.setPlayerOffset(0, 0);
                    view.repaint();
                }
            }
        });
    }

    private boolean isAnyDirectionPressed() {
        for (boolean direction : directions) {
            if (direction) return true;
        }
        return false;
    }

    private void stopMovement() {
        if (moveTimer != null && moveTimer.isRunning()) {
            moveTimer.stop();
        }
        view.setPlayerOffset(0, 0);
        for (int i = 0; i < directions.length; i++) {
            directions[i] = false;
        }
    }

    private void startContinuousMovement() {
        int steps = 10;
        int cellSize = view.getCellSize();
        float stepDuration = 10f;

        moveTimer = new Timer((int) stepDuration, null);
        int[] step = {0};
        moveTimer.addActionListener(e -> {
            step[0]++;
            int currentX = model.getPlayerX();
            int currentY = model.getPlayerY();
            int targetX = currentX;
            int targetY = currentY;

            if (directions[0] && model.getMazeCell(currentY - 1, currentX) == 0) targetY--;
            else if (directions[1] && model.getMazeCell(currentY + 1, currentX) == 0) targetY++;
            else if (directions[2] && model.getMazeCell(currentY, currentX - 1) == 0) targetX--;
            else if (directions[3] && model.getMazeCell(currentY, currentX + 1) == 0) targetX++;

            float dx = (targetX - currentX) * cellSize / (float) steps;
            float dy = (targetY - currentY) * cellSize / (float) steps;

            view.setPlayerOffset(dx * step[0], dy * step[0]);
            view.repaint();

            if (step[0] >= steps) {
                step[0] = 0;
                model.setPlayerX(targetX);
                model.setPlayerY(targetY);
                view.setPlayerOffset(0, 0);
                soundManager.playSoundEffect("blip.wav");
                if (model.getPlayerX() == model.getExitX() && model.getPlayerY() == model.getExitY()) {
                    soundManager.stopBackgroundMusic();
                    soundManager.playSoundEffect("organ.wav");
                }
                if (!isAnyDirectionPressed()) {
                    moveTimer.stop();
                }
            }
        });
        moveTimer.start();
    }
}
