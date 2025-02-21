package com.francky.projet.maze_Grok.model;

import java.util.Random;

public class MazeModel {
    private static final Random RANDOM = new Random();
    private int[][] maze;
    private int playerX, playerY;
    private int exitX, exitY;
    private int movingWallX, movingWallY;
    private int trapX, trapY;

    public MazeModel(int level) {
        int size = getSizeForLevel(level); // Utiliser la taille selon le niveau
        maze = new int[2 * size + 1][2 * size + 1];
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                maze[i][j] = 1;
            }
        }
        carvePath(1, 1);
        maze[1][0] = 0;
        maze[1][1] = 0;
        int exitYIndex = 2 * size - 1;
        maze[exitYIndex][2 * size] = 0;
        maze[exitYIndex][2 * size - 1] = 0;
        exitX = 2 * size;
        exitY = exitYIndex;
        playerX = 0;
        playerY = 1;

        initMovingWall();
        initTrap();
    }

    private void carvePath(int x, int y) {
        maze[y][x] = 0;
        int[] directions = {0, 1, 2, 3};
        shuffleArray(directions);

        for (int dir : directions) {
            int nx = x, ny = y;
            switch (dir) {
                case 0: ny -= 2; break;
                case 1: ny += 2; break;
                case 2: nx += 2; break;
                case 3: nx -= 2; break;
            }
            if (nx >= 0 && nx < maze[0].length && ny >= 0 && ny < maze.length && maze[ny][nx] == 1) {
                maze[y + (ny - y) / 2][x + (nx - x) / 2] = 0;
                maze[ny][nx] = 0;
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

    // Nouvelle méthode pour déterminer la taille selon le niveau
    public static int getSizeForLevel(int level) {
        switch (level) {
            case 1: return 10;
            case 2: return 12;
            case 3: return 14;
            case 4: return 16;
            case 5: return 18;
            case 6: return 20;
            case 7: return 22;
            default: return 10;
        }
    }

    private void initMovingWall() {
        int attempts = 0;
        while (attempts < 100) {
            int x = RANDOM.nextInt(maze[0].length - 4) + 2;
            int y = RANDOM.nextInt(maze.length - 4) + 2;
            if (maze[y][x] == 1 && !(x == playerX && y == playerY) && !(x == exitX && y == exitY)) {
                movingWallX = x;
                movingWallY = y;
                return;
            }
            attempts++;
        }
        movingWallX = -1;
        movingWallY = -1;
    }

    private void initTrap() {
        int attempts = 0;
        while (attempts < 100) {
            int x = exitX - RANDOM.nextInt(3) - 1;
            int y = exitY - RANDOM.nextInt(3) - 1;
            if (x > 0 && x < maze[0].length - 1 && y > 0 && y < maze.length - 1 &&
                maze[y][x] == 0 && !(x == playerX && y == playerY) && !(x == exitX && y == exitY)) {
                trapX = x;
                trapY = y;
                return;
            }
            attempts++;
        }
        trapX = -1;
        trapY = -1;
    }

    public void breakRandomWall() {
        int attempts = 0;
        while (attempts < 100) {
            int x = RANDOM.nextInt(maze[0].length - 2) + 1;
            int y = RANDOM.nextInt(maze.length - 2) + 1;
            if (maze[y][x] == 1 && !(x == playerX && y == playerY) && !(x == exitX && y == exitY)) {
                maze[y][x] = 0;
                return;
            }
            attempts++;
        }
    }

    public void createRandomWall() {
        int attempts = 0;
        while (attempts < 100) {
            int x = RANDOM.nextInt(maze[0].length - 2) + 1;
            int y = RANDOM.nextInt(maze.length - 2) + 1;
            if (maze[y][x] == 0 && !(x == playerX && y == playerY) && !(x == exitX && y == exitY)) {
                maze[y][x] = 1;
                return;
            }
            attempts++;
        }
    }

    public void moveWall() {
        if (movingWallX == -1 || movingWallY == -1) return;
        maze[movingWallY][movingWallX] = 0;
        int[] directions = {0, 1, 2, 3};
        shuffleArray(directions);
        for (int dir : directions) {
            int newX = movingWallX, newY = movingWallY;
            switch (dir) {
                case 0: newY--; break;
                case 1: newY++; break;
                case 2: newX++; break;
                case 3: newX--; break;
            }
            if (newX > 1 && newX < maze[0].length - 2 && newY > 1 && newY < maze.length - 2 &&
                maze[newY][newX] == 0 && !(newX == playerX && newY == playerY) && !(newX == exitX && newY == exitY)) {
                movingWallX = newX;
                movingWallY = newY;
                maze[newY][newX] = 1;
                return;
            }
        }
        maze[movingWallY][movingWallX] = 1;
    }

    public int getPlayerX() { return playerX; }
    public void setPlayerX(int x) {
        if (x >= 0 && x < maze[0].length && maze[playerY][x] == 0) playerX = x;
    }
    public int getPlayerY() { return playerY; }
    public void setPlayerY(int y) {
        if (y >= 0 && y < maze.length && maze[y][playerX] == 0) playerY = y;
    }
    public int getMazeCell(int y, int x) {
        if (y >= 0 && y < maze.length && x >= 0 && x < maze[0].length) return maze[y][x];
        return 1;
    }
    public int getMazeHeight() { return maze.length; }
    public int getMazeWidth() { return maze[0].length; }
    public int getExitX() { return exitX; }
    public int getExitY() { return exitY; }
    public int getTrapX() { return trapX; }
    public int getTrapY() { return trapY; }
}