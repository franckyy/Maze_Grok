package com.francky.projet.maze_Grok.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.francky.projet.maze_Grok.model.MazeModel;
import com.francky.projet.maze_Grok.view.MazeView;

public class MazeController {
	private MazeModel model;
    private MazeView view;

    public MazeController(MazeModel model, MazeView view) {
        this.model = model;
        this.view = view;
        setupKeyBindings();
    }

    private void setupKeyBindings() {
        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                int currentX = model.getPlayerX();
                int currentY = model.getPlayerY();
                
                if (key == KeyEvent.VK_UP && currentY > 0 && model.getMazeCell(currentY - 1, currentX) == 0) {
                    model.setPlayerY(currentY - 1);
                } else if (key == KeyEvent.VK_DOWN && currentY < model.getMazeHeight() - 1 && model.getMazeCell(currentY + 1, currentX) == 0) {
                    model.setPlayerY(currentY + 1);
                } else if (key == KeyEvent.VK_LEFT && currentX > 0 && model.getMazeCell(currentY, currentX - 1) == 0) {
                    model.setPlayerX(currentX - 1);
                } else if (key == KeyEvent.VK_RIGHT && currentX < model.getMazeWidth() - 1 && model.getMazeCell(currentY, currentX + 1) == 0) {
                    model.setPlayerX(currentX + 1);
                }
                view.repaint();
            }
        });
    }
}
