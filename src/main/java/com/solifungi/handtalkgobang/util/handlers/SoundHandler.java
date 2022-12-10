package com.solifungi.handtalkgobang.util.handlers;

import com.solifungi.handtalkgobang.game.GameConfigs;
import com.solifungi.handtalkgobang.util.Reference;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.MalformedURLException;

public class SoundHandler
{
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
        resetMusicVolume();
        player.play();
    }

    public static void resetMusicVolume(){
        player.setVolume(GameConfigs.musicVolume / 100.0);
    }

    public static void muteAll(){
        player.stop();
    }

    public static void playAll(){
        player.play();
    }
}
