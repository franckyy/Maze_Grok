package com.francky.projet.maze_Grok;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.francky.projet.maze_Grok.controller.MazeController;
import com.francky.projet.maze_Grok.model.MazeModel;
import com.francky.projet.maze_Grok.view.InfoPanel;
import com.francky.projet.maze_Grok.view.MazeView;

public class Main {
    private static final String PLAYERS_FILE = "/home/oem/git/Maze_Grok/players_level.txt";
    private static final String TIMES_FILE = "/home/oem/git/Maze_Grok/player_times.txt";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        String playerName = JOptionPane.showInputDialog(null, "Entrez votre prénom :", "Bienvenue", JOptionPane.PLAIN_MESSAGE);
        if (playerName == null || playerName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Au revoir !", "Fermeture", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        int level = loadPlayerLevel(playerName);
        PlayerTimes playerTimes = loadPlayerTimes(playerName);
        MazeModel model = new MazeModel(level);
        MazeView view = new MazeView(model);
        InfoPanel infoPanel = new InfoPanel(level, playerTimes);
        MazeController controller = new MazeController(model, view, level, playerName, infoPanel);
        view.setController(controller);

        JFrame frame = new JFrame("Amazing Maze - Niveau " + level);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel mazeContainer = new JPanel(new GridBagLayout());
        mazeContainer.setOpaque(false);
        mazeContainer.add(view);

        frame.add(infoPanel, BorderLayout.NORTH);
        frame.add(mazeContainer, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        view.setFocusable(true);
        view.requestFocusInWindow();
    }

    public static int loadPlayerLevel(String playerName) {
        File file = new File(PLAYERS_FILE);
        if (!file.exists()) {
            return 1;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2 && parts[0].trim().equals(playerName)) {
                    return Integer.parseInt(parts[1].trim());
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erreur lors de la lecture de " + PLAYERS_FILE + " : " + e.getMessage());
        }
        return 1;
    }

    public static PlayerTimes loadPlayerTimes(String playerName) {
        File file = new File(TIMES_FILE);
        Map<String, PlayerTimes> players = new HashMap<>();

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        String name = parts[0].trim();
                        String[] times = parts[1].split(",");
                        if (times.length >= 5) {
                            int lastLevel = Integer.parseInt(times[0].trim());
                            int lastLevelTime = Integer.parseInt(times[1].trim());
                            int lastTotalTime = Integer.parseInt(times[2].trim());
                            Map<Integer, Integer> highScores = new HashMap<>();
                            String[] highScoreParts = times[3].split(":");
                            for (String part : highScoreParts) {
                                String[] levelTime = part.split("-");
                                if (levelTime.length == 2) {
                                    highScores.put(Integer.parseInt(levelTime[0]), Integer.parseInt(levelTime[1]));
                                }
                            }
                            int highScoreTotal = Integer.parseInt(times[4].trim());
                            players.put(name, new PlayerTimes(lastLevel, lastLevelTime, lastTotalTime, highScores, highScoreTotal));
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture de " + TIMES_FILE + " : " + e.getMessage());
            }
        }

        return players.getOrDefault(playerName, new PlayerTimes(0, 0, 0, new HashMap<>(), 0));
    }

    public static void savePlayerTimes(String playerName, int lastLevel, int lastLevelTime, int lastTotalTime, Map<Integer, Integer> highScores, int highScoreTotal) {
        File file = new File(TIMES_FILE);
        Map<String, PlayerTimes> players = new HashMap<>();

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        String name = parts[0].trim();
                        String[] times = parts[1].split(",");
                        if (times.length >= 5) {
                            int level = Integer.parseInt(times[0].trim());
                            int levelTime = Integer.parseInt(times[1].trim());
                            int totalTime = Integer.parseInt(times[2].trim());
                            Map<Integer, Integer> scores = new HashMap<>();
                            String[] highScoreParts = times[3].split(":");
                            for (String part : highScoreParts) {
                                String[] levelTimePair = part.split("-");
                                if (levelTimePair.length == 2) {
                                    scores.put(Integer.parseInt(levelTimePair[0]), Integer.parseInt(levelTimePair[1]));
                                }
                            }
                            int highTotal = Integer.parseInt(times[4].trim());
                            players.put(name, new PlayerTimes(level, levelTime, totalTime, scores, highTotal));
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture de " + TIMES_FILE + " : " + e.getMessage());
            }
        }

        players.put(playerName, new PlayerTimes(lastLevel, lastLevelTime, lastTotalTime, highScores, highScoreTotal));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, PlayerTimes> entry : players.entrySet()) {
                String name = entry.getKey();
                PlayerTimes times = entry.getValue();
                StringBuilder highScoresStr = new StringBuilder();
                for (Map.Entry<Integer, Integer> score : times.highScores.entrySet()) {
                    if (highScoresStr.length() > 0) highScoresStr.append(":");
                    highScoresStr.append(score.getKey()).append("-").append(score.getValue());
                }
                writer.write(name + "=" + times.lastLevel + "," + times.lastLevelTime + "," + times.lastTotalTime + "," + highScoresStr + "," + times.highScoreTotal);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture dans " + TIMES_FILE + " : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void savePlayerLevel(String playerName, int level) {
        File file = new File(PLAYERS_FILE);
        Map<String, Integer> players = new HashMap<>();

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        players.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
                    }
                }
            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture de " + PLAYERS_FILE + " : " + e.getMessage());
            }
        }

        players.put(playerName, level);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, Integer> entry : players.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture dans " + PLAYERS_FILE + " : " + e.getMessage());
            e.printStackTrace();
        }
    }
}