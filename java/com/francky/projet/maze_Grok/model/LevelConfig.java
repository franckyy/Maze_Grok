package com.francky.projet.maze_Grok.model;

import java.util.*;

public class LevelConfig {
    private int levelNumber;
    private int mazeWidth;
    private int mazeHeight;
    private List<String> obstacles;
    private List<Integer> obstacleCounts;
    private List<Integer> obstacleFrequencies;
    private int lives;

    public LevelConfig(int levelNumber) {
        this.levelNumber = levelNumber;
        // Valeurs par défaut si le fichier est absent
        this.mazeWidth = 10 + (levelNumber - 1) * 2; // 10, 12, 14, etc.
        this.mazeHeight = mazeWidth;
        this.obstacles = new ArrayList<>();
        this.obstacleCounts = new ArrayList<>();
        this.obstacleFrequencies = new ArrayList<>();
        this.lives = 3;

        // Ajouter des obstacles par défaut selon le niveau
        if (levelNumber >= 4) {
            obstacles.add("wallChange");
            obstacleCounts.add(1);
            obstacleFrequencies.add(10);
        }
        if (levelNumber >= 6) {
            obstacles.add("trap");
            obstacleCounts.add(1);
            obstacleFrequencies.add(8);
        }
    }

    // Setters pour charger depuis le fichier
    public void setMazeWidth(int width) { this.mazeWidth = width; }
    public void setMazeHeight(int height) { this.mazeHeight = height; }
    public void setObstacles(String obstaclesStr) {
        this.obstacles = obstaclesStr.isEmpty() ? new ArrayList<>() : Arrays.asList(obstaclesStr.split(","));
    }
    public void setObstacleCounts(String countsStr) {
        this.obstacleCounts = new ArrayList<>();
        if (!countsStr.isEmpty()) {
            for (String count : countsStr.split(",")) {
                this.obstacleCounts.add(Integer.parseInt(count.trim()));
            }
        }
    }
    public void setObstacleFrequencies(String freqsStr) {
        this.obstacleFrequencies = new ArrayList<>();
        if (!freqsStr.isEmpty()) {
            for (String freq : freqsStr.split(",")) {
                this.obstacleFrequencies.add(Integer.parseInt(freq.trim()));
            }
        }
    }
    public void setLives(int lives) { this.lives = lives; }

    // Getters
    public int getLevelNumber() { return levelNumber; }
    public int getMazeWidth() { return mazeWidth; }
    public int getMazeHeight() { return mazeHeight; }
    public List<String> getObstacles() { return obstacles; }
    public List<Integer> getObstacleCounts() { return obstacleCounts; }
    public List<Integer> getObstacleFrequencies() { return obstacleFrequencies; }
    public int getLives() { return lives; }
}