package com.francky.projet.maze_Grok.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import com.francky.projet.maze_Grok.Main;
import com.francky.projet.maze_Grok.model.LevelConfig;
import com.francky.projet.maze_Grok.model.LevelManager;
import com.francky.projet.maze_Grok.model.MazeModel;
import com.francky.projet.maze_Grok.utils.SoundManager;
import com.francky.projet.maze_Grok.view.MazeView;

public class MazeController {
    private MazeModel model;
    private MazeView view;
    private Timer moveTimer;
    public Timer wallChangeTimer;
    public Timer trapTimer;
    private boolean[] directions;
    private static final int[] DIRECTION_KEYS = {KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT};
    private SoundManager soundManager;
    private int level;
    private String playerName;
    private int lives;
    private LevelManager levelManager;
    private boolean gameOver = false;
    private Map<String, Integer> obstacleOccurrences = new HashMap<>();

    public MazeController(MazeModel model, MazeView view, int level, String playerName) {
        this.model = model;
        this.view = view;
        this.level = level;
        this.playerName = playerName;
        this.directions = new boolean[4];
        this.soundManager = new SoundManager();
        this.levelManager = new LevelManager();
        LevelConfig config = levelManager.getLevelConfig(level);
        this.lives = config.getLives();
        setupKeyBindings();
        soundManager.playBackgroundMusic("king_tubby_01.wav");
        setupLevelTimers();
    }

    private void setupLevelTimers() {
        LevelConfig config = levelManager.getLevelConfig(level);
        List<String> obstacles = config.getObstacles();
        List<Integer> frequencies = config.getObstacleFrequencies();
        List<Integer> counts = config.getObstacleCounts();

        obstacleOccurrences.clear();
        System.out.println("Obstacles chargés pour le niveau " + level + " : " + obstacles);
        for (int i = 0; i < obstacles.size(); i++) {
            obstacleOccurrences.put(obstacles.get(i), 0);
        }

        if (obstacles.contains("wallChange")) {
            int index = obstacles.indexOf("wallChange");
            int frequency = frequencies.get(index) * 1000;
            int maxOccurrences = counts.get(index);
            wallChangeTimer = new Timer(frequency, e -> {
                Integer occurrences = obstacleOccurrences.get("wallChange");
                int count = (occurrences != null) ? occurrences : 0;
                if (count < maxOccurrences) {
                    model.modifyPath();
                    obstacleOccurrences.put("wallChange", count + 1);
                    view.repaint();
                }
                if (obstacleOccurrences.get("wallChange") >= maxOccurrences) {
                    wallChangeTimer.stop();
                }
            });
            wallChangeTimer.setInitialDelay(frequency / 2);
            wallChangeTimer.start();
        }

        if (obstacles.contains("trap")) {
            int index = obstacles.indexOf("trap");
            int frequency = frequencies.get(index) * 1000;
            int maxCycles = counts.get(index); // Nombre de cycles complets (ouvert-fermé)
            trapTimer = new Timer(frequency / 2, e -> { // Divisé par 2 car 2 états par cycle
                Integer occurrences = obstacleOccurrences.get("trap");
                int cycleCount = (occurrences != null) ? occurrences / 2 : 0; // Compter les cycles (2 toggles = 1 cycle)
                if (cycleCount < maxCycles) {
                    model.toggleTrap(); // Ouvre ou ferme
                    int newOccurrences = (occurrences != null) ? occurrences + 1 : 1;
                    obstacleOccurrences.put("trap", newOccurrences);
                    view.repaint();
                    if (newOccurrences >= maxCycles * 2) { // Arrêter après maxCycles * 2 toggles
                        trapTimer.stop();
                    }
                }
            });
            trapTimer.setInitialDelay(frequency / 2);
            trapTimer.start();
        }
    }

    public int getLevel() {
        return level;
    }

