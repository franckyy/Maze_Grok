package com.francky.projet.maze_Grok;

import java.util.HashMap;
import java.util.Map;

public class PlayerTimes {
    public int lastLevel;
    public int lastLevelTime;
    public int lastTotalTime;
    public Map<Integer, Integer> highScores;
    public int highScoreTotal;
    public int lives; // Nouveau champ pour les vies

    public PlayerTimes(int lastLevel, int lastLevelTime, int lastTotalTime, Map<Integer, Integer> highScores, int highScoreTotal, int lives) {
        this.lastLevel = lastLevel;
        this.lastLevelTime = lastLevelTime;
        this.lastTotalTime = lastTotalTime;
        this.highScores = highScores;
        this.highScoreTotal = highScoreTotal;
        this.lives = lives;
    }
}