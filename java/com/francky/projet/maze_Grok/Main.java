package com.francky.projet.maze_Grok;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.francky.projet.maze_Grok.controller.MazeController;
import com.francky.projet.maze_Grok.model.MazeModel;
import com.francky.projet.maze_Grok.view.MazeView;

public class Main {// Constantes pour les options de taille
	private static final String[] SIZE_OPTIONS = {"10 x 10", "20 x 20", "30 x 30"};

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        String selectedSize = (String) JOptionPane.showInputDialog(
            null,
            "Choisissez la taille du labyrinthe :",
            "Taille du labyrinthe",
            JOptionPane.QUESTION_MESSAGE,
            null,
            SIZE_OPTIONS,
            SIZE_OPTIONS[0]
        );

        if (selectedSize == null) {
            JOptionPane.showMessageDialog(null, "Au revoir !", "Fermeture", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        int size = parseSize(selectedSize);

        MazeModel model = new MazeModel(size / 2, size / 2);
        MazeView view = new MazeView(model, size);
        MazeController controller = new MazeController(model, view);
        view.setController(controller); // Lier le contrôleur à la vue

        JFrame frame = new JFrame("Amazing maze !");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(view);
        frame.pack();
        frame.setMaximumSize(new Dimension(800, 800));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        view.setFocusable(true);
        view.requestFocusInWindow();
    }

    private static int parseSize(String selectedSize) {
        switch (selectedSize) {
            case "10 x 10": return 10;
            case "20 x 20": return 20;
            case "30 x 30": return 30;
            default: return 10;
        }
    }
}