    public void setModel(MazeModel newModel) {
        this.model = newModel;
        stopMovement();
        if (wallChangeTimer != null) wallChangeTimer.stop();
        if (trapTimer != null) trapTimer.stop();
        view.setPlayerOffset(0, 0, 0);
        view.setModel(newModel);
        view.resetView();
        view.repaint();
        view.requestFocusInWindow();
        setupLevelTimers();
        gameOver = false;
        obstacleOccurrences.clear();
    }

    private void setupKeyBindings() {
        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver) return;
                int key = e.getKeyCode();
                int currentX = model.getPlayerX();
                int currentY = model.getPlayerY();

                if (key == KeyEvent.VK_UP) directions[0] = true;
                else if (key == KeyEvent.VK_DOWN) directions[1] = true;
                else if (key == KeyEvent.VK_LEFT) directions[2] = true;
                else if (key == KeyEvent.VK_RIGHT) directions[3] = true;
                else if (key == KeyEvent.VK_R && model.getPlayerX() == model.getExitX() && model.getPlayerY() == model.getExitY()) {
                    nextLevel();
                    return;
                } else if (key == KeyEvent.VK_Q) {
                    soundManager.stopBackgroundMusic();
                    if (wallChangeTimer != null) wallChangeTimer.stop();
                    if (trapTimer != null) trapTimer.stop();
                    System.exit(0);
                }

                if (moveTimer == null || !moveTimer.isRunning()) {
                    startContinuousMovement();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (gameOver) return;
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_UP) directions[0] = false;
                else if (key == KeyEvent.VK_DOWN) directions[1] = false;
                else if (key == KeyEvent.VK_LEFT) directions[2] = false;
                else if (key == KeyEvent.VK_RIGHT) directions[3] = false;

                if (!isAnyDirectionPressed() && moveTimer != null && moveTimer.isRunning()) {
                    moveTimer.stop();
                    view.setPlayerOffset(0, 0, 0);
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
        view.setPlayerOffset(0, 0, 0);
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
            if (gameOver) {
                moveTimer.stop();
                return;
            }
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

            int animationFrame = step[0] % 2;
            view.setPlayerOffset(dx * step[0], dy * step[0], animationFrame);
            view.repaint();

            if (step[0] >= steps) {
                step[0] = 0;
                model.setPlayerX(targetX);
                model.setPlayerY(targetY);
                view.setPlayerOffset(0, 0, 0);
                soundManager.playSoundEffect("blip.wav");
                checkTrapCollision();
                if (model.getPlayerX() == model.getExitX() && model.getPlayerY() == model.getExitY()) {
                    soundManager.stopBackgroundMusic();
                    soundManager.playSoundEffect("organ.wav");
                    view.setGameWon(true);
                    gameOver = true;
                    stopMovement();
                }
                if (!isAnyDirectionPressed() && !gameOver) {
                    moveTimer.stop();
                }
            }
        });
        moveTimer.start();
    }

    private void checkTrapCollision() {
        if (level >= 6 && model.getPlayerX() == model.getTrapX() && model.getPlayerY() == model.getTrapY() && model.isTrapOpen()) {
            lives--;
            if (lives <= 0) {
                JOptionPane.showMessageDialog(null, "Game Over ! Plus de vies.", "Défaite", JOptionPane.ERROR_MESSAGE);
                soundManager.stopBackgroundMusic();
                if (wallChangeTimer != null) wallChangeTimer.stop();
                if (trapTimer != null) trapTimer.stop();
                System.exit(0);
            } else {
                view.startTrapAnimation(() -> {
                    model.setPlayerX(0);
                    model.setPlayerY(1);
                    view.repaint();
                });
            }
        }
    }

    public void nextLevel() {
        level++;
        LevelConfig config = levelManager.getLevelConfig(level);
        this.lives = config.getLives();
        MazeModel newModel = new MazeModel(level);
        setModel(newModel);
        soundManager.playBackgroundMusic("king_tubby_01.wav");
        Main.savePlayerLevel(playerName, level);
        ((JFrame) view.getTopLevelAncestor()).setTitle("Amazing Maze - Niveau " + level);
    }
}