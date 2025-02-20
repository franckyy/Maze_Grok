package com.francky.projet.maze_Grok.utils;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager {
	private Clip backgroundClip;

    // Charger et jouer la musique de fond en boucle avec volume ajusté
    public void playBackgroundMusic(String fileName) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                getClass().getResource("/com/francky/projet/maze_Grok/sounds/" + fileName)
            );
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);
            
            // Ajuster le volume (gain en dB)
            FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-20.0f); // Réduit le volume de 20 dB (ajustable)
            
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
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

    // Jouer un effet sonore (sans modification du volume ici)
    public void playSoundEffect(String fileName) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                getClass().getResource("/com/francky/projet/maze_Grok/sounds/" + fileName)
            );
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
