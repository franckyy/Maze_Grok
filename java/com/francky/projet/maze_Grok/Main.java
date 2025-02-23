package com.francky.projet.maze_Grok;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import com.francky.projet.maze_Grok.controller.MazeController;
import com.francky.projet.maze_Grok.model.MazeModel;
import com.francky.projet.maze_Grok.view.MazeView;

public class Main {
    private static final String PLAYERS_FILE_RESOURCE = "/com/francky/projet/maze_Grok/resources/players_level.txt";
    private static final String DB_URL = "jdbc:h2:mem:maze;DB_CLOSE_DELAY=-1"; // Base en mémoire
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        initializeDatabase();
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

    private static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS PLAYERS (NAME VARCHAR(255) PRIMARY KEY, LEVEL INT)");

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(Main.class.getResourceAsStream(PLAYERS_FILE_RESOURCE)))) {
                if (reader != null) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("=");
                        if (parts.length == 2) {
                            String name = parts[0].trim();
                            int level = Integer.parseInt(parts[1].trim());
                            String sql = "INSERT INTO PLAYERS (NAME, LEVEL) VALUES (?, ?) ON DUPLICATE KEY UPDATE LEVEL = ?";
                            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                                pstmt.setString(1, name);
                                pstmt.setInt(2, level);
                                pstmt.setInt(3, level);
                                pstmt.executeUpdate();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture initiale de " + PLAYERS_FILE_RESOURCE + " : " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'initialisation de la base H2 : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int loadPlayerLevel(String playerName) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT LEVEL FROM PLAYERS WHERE NAME = ?")) {
            pstmt.setString(1, playerName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("LEVEL");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la lecture du niveau pour " + playerName + " : " + e.getMessage());
        }
        return 1; // Niveau par défaut si joueur non trouvé ou erreur
    }

    public static void savePlayerLevel(String playerName, int level) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO PLAYERS (NAME, LEVEL) VALUES (?, ?) ON DUPLICATE KEY UPDATE LEVEL = ?")) {
            pstmt.setString(1, playerName);
            pstmt.setInt(2, level);
            pstmt.setInt(3, level);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la sauvegarde du niveau pour " + playerName + " : " + e.getMessage());
            e.printStackTrace();
        }
    }
}