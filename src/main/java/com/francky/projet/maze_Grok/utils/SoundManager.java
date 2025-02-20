package com.francky.projet.maze_Grok.utils;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager {
    private Clip backgroundClip; // Pour la musique de fond
    private static final String SOUND_PATH = "src/com/francky/projet/maze_Grok/sounds/";

    // Charger et jouer la musique de fond en boucle
    public void playBackgroundMusic(String fileName) {
        try {
            File file = new File(SOUND_PATH + fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); // Boucle infinie
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Arrêter la musique de fond
    public void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }

    // Jouer un effet sonore (déplacement ou victoire)
    public void playSoundEffect(String fileName) {
        try {
            File file = new File(SOUND_PATH + fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start(); // Joue une seule fois
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
