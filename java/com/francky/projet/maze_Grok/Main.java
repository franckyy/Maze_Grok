package com.francky.projet.maze_Grok;

import java.awt.Dimension;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import com.francky.projet.maze_Grok.controller.MazeController;
import com.francky.projet.maze_Grok.model.MazeModel;
import com.francky.projet.maze_Grok.view.MazeView;

public class Main {
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
        MazeController controller = new MazeController(model, view, level, playerName);
        view.setController(controller);

        JFrame frame = new JFrame("Amazing Maze - Niveau " + level);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(view);
        frame.pack();
        frame.setMaximumSize(new Dimension(800, 800));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        view.setFocusable(true);
        view.requestFocusInWindow();
    }

    private static int loadPlayerLevel(String playerName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(playerName + "_level.txt"))) {
            return Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            return 1;
        }
    }

    public static void savePlayerLevel(String playerName, int level) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(playerName + "_level.txt"))) {
            writer.write(String.valueOf(level));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}