package com.francky.projet.maze_Grok.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.francky.projet.maze_Grok.model.MazeModel;

public class MazeView extends JPanel {
	private MazeModel model;
	private int CELL_SIZE; // Calculé dynamiquement
	private final int PLAYER_SIZE_RATIO = 2; // Ratio pour PLAYER_SIZE (CELL_SIZE - 2 reste)
	private JButton replayButton;
	private JButton quitButton;
	private boolean gameWon = false;
	private float offsetX = 0, offsetY = 0; // Décalages pour l’animation

	public MazeView(MazeModel model) {
		this(model, 10); // Taille par défaut
	}

	public MazeView(MazeModel model, int mazeSize) {
		this.model = model;
		setPreferredSize(new Dimension(600, 600)); // Taille initiale suggérée
		initButtons(); // Initialiser les boutons
	}

	// Méthode pour récupérer CELL_SIZE depuis MazeController
    public int getCellSize() {
        return CELL_SIZE;
    }
    
	//Méthodes pour gérer le décalage du joueur
	public void setPlayerOffset(float offsetX, float offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	private void initButtons() {
		replayButton = new JButton("Rejouer");
		quitButton = new JButton("Quitter");
		replayButton.setVisible(false);
		quitButton.setVisible(false);
		add(replayButton);
		add(quitButton);

		replayButton.addActionListener(e -> restartGame());
		quitButton.addActionListener(e -> System.exit(0));
	}

	private void restartGame() {
		model = new MazeModel(model.getMazeWidth() / 2 - 1, model.getMazeHeight() / 2 - 1); // Recréer un labyrinthe
		gameWon = false;
		replayButton.setVisible(false);
		quitButton.setVisible(false);
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		CELL_SIZE = Math.min(getWidth() / model.getMazeWidth(), getHeight() / model.getMazeHeight());
		int PLAYER_SIZE = CELL_SIZE - PLAYER_SIZE_RATIO;

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		for (int y = 0; y < model.getMazeHeight(); y++) {
			for (int x = 0; x < model.getMazeWidth(); x++) {
				if (model.getMazeCell(y, x) == 1) {
					drawBrickWall(g2d, x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE);
				}
			}
		}

		g2d.setColor(Color.GREEN);
		int exitX = model.getExitX() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2;
		int exitY = model.getExitY() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2;
		g2d.fillOval(exitX, exitY, PLAYER_SIZE, PLAYER_SIZE);

		// Dessiner le joueur avec décalage
		g2d.setColor(Color.RED);
		int playerX = model.getPlayerX() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2 + (int) offsetX;
		int playerY = model.getPlayerY() * CELL_SIZE + (CELL_SIZE - PLAYER_SIZE) / 2 + (int) offsetY;
		g2d.fillOval(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);

		if (model.getPlayerX() == model.getExitX() && model.getPlayerY() == model.getExitY()) {
			gameWon = true;
			g2d.setFont(new Font("Arial", Font.BOLD, 30));
			int rectWidth = 120 + CELL_SIZE;
			int rectHeight = 40 + CELL_SIZE;
			int rectX = getWidth() / 2 - rectWidth / 2;
			int rectY = getHeight() / 2 - rectHeight / 2 - CELL_SIZE;
			g2d.setColor(Color.GREEN);
			g2d.fillRect(rectX, rectY, rectWidth, rectHeight);

			g2d.setColor(Color.BLUE);
			int textX = rectX + (rectWidth - 100) / 2 - 5;
			int textY = rectY + (rectHeight + 10) / 2 + 5;
			g2d.drawString("Gagné !", textX, textY);

			replayButton.setBounds(getWidth() / 2 - 100, getHeight() / 2 + 20, 80, 30);
			quitButton.setBounds(getWidth() / 2 + 20, getHeight() / 2 + 20, 80, 30);
			replayButton.setVisible(true);
			quitButton.setVisible(true);
		}
	}

	private void drawBrickWall(Graphics2D g2d, int x, int y, int size) {
		// Couleur des briques
		g2d.setColor(new Color(139, 69, 19)); // Brun rougeâtre
		g2d.fillRect(x, y, size, size); // Fond de la cellule

		// Taille des briques : plus larges que hautes
		int brickWidth = size / 2; // Largeur d’une brique (2 briques par cellule en largeur)
		int brickHeight = size / 4; // Hauteur d’une brique (4 briques par cellule en hauteur)

		// Couleur des joints
		g2d.setColor(Color.LIGHT_GRAY);

		// Dessiner les joints horizontaux (toutes les brickHeight)
		for (int h = 0; h <= size; h += brickHeight) {
			g2d.drawLine(x, y + h, x + size, y + h);
		}

		// Dessiner les joints verticaux avec décalage d’une demi-largeur par rangée
		for (int row = 0; row < size / brickHeight; row++) {
			int yPos = y + row * brickHeight;
			int offset = (row % 2 == 0) ? 0 : brickWidth / 2; // Décalage alterné
			for (int col = offset; col < size; col += brickWidth) {
				g2d.drawLine(x + col, yPos, x + col, yPos + brickHeight);
			}
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(600, 600); // Taille par défaut, ajustable dynamiquement
	}
}
