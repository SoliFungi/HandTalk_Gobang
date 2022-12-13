package com.solifungi.handtalkgobang.util.handlers;

import com.solifungi.handtalkgobang.game.GameConfigs;
import com.solifungi.handtalkgobang.util.Reference;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.MalformedURLException;

public class SoundHandler
{
    // Music
    public static MediaPlayer player;

    public static void setAndPlayBGM(){
        File bgm = new File(Reference.BACKGROUND_MUSIC);
        Media music;
        try{
            music = new Media(bgm.toURI().toURL().toString());
        }catch(MalformedURLException e){
            System.out.println("Error in loading bgm.");
            return;
        }
        player = new MediaPlayer(music);
        player.setVolume(GameConfigs.musicVolume / 100.0);
        player.play();
    }

    public static void muteAll(){
        player.stop();
    }

    public static void playAll(){
        player.play();
    }

    // Sound Effect
    public static AudioClip audio;

    static {
        File sound = new File(Reference.PIECE_PLACE_SOUND);
        try{
            audio = new AudioClip(sound.toURI().toURL().toExternalForm());
        }catch(MalformedURLException e){
            System.out.println("Error in loading sound effect.");
        }
        audio.setVolume(GameConfigs.effectVolume / 100.0);
    }

}
