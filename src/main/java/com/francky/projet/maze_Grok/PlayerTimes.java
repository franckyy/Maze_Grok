package com.francky.projet.maze_Grok;

import java.util.HashMap;
import java.util.Map;

public class PlayerTimes {
    public int lastLevel;           // Rendu public
    public int lastLevelTime;       // Rendu public
    public int lastTotalTime;       // Rendu public
    public Map<Integer, Integer> highScores; // Rendu public
    public int highScoreTotal;      // Rendu public

    public PlayerTimes(int lastLevel, int lastLevelTime, int lastTotalTime, Map<Integer, Integer> highScores, int highScoreTotal) {
        this.lastLevel = lastLevel;
        this.lastLevelTime = lastLevelTime;
        this.lastTotalTime = lastTotalTime;
        this.highScores = highScores;
        this.highScoreTotal = highScoreTotal;
    }
}