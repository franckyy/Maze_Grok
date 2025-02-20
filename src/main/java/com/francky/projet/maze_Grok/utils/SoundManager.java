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
	private float backgroundGain = -20.0f; // Volume musique
    private float effectGain = -25.0f;    // Volume effets	

    // Charger et jouer la musique de fond en boucle avec volume ajusté
    public void playBackgroundMusic(String fileName) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                getClass().getResource("/com/francky/projet/maze_Grok/sounds/" + fileName)
            );
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);
            
            FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(backgroundGain);
            
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

    // Jouer un effet sonore avec volume ajusté
    public void playSoundEffect(String fileName) {
        try {
            java.net.URL soundURL = getClass().getResource("/com/francky/projet/maze_Grok/sounds/" + fileName);
            if (soundURL == null) {
                System.out.println("Erreur : Fichier audio non trouvé - " + fileName);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            // Ajuster le volume des effets sonores
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(effectGain);
            clip.start();
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    // Méthodes pour ajuster dynamiquement si besoin
    public void setBackgroundGain(float gain) {
        this.backgroundGain = gain;
        if (backgroundClip != null && backgroundClip.isRunning()) {
            FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(gain);
        }
    }

    public void setEffectGain(float gain) {
        this.effectGain = gain;
    }
}
