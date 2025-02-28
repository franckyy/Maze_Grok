package com.francky.projet.maze_Grok;

import java.util.HashMap;
import java.util.Map;

public class HighScores {
    public Map<Integer, Integer> levelHighScores; // Niveau -> meilleur temps
    public int totalHighScore; // Meilleur temps total global

    public HighScores() {
        this.levelHighScores = new HashMap<>();
        this.totalHighScore = Integer.MAX_VALUE; // Initialisé à une valeur maximale
    }
}