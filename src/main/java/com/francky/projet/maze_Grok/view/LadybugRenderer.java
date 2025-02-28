package com.francky.projet.maze_Grok.view;

import java.awt.Color;
import java.awt.Graphics2D;

public class LadybugRenderer {
    public void draw(Graphics2D g2d, int x, int y, int size, int frame) {
        // Corps principal (rond rouge)
        g2d.setColor(Color.RED);
        int bodySize = size; // Taille du corps = taille du carré
        g2d.fillOval(x, y, bodySize, bodySize);

        // Points noirs
        g2d.setColor(Color.BLACK);
        int spotSize = size / 5; // Taille des points
        g2d.fillOval(x + size / 4, y + size / 4, spotSize, spotSize); // Haut gauche
        g2d.fillOval(x + size * 2 / 3, y + size / 4, spotSize, spotSize); // Haut droit
        g2d.fillOval(x + size / 4, y + size * 2 / 3, spotSize, spotSize); // Bas gauche
        g2d.fillOval(x + size * 2 / 3, y + size * 2 / 3, spotSize, spotSize); // Bas droit

        // Tête (petit rond noir)
        g2d.setColor(Color.BLACK);
        int headSize = size / 3;
        g2d.fillOval(x + size * 2 / 3 - headSize / 2, y - headSize / 2, headSize, headSize);

        // Antennes (lignes courtes)
        g2d.drawLine(x + size * 2 / 3, y - headSize / 2, x + size * 3 / 4, y - headSize); // Antenne droite
        g2d.drawLine(x + size * 2 / 3, y - headSize / 2, x + size / 2, y - headSize); // Antenne gauche

        // Pattes (animation avec frame)
        g2d.setColor(Color.BLACK);
        int legOffset = (frame == 0) ? size / 8 : -size / 8;
        g2d.drawLine(x, y + size / 2, x - size / 4, y + size / 2 + legOffset); // Patte gauche avant
        g2d.drawLine(x, y + size * 3 / 4, x - size / 4, y + size * 3 / 4 + legOffset); // Patte gauche arrière
        g2d.drawLine(x + size, y + size / 2, x + size + size / 4, y + size / 2 + legOffset); // Patte droite avant
        g2d.drawLine(x + size, y + size * 3 / 4, x + size + size / 4, y + size * 3 / 4 + legOffset); // Patte droite arrière
    }
}