package com.francky.projet.maze_Grok.model;

import java.util.*;

public class MazeModel {
    private static final Random RANDOM = new Random();
    private int[][] maze;
    private int playerX, playerY;
    private int exitX, exitY;
    private int movingWallX, movingWallY;
    private int trapX, trapY;
    private boolean trapOpen = false;
    private Set<String> modifiedBlocks = new HashSet<>(); // Suivi des blocs modifiés

    public MazeModel(int level) {
        int size = getSizeForLevel(level) / 2;
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
                modifiedBlocks.add(x + "," + y);
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

    // Vérifie s’il existe un chemin entre (startX, startY) et (goalX, goalY)
    private boolean hasPathToExit(int startX, int startY, int goalX, int goalY) {
        boolean[][] visited = new boolean[maze.length][maze[0].length];
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startX, startY});

        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int x = current[0];
            int y = current[1];

            if (x == goalX && y == goalY) {
                return true;
            }

            if (visited[y][x] || maze[y][x] == 1) {
                continue;
            }

            visited[y][x] = true;

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (nx >= 0 && nx < maze[0].length && ny >= 0 && ny < maze.length && !visited[ny][nx] && maze[ny][nx] == 0) {
                    stack.push(new int[]{nx, ny});
                }
            }
        }
        return false;
    }

    // Trouve un chemin actuel entre joueur et sortie
    private List<int[]> findCurrentPath() {
        List<int[]> path = new ArrayList<>();
        boolean[][] visited = new boolean[maze.length][maze[0].length];
        Stack<int[]> stack = new Stack<>();
        Map<String, int[]> parent = new HashMap<>();
        stack.push(new int[]{playerX, playerY});

        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int x = current[0];
            int y = current[1];

            if (x == exitX && y == exitY) {
                // Reconstruire le chemin
                int[] pos = new int[]{x, y};
                while (pos != null) {
                    path.add(0, pos);
                    pos = parent.getOrDefault(pos[0] + "," + pos[1], null);
                }
                return path;
            }

            if (visited[y][x] || maze[y][x] == 1) {
                continue;
            }

            visited[y][x] = true;

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (nx >= 0 && nx < maze[0].length && ny >= 0 && ny < maze.length && !visited[ny][nx] && maze[ny][nx] == 0) {
                    stack.push(new int[]{nx, ny});
                    parent.put(nx + "," + ny, new int[]{x, y});
                }
            }
        }
        return path;
    }

    public void breakRandomWall() {
        int attempts = 0;
        int[][] backup = new int[maze.length][maze[0].length];
        for (int i = 0; i < maze.length; i++) {
            System.arraycopy(maze[i], 0, backup[i], 0, maze[i].length);
        }
        List<int[]> currentPath = findCurrentPath();

        while (attempts < 100) {
            int x = RANDOM.nextInt(maze[0].length - 2) + 1;
            int y = RANDOM.nextInt(maze.length - 2) + 1;
            String posKey = x + "," + y;
            if (maze[y][x] == 1 && !modifiedBlocks.contains(posKey) && 
                !(x == playerX && y == playerY) && !(x == exitX && y == exitY)) {
                maze[y][x] = 0;
                if (hasPathToExit(playerX, playerY, exitX, exitY)) {
                    modifiedBlocks.add(posKey);
                    return;
                }
                for (int i = 0; i < maze.length; i++) {
                    System.arraycopy(backup[i], 0, maze[i], 0, maze[i].length);
                }
            }
            attempts++;
        }
    }

    public void createRandomWall() {
        int attempts = 0;
        int[][] backup = new int[maze.length][maze[0].length];
        for (int i = 0; i < maze.length; i++) {
            System.arraycopy(maze[i], 0, backup[i], 0, maze[i].length);
        }
        List<int[]> currentPath = findCurrentPath();

        while (attempts < 100) {
            // Prioriser un bloc près du chemin actuel
            int pathIndex = RANDOM.nextInt(currentPath.size() - 1); // Éviter la sortie
            int x = currentPath.get(pathIndex)[0] + RANDOM.nextInt(3) - 1; // Autour du chemin
            int y = currentPath.get(pathIndex)[1] + RANDOM.nextInt(3) - 1;
            String posKey = x + "," + y;
            if (x > 0 && x < maze[0].length - 1 && y > 0 && y < maze.length - 1 &&
                maze[y][x] == 0 && !modifiedBlocks.contains(posKey) && 
                !(x == playerX && y == playerY) && !(x == exitX && y == exitY)) {
                maze[y][x] = 1;
                if (hasPathToExit(playerX, playerY, exitX, exitY)) {
                    modifiedBlocks.add(posKey);
                    return;
                }
                for (int i = 0; i < maze.length; i++) {
                    System.arraycopy(backup[i], 0, maze[i], 0, maze[i].length);
                }
            }
            attempts++;
        }
    }

    public void moveWall() {
        if (movingWallX == -1 || movingWallY == -1) return;

        int[][] backup = new int[maze.length][maze[0].length];
        for (int i = 0; i < maze.length; i++) {
            System.arraycopy(maze[i], 0, backup[i], 0, maze[i].length);
        }
        List<int[]> currentPath = findCurrentPath();

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
            String newPosKey = newX + "," + newY;
            if (newX > 1 && newX < maze[0].length - 2 && newY > 1 && newY < maze.length - 2 &&
                maze[newY][newX] == 0 && !modifiedBlocks.contains(newPosKey) && 
                !(newX == playerX && newY == playerY) && !(newX == exitX && newY == exitY)) {
                maze[newY][newX] = 1;
                if (hasPathToExit(playerX, playerY, exitX, exitY)) {
                    modifiedBlocks.remove(movingWallX + "," + movingWallY);
                    modifiedBlocks.add(newPosKey);
                    movingWallX = newX;
                    movingWallY = newY;
                    return;
                }
                for (int i = 0; i < maze.length; i++) {
                    System.arraycopy(backup[i], 0, maze[i], 0, maze[i].length);
                }
            }
        }
        maze[movingWallY][movingWallX] = 1;
    }

    public void toggleTrap() {
        trapOpen = !trapOpen;
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
    public boolean isTrapOpen() { return trapOpen; }
}