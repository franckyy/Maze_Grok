package com.francky.projet.maze_Grok.model;

import java.util.Random;

public class MazeModel {
	private static final Random RANDOM = new Random();
    private int[][] maze; // 0 pour passage, 1 pour mur
    private int playerX, playerY;
    private int exitX, exitY; // Coordonnées de la sortie

    public MazeModel(int width, int height) {
        // Grille plus grande pour inclure murs et passages
        maze = new int[2 * height + 1][2 * width + 1];
        // Initialise tout comme murs
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                maze[i][j] = 1;
            }
        }
        // Creuse les chemins à partir de (1, 1)
        carvePath(1, 1);

        // Ajouter l'entrée (bord gauche)
        maze[1][0] = 0; // Ouvre le mur extérieur à gauche
        maze[1][1] = 0; // Assure que l'entrée est connectée (déjà fait par carvePath)

        // Ajouter la sortie (bord droit)
        int exitYIndex = 2 * height - 1; // Dernière ligne "intérieure" (indice impair)
        maze[exitYIndex][2 * width] = 0; // Ouvre le mur extérieur à droite
        maze[exitYIndex][2 * width - 1] = 0; // Assure que la sortie est connectée
        exitX = 2 * width; // Coordonnées de la sortie
        exitY = exitYIndex;

        // Place le joueur à l'entrée
        playerX = 0; // Bord gauche
        playerY = 1;
    }

    private void carvePath(int x, int y) {
        maze[y][x] = 0; // Marque la cellule actuelle comme passage
        int[] directions = {0, 1, 2, 3}; // N, S, E, W
        shuffleArray(directions);

        for (int dir : directions) {
            int nx = x, ny = y;
            switch (dir) {
                case 0: ny -= 2; break; // Nord (déplacement de 2 cases)
                case 1: ny += 2; break; // Sud
                case 2: nx += 2; break; // Est
                case 3: nx -= 2; break; // Ouest
            }

            if (nx >= 0 && nx < maze[0].length && ny >= 0 && ny < maze.length && maze[ny][nx] == 1) {
                maze[y + (ny - y) / 2][x + (nx - x) / 2] = 0; // Creuse le mur entre
                maze[ny][nx] = 0; // Creuse la nouvelle cellule
                carvePath(nx, ny);
            }
        }
    }

    private void shuffleArray(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = RANDOM.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    // Getters et setters
    public int getPlayerX() { return playerX; }

    public void setPlayerX(int x) {
        if (x >= 0 && x < maze[0].length && maze[playerY][x] == 0) {
            playerX = x;
        }
    }

    public int getPlayerY() { return playerY; }

    public void setPlayerY(int y) {
        if (y >= 0 && y < maze.length && maze[y][playerX] == 0) {
            playerY = y;
        }
    }

    public int getMazeCell(int y, int x) {
        if (y >= 0 && y < maze.length && x >= 0 && x < maze[0].length) {
            return maze[y][x];
        }
        return 1; // Mur hors limites
    }

    public int getMazeHeight() { return maze.length; }

    public int getMazeWidth() { return maze[0].length; }

    // Nouveaux getters pour la sortie
    public int getExitX() { return exitX; }

    public int getExitY() { return exitY; }
    
}
