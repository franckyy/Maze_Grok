package com.francky.projet.maze_Grok.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class LevelManager {
    private Map<Integer, LevelConfig> levels;

    public LevelManager() {
        levels = new HashMap<>();
        loadLevelsFromFile();
    }

    private void loadLevelsFromFile() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/com/francky/projet/maze_Grok/resources/levels.txt")))) {
            String line;
            LevelConfig currentLevel = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("[level")) {
                    int levelNum = Integer.parseInt(line.substring(6, line.length() - 1));
                    currentLevel = new LevelConfig(levelNum);
                    levels.put(levelNum, currentLevel);
                } else if (currentLevel != null && !line.isEmpty()) {
                    String[] parts = line.split("=");
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    switch (key) {
                        case "mazeWidth": currentLevel.setMazeWidth(Integer.parseInt(value)); break;
                        case "mazeHeight": currentLevel.setMazeHeight(Integer.parseInt(value)); break;
                        case "obstacles": currentLevel.setObstacles(value); break;
                        case "obstacleCount": currentLevel.setObstacleCounts(value); break;
                        case "obstacleFrequency": currentLevel.setObstacleFrequencies(value); break;
                        case "lives": currentLevel.setLives(Integer.parseInt(value)); break;
                    }
                }
            }
        } catch (Exception e) {
            // Si le fichier est introuvable ou corrompu, remplir avec des niveaux par défaut
            for (int i = 1; i <= 7; i++) {
                levels.put(i, new LevelConfig(i));
            }
        }
    }

    public LevelConfig getLevelConfig(int level) {
        return levels.getOrDefault(level, new LevelConfig(level));
    }
}