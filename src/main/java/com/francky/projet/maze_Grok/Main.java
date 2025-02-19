package com.francky.projet.maze_Grok;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.francky.projet.maze_Grok.controller.MazeController;
import com.francky.projet.maze_Grok.model.MazeModel;
import com.francky.projet.maze_Grok.view.MazeView;

public class Main {
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Options de taille pour le joueur
            String[] options = {"10 x 10", "20 x 20", "30 x 30"};
            String selectedSize = (String) JOptionPane.showInputDialog(
                null,
                "Choisissez la taille du labyrinthe :",
                "Taille du labyrinthe",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );

            if (selectedSize == null) {
                System.exit(0);
            }

            int size;
            switch (selectedSize) {
                case "10 x 10": size = 10; break;
                case "20 x 20": size = 20; break;
                case "30 x 30": size = 30; break;
                default: size = 10;
            }

            MazeModel model = new MazeModel(size / 2, size / 2);
            MazeView view = new MazeView(model, size); // Passe la taille choisie
            MazeController controller = new MazeController(model, view);

            JFrame frame = new JFrame("Maze Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(view);
            frame.pack();
            frame.setVisible(true);
            view.setFocusable(true);
            view.requestFocusInWindow();
        });
    }
}
