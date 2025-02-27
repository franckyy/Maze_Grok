package com.francky.projet.maze_Grok.view;

import java.awt.Color;
import java.awt.Graphics2D;

public class RobotRenderer {
    public void draw(Graphics2D g2d, int x, int y, int size, int frame) {
        // Corps cylindrique gris métallique
        g2d.setColor(new Color(150, 150, 150)); // Gris métallique
        int bodyWidth = size * 2 / 3; // Largeur du cylindre
        int bodyHeight = size * 4 / 3; // Hauteur plus grande
        g2d.fillRect(x + size / 6, y + size / 4, bodyWidth, bodyHeight); // Corps principal
        g2d.setColor(new Color(100, 100, 100)); // Ombre pour effet 3D
        g2d.fillRect(x + size / 6 + bodyWidth - size / 12, y + size / 4, size / 12, bodyHeight); // Côté droit sombre

        // Tête cylindrique gris métallique
        g2d.setColor(new Color(150, 150, 150)); // Gris métallique
        int headWidth = size * 2 / 3; // Même largeur que le corps
        int headHeight = size / 2; // Hauteur plus courte
        g2d.fillRect(x + size / 6, y, headWidth, headHeight); // Tête principale
        g2d.setColor(new Color(100, 100, 100)); // Ombre pour effet 3D
        g2d.fillRect(x + size / 6 + headWidth - size / 12, y, size / 12, headHeight); // Côté droit sombre

        // Yeux blancs avec pupilles noires
        g2d.setColor(Color.WHITE);
        int eyeSize = headHeight / 3;
        g2d.fillOval(x + size / 4 - eyeSize / 2, y + headHeight / 4 - eyeSize / 2, eyeSize, eyeSize);
        g2d.fillOval(x + size / 2 - eyeSize / 2, y + headHeight / 4 - eyeSize / 2, eyeSize, eyeSize);
        g2d.setColor(Color.BLACK);
        int pupilSize = eyeSize / 2;
        g2d.fillOval(x + size / 4 - pupilSize / 2, y + headHeight / 4 - pupilSize / 2, pupilSize, pupilSize);
        g2d.fillOval(x + size / 2 - pupilSize / 2, y + headHeight / 4 - pupilSize / 2, pupilSize, pupilSize);

        // Bouche souriante
        g2d.setColor(Color.BLACK);
        g2d.drawArc(x + size / 4, y + headHeight / 2, size / 3, size / 6, 0, -180); // Sourire vers le haut

        // Antennes courtes
        g2d.setColor(Color.BLACK);
        g2d.drawLine(x + size / 3, y, x + size / 3, y - size / 4); // Antenne gauche
        g2d.drawLine(x + size / 2, y, x + size / 2, y - size / 4); // Antenne droite
        g2d.fillOval(x + size / 3 - size / 16, y - size / 4 - size / 16, size / 8, size / 8); // Bout de l’antenne gauche
        g2d.fillOval(x + size / 2 - size / 16, y - size / 4 - size / 16, size / 8, size / 8); // Bout de l’antenne droite

        // Bras (animation : oscillation)
        g2d.setColor(new Color(150, 150, 150)); // Gris métallique
        int armOffset = (frame == 0) ? size / 8 : -size / 8; // Frame 0 : bras bas, Frame 1 : bras haut
        g2d.fillOval(x - size / 8, y + size / 2 + armOffset, size / 4, size / 4); // Bras gauche
        g2d.fillOval(x + size - size / 8, y + size / 2 + armOffset, size / 4, size / 4); // Bras droit

        // Jambes courtes (ajustées au corps cylindrique)
        g2d.fillRect(x + size / 4, y + bodyHeight + size / 4, size / 8, size / 4); // Jambe gauche
        g2d.fillRect(x + size / 2, y + bodyHeight + size / 4, size / 8, size / 4); // Jambe droite
    }
}