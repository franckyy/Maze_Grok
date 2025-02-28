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
        MazeModel model = new MazeModel(level);
        MazeView view = new MazeView(model);
        InfoPanel infoPanel = new InfoPanel(level);
        MazeController controller = new MazeController(model, view, level, playerName, infoPanel);
        view.setController(controller);

        JFrame frame = new JFrame("Amazing Maze - Niveau " + level);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        // Panneau conteneur pour centrer le labyrinthe
        JPanel mazeContainer = new JPanel(new GridBagLayout());
        mazeContainer.setOpaque(false); // Fond transparent pour voir le blanc derrière
        mazeContainer.add(view); // MazeView reste 600x600

        // Ajouter les panneaux
        frame.add(infoPanel, BorderLayout.NORTH);
        frame.add(mazeContainer, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        view.setFocusable(true);
        view.requestFocusInWindow();
    }

    private static int loadPlayerLevel(String playerName) {
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