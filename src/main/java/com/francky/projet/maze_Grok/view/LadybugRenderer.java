package com.francky.projet.maze_Grok.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class LadybugRenderer {
    public void draw(Graphics2D g2d, int x, int y, int size, int frame, int direction) {
        // Sauvegarde de la transformation initiale
        AffineTransform originalTransform = g2d.getTransform();

        // Taille réduite de la coccinelle (70% du carré)
        int ladybugSize = (int) (size * 0.7);
        int offset = (size - ladybugSize) / 2;

        // Calculer l'angle en fonction de la direction (corrigé)
        double angle = 0;
        switch (direction) {
            case 0: angle = 0; break;            // Haut
            case 1: angle = Math.PI; break;      // Bas
            case 2: angle = -Math.PI / 2; break; // Gauche
            case 3: angle = Math.PI / 2; break;  // Droite
            default: angle = Math.PI / 2; break; // Par défaut : droite
        }

        // Appliquer la rotation autour du centre de la coccinelle
        g2d.translate(x + size / 2, y + size / 2);
        g2d.rotate(angle);
        g2d.translate(-ladybugSize / 2, -ladybugSize / 2);

        // Corps : deux demi-cercles qui se touchent au centre
        g2d.setColor(Color.RED);
        g2d.fillArc(0, 0, ladybugSize, ladybugSize, 0, 180);    // Aile droite (haut)
        g2d.fillArc(0, 0, ladybugSize, ladybugSize, 180, 180);  // Aile gauche (bas)

        // Ligne de séparation au centre
        g2d.setColor(Color.BLACK);
        g2d.drawLine(ladybugSize / 2, 0, ladybugSize / 2, ladybugSize);

        // Points noirs sur les ailes
        int spotSize = ladybugSize / 5;
        g2d.fillOval(ladybugSize / 4, ladybugSize / 4, spotSize, spotSize);           // Aile droite haut
        g2d.fillOval(ladybugSize * 3 / 4 - spotSize, ladybugSize / 4, spotSize, spotSize); // Aile droite bas
        g2d.fillOval(ladybugSize / 4, ladybugSize * 3 / 4 - spotSize, spotSize, spotSize); // Aile gauche haut
        g2d.fillOval(ladybugSize * 3 / 4 - spotSize, ladybugSize * 3 / 4 - spotSize, spotSize, spotSize); // Aile gauche bas

        // Tête : demi-cercle noir orienté vers l'avant (haut)
        g2d.setColor(Color.BLACK);
        int headSize = ladybugSize / 3;
        g2d.fillArc(ladybugSize / 2 - headSize / 2, -headSize / 2, headSize, headSize, 0, 180);

        // Antennes
        g2d.drawLine(ladybugSize / 2 - headSize / 4, -headSize / 2, ladybugSize / 2 - headSize / 2, -headSize); // Gauche
        g2d.drawLine(ladybugSize / 2 + headSize / 4, -headSize / 2, ladybugSize / 2 + headSize / 2, -headSize); // Droite

        // Pattes (animation avec frame)
        int legOffset = (frame == 0) ? ladybugSize / 8 : -ladybugSize / 8;
        g2d.drawLine(0, ladybugSize / 2, -ladybugSize / 4, ladybugSize / 2 + legOffset); // Gauche avant
        g2d.drawLine(0, ladybugSize * 3 / 4, -ladybugSize / 4, ladybugSize * 3 / 4 + legOffset); // Gauche arrière
        g2d.drawLine(ladybugSize, ladybugSize / 2, ladybugSize + ladybugSize / 4, ladybugSize / 2 + legOffset); // Droite avant
        g2d.drawLine(ladybugSize, ladybugSize * 3 / 4, ladybugSize + ladybugSize / 4, ladybugSize * 3 / 4 + legOffset); // Droite arrière

        // Restaurer la transformation initiale
        g2d.setTransform(originalTransform);
    }
}