package com.solifungi.handtalkgobang;

import com.solifungi.handtalkgobang.game.GobangGame;
import com.solifungi.handtalkgobang.util.handlers.StageHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class HandTalkApp extends Application
{
    private StageHandler stageHandler;
    private static GobangGame currentGame;

    @Override
    public void start(Stage primaryStage){
        stageHandler = new StageHandler();
        stageHandler.setPrimaryStage(primaryStage);
    }

    public static void setCurrentGame(GobangGame game){
        currentGame = game;
    }

    public static GobangGame getCurrentGame(){
        return currentGame;
    }

    public static void main(String[] args) {
        launch();
    }
}