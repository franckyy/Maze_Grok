package com.francky.projet.maze_Grok.utils;

import javax.sound.sampled.*;
import java.io.IOException;
 
public class SoundManager {
    private Clip backgroundClip;
    private Clip currentEffectClip;
    private float backgroundGain = -20.0f;
    private float effectGain = -30.0f;

    public void playBackgroundMusic(String fileName) {
        stopBackgroundMusic();
        java.net.URL url = getClass().getResource("/sounds/" + fileName);
        if (url == null) {
            System.out.println("Erreur : Fichier audio non trouvé - " + fileName);
            return;
        }
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);
            FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(backgroundGain);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }

    public void playSoundEffect(String fileName) {
        if (currentEffectClip != null && currentEffectClip.isRunning()) {
            return;
        }
        java.net.URL soundURL = getClass().getResource("/sounds/" + fileName);
        if (soundURL == null) {
            System.out.println("Erreur : Fichier audio non trouvé - " + fileName);
            return;
        }
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            currentEffectClip = AudioSystem.getClip();
            currentEffectClip.open(audioStream);
            FloatControl gainControl = (FloatControl) currentEffectClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(effectGain);
            currentEffectClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    currentEffectClip.close();
                    currentEffectClip = null;
                }
            });
            currentEffectClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

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