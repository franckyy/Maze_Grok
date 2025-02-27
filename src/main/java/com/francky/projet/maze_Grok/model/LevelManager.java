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
                new InputStreamReader(getClass().getResourceAsStream("/levels.txt")))) {
            System.out.println("Chargement de levels.txt depuis /levels.txt");
            String line;
            LevelConfig currentLevel = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                System.out.println("Ligne lue : '" + line + "'");
                if (line.startsWith("[level")) {
                    int levelNum = Integer.parseInt(line.substring(6, line.length() - 1));
                    System.out.println("Niveau détecté : " + levelNum);
                    currentLevel = new LevelConfig(levelNum);
                    levels.put(levelNum, currentLevel);
                } else if (currentLevel != null && !line.isEmpty()) {
                    String[] parts = line.split("=");
                    if (parts.length < 2) continue;
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    System.out.println("Clé : " + key + ", Valeur : " + value);
                    switch (key) {
                        case "mazeWidth": 
                            currentLevel.setMazeWidth(Integer.parseInt(value)); 
                            break;
                        case "mazeHeight": 
                            currentLevel.setMazeHeight(Integer.parseInt(value)); 
                            break;
                        case "obstacles": 
                            currentLevel.setObstacles(value); 
                            break;
                        case "wallCount": 
                            currentLevel.setWallCount(Integer.parseInt(value)); 
                            break;
                        case "trapCount": 
                            currentLevel.setTrapCount(Integer.parseInt(value)); 
                            break;
                        case "wallFrequency": 
                            currentLevel.setWallFrequency(Integer.parseInt(value)); 
                            break;
                        case "lives": 
                            currentLevel.setLives(Integer.parseInt(value)); 
                            break;
                        case "trapOpenTime": 
                            currentLevel.setTrapOpenTime(Integer.parseInt(value)); 
                            break;
                        case "trapClosedTime": 
                            currentLevel.setTrapClosedTime(Integer.parseInt(value)); 
                            break;
                    }
                }
            }
            System.out.println("Chargement terminé. Niveaux chargés : " + levels.keySet());
            for (Integer level : levels.keySet()) {
                LevelConfig config = levels.get(level);
                System.out.println("Niveau " + level + " : obstacles=" + config.getObstacles() + ", wallCount=" + config.getWallCount() + ", wallFrequency=" + config.getWallFrequency());
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de levels.txt : " + e.getMessage());
            for (int i = 1; i <= 8; i++) {
                levels.put(i, new LevelConfig(i));
            }
        }
    }

    public LevelConfig getLevelConfig(int level) {
        return levels.getOrDefault(level, new LevelConfig(level));
    }
}