package com.francky.projet.maze_Grok;

import java.util.HashMap;
import java.util.Map;

public class HighScores {
    public Map<Integer, HighScoreEntry> levelHighScores; // Niveau -> temps + joueur

    public HighScores() {
        this.levelHighScores = new HashMap<>();
    }

    public static class HighScoreEntry {
        public int time;
        public String player;

        public HighScoreEntry(int time, String player) {
            this.time = time;
            this.player = player;
        }
    }
}