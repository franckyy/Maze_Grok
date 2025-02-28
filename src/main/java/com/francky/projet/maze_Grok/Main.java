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
    private static final String HIGH_SCORES_FILE = "/home/oem/git/Maze_Grok/high_scores.txt";

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
        HighScores highScores = loadHighScores(); // Charger les high scores globaux
        MazeModel model = new MazeModel(level);
        MazeView view = new MazeView(model);
        InfoPanel infoPanel = new InfoPanel(level, playerTimes, highScores);
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
                        if (times.length >= 3) { // Plus de high scores par joueur
                            int lastLevel = Integer.parseInt(times[0].trim());
                            int lastLevelTime = Integer.parseInt(times[1].trim());
                            int lastTotalTime = Integer.parseInt(times[2].trim());
                            players.put(name, new PlayerTimes(lastLevel, lastLevelTime, lastTotalTime, new HashMap<>(), 0));
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture de " + TIMES_FILE + " : " + e.getMessage());
            }
        }

        return players.getOrDefault(playerName, new PlayerTimes(0, 0, 0, new HashMap<>(), 0));
    }

    public static HighScores loadHighScores() {
        File file = new File(HIGH_SCORES_FILE);
        HighScores highScores = new HighScores();

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        if (parts[0].startsWith("level")) {
                            int level = Integer.parseInt(parts[0].substring(5));
                            int time = Integer.parseInt(parts[1].trim());
                            highScores.levelHighScores.put(level, time);
                        } else if (parts[0].equals("high_total")) {
                            highScores.totalHighScore = Integer.parseInt(parts[1].trim());
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture de " + HIGH_SCORES_FILE + " : " + e.getMessage());
            }
        }

        return highScores;
    }

    public static void savePlayerTimes(String playerName, int lastLevel, int lastLevelTime, int lastTotalTime) {
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
                        if (times.length >= 3) {
                            int level = Integer.parseInt(times[0].trim());
                            int levelTime = Integer.parseInt(times[1].trim());
                            int totalTime = Integer.parseInt(times[2].trim());
                            players.put(name, new PlayerTimes(level, levelTime, totalTime, new HashMap<>(), 0));
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture de " + TIMES_FILE + " : " + e.getMessage());
            }
        }

        players.put(playerName, new PlayerTimes(lastLevel, lastLevelTime, lastTotalTime, new HashMap<>(), 0));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, PlayerTimes> entry : players.entrySet()) {
                String name = entry.getKey();
                PlayerTimes times = entry.getValue();
                writer.write(name + "=" + times.lastLevel + "," + times.lastLevelTime + "," + times.lastTotalTime);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture dans " + TIMES_FILE + " : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void saveHighScores(HighScores highScores) {
        File file = new File(HIGH_SCORES_FILE);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<Integer, Integer> entry : highScores.levelHighScores.entrySet()) {
                writer.write("level" + entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
            writer.write("high_total=" + highScores.totalHighScore);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture dans " + HIGH_SCORES_FILE + " : " + e.getMessage());
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