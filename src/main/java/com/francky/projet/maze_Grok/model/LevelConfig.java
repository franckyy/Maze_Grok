package com.francky.projet.maze_Grok.model;

import java.util.*;

public class LevelConfig {
    private int levelNumber;
    private int mazeWidth;
    private int mazeHeight;
    private List<String> obstacles;
    private int wallCount;  // Remplace obstacleCounts pour wallChange
    private int trapCount;  // Remplace obstacleCounts pour trap
    private int wallFrequency;
    private int lives;
    private int trapOpenTime;
    private int trapClosedTime;

    public LevelConfig(int levelNumber) {
        this.levelNumber = levelNumber;
        this.mazeWidth = 10 + (levelNumber - 1) * 2;
        this.mazeHeight = mazeWidth;
        this.obstacles = new ArrayList<>();
        this.wallCount = 0;  // Valeur par défaut
        this.trapCount = 0;  // Valeur par défaut
        this.wallFrequency = 0;
        this.lives = 3;
        this.trapOpenTime = 2;
        this.trapClosedTime = 4;

        if (levelNumber >= 4) {
            obstacles.add("wallChange");
            wallCount = 1;
            wallFrequency = 10;
        }
        if (levelNumber >= 6) {
            obstacles.add("trap");
            trapCount = 1;
        }
    }

    // Setters
    public void setMazeWidth(int width) { this.mazeWidth = width; }
    public void setMazeHeight(int height) { this.mazeHeight = height; }
    public void setObstacles(String obstaclesStr) {
        this.obstacles = obstaclesStr.isEmpty() ? new ArrayList<>() : Arrays.asList(obstaclesStr.split(","));
    }
    public void setWallCount(int wallCount) { this.wallCount = wallCount; }  // Nouveau setter
    public void setTrapCount(int trapCount) { this.trapCount = trapCount; }  // Nouveau setter
    public void setWallFrequency(int wallFrequency) { this.wallFrequency = wallFrequency; }
    public void setLives(int lives) { this.lives = lives; }
    public void setTrapOpenTime(int trapOpenTime) { this.trapOpenTime = trapOpenTime; }
    public void setTrapClosedTime(int trapClosedTime) { this.trapClosedTime = trapClosedTime; }

    // Getters
    public int getLevelNumber() { return levelNumber; }
    public int getMazeWidth() { return mazeWidth; }
    public int getMazeHeight() { return mazeHeight; }
    public List<String> getObstacles() { return obstacles; }
    public int getWallCount() { return wallCount; }  // Nouveau getter
    public int getTrapCount() { return trapCount; }  // Nouveau getter
    public int getWallFrequency() { return wallFrequency; }
    public int getLives() { return lives; }
    public int getTrapOpenTime() { return trapOpenTime; }
    public int getTrapClosedTime() { return trapClosedTime; }
}