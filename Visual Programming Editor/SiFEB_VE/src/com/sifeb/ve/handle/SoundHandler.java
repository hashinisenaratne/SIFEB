/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.handle;

import com.sifeb.ve.MainApp;
import java.net.URL;
import javafx.scene.media.AudioClip;

/**
 *
 * @author Udith Arosha
 */
public class SoundHandler {

    private static final String SOUND_DIR = "sounds/";
    private static AudioClip backMusic;
    private static boolean musicOn = true;

    public static void playWelcome() {
        AudioClip aud = playAudioClip("welcome.wav", 1);
    }

    public static void playBackMusic() {
        if (musicOn) {
            if (backMusic == null) {
                backMusic = playAudioClip("backgroundMusic.wav", AudioClip.INDEFINITE);
            } else if(!backMusic.isPlaying()){
                backMusic.play();
            }
        }
    }

    public static void stopBackMusic() {
        if (backMusic != null) {
            backMusic.stop();
        }
    }

    public static AudioClip playAudioClip(String name, int numCycles) {
        URL res = MainApp.class.getResource(SOUND_DIR + name);
        AudioClip audioClip = new AudioClip(res.toString());
        audioClip.setCycleCount(numCycles);
        audioClip.play();
        return audioClip;
    }

    public static boolean isMusicOn() {
        return musicOn;
    }

    public static void setMusicOn(boolean musicOn) {
        SoundHandler.musicOn = musicOn;
    }
    
    
}
