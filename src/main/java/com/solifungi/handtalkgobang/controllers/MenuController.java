package com.solifungi.handtalkgobang.controllers;

import com.solifungi.handtalkgobang.HandTalkApp;
import com.solifungi.handtalkgobang.game.GameConfigs;
import com.solifungi.handtalkgobang.util.IHandleStage;
import com.solifungi.handtalkgobang.util.Reference;
import com.solifungi.handtalkgobang.util.handlers.SoundHandler;
import com.solifungi.handtalkgobang.util.handlers.StageHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MenuController implements IHandleStage
{
    StageHandler handler;

    @Override
    public void setHandler(StageHandler handler) {
        this.handler = handler;
    }

    @FXML
    private void initialize(){
        if(GameConfigs.musicMuted){
            mute.setVisible(false);
            play.setVisible(true);
        }
    }

    @FXML Button mute, play;

    /* FXML EventHandler Methods */
    @FXML
    protected void quitGame() {
        Platform.exit();
    }

    @FXML
    protected void mainMenuToOptions() {
        handler.switchScene(Reference.MAIN, Reference.OPTION, Reference.OPTION_CSS);
    }

    @FXML
    protected void onGameStarted() {
        handler.loadStage(Reference.GAME, Reference.GAME_CSS, StageStyle.DECORATED);
        handler.changeStage(Reference.MAIN, Reference.GAME);

        Stage gameStage = handler.getStage(Reference.GAME);
        gameStage.fullScreenProperty().addListener((ob, old, value) ->
                HandTalkApp.currentChessboard.setBoardLength(value ? gameStage.getHeight() * 0.85 : GameController.boardPaneHeight * 0.85));
        gameStage.setFullScreenExitHint("");
        gameStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        if(GameConfigs.isFullScreen){
            gameStage.setFullScreen(true);
        }
    }

    @FXML
    protected void mute(){
        SoundHandler.muteAll();
        mute.setVisible(false);
        play.setVisible(true);
        GameConfigs.musicMuted = true;
    }

    @FXML
    protected void play(){
        SoundHandler.playAll();
        play.setVisible(false);
        mute.setVisible(true);
        GameConfigs.musicMuted = false;
    }
}