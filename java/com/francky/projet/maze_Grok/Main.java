package com.francky.projet.maze_Grok;

import java.awt.Dimension;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import com.francky.projet.maze_Grok.controller.MazeController;
import com.francky.projet.maze_Grok.model.MazeModel;
import com.francky.projet.maze_Grok.view.MazeView;

public class Main {
    private static final String PLAYERS_FILE_RESOURCE = "/com/francky/projet/maze_Grok/resources/players_level.txt"; // Lecture depuis resources
    private static final String PLAYERS_FILE_LOCAL = "players_level.txt"; // Écriture dans le répertoire courant

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
        System.out.println("Niveau chargé pour " + playerName + " : " + level);
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
        // Essayer de lire d'abord depuis le fichier local
        File localFile = new File(PLAYERS_FILE_LOCAL);
        System.out.println("Tentative de lecture de " + localFile.getAbsolutePath());
        if (localFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(localFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Ligne lue (local) : " + line);
                    String[] parts = line.split("=");
                    if (parts.length == 2 && parts[0].trim().equals(playerName)) {
                        return Integer.parseInt(parts[1].trim());
                    }
                }
                System.out.println("Joueur " + playerName + " non trouvé dans le fichier local.");
            } catch (IOException | NumberFormatException e) {
                System.out.println("Erreur lors de la lecture de " + PLAYERS_FILE_LOCAL + " : " + e.getMessage());
            }
        } else {
            System.out.println("Fichier local " + PLAYERS_FILE_LOCAL + " non trouvé, tentative depuis resources.");
        }

        // Si pas trouvé localement, essayer depuis resources
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Main.class.getResourceAsStream(PLAYERS_FILE_RESOURCE)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Ligne lue (resources) : " + line);
                String[] parts = line.split("=");
                if (parts.length == 2 && parts[0].trim().equals(playerName)) {
                    return Integer.parseInt(parts[1].trim());
                }
            }
            System.out.println("Joueur " + playerName + " non trouvé dans les resources.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erreur lors de la lecture de " + PLAYERS_FILE_RESOURCE + " depuis resources ou fichier absent : " + e.getMessage());
        }

        return 1; // Niveau par défaut si joueur non trouvé ou erreur
    }

    public static void savePlayerLevel(String playerName, int level) {
        File file = new File(PLAYERS_FILE_LOCAL);
        System.out.println("Tentative d'écriture dans " + file.getAbsolutePath());
        Map<String, Integer> players = new HashMap<>();

        // Charger les données existantes depuis le fichier local
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Ligne existante lue (local) : " + line);
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        players.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
                    }
                }
            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture existante de " + PLAYERS_FILE_LOCAL + " : " + e.getMessage());
            }
        } else {
            // Si le fichier local n'existe pas, essayer de charger depuis resources
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(Main.class.getResourceAsStream(PLAYERS_FILE_RESOURCE)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Ligne existante lue (resources) : " + line);
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        players.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
                    }
                }
            } catch (IOException | NullPointerException e) {
                System.out.println("Aucune donnée initiale dans " + PLAYERS_FILE_RESOURCE + " ou fichier absent : " + e.getMessage());
            }
            System.out.println("Fichier " + PLAYERS_FILE_LOCAL + " n'existe pas encore, création en cours.");
        }

        // Mettre à jour ou ajouter le joueur actuel
        players.put(playerName, level);
        System.out.println("Sauvegarde de " + playerName + " au niveau " + level);

        // Réécrire toutes les données dans le fichier local
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, Integer> entry : players.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
            System.out.println("Fichier " + PLAYERS_FILE_LOCAL + " mis à jour avec succès.");
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture dans " + PLAYERS_FILE_LOCAL + " : " + e.getMessage());
            e.printStackTrace();
        }
    }
}