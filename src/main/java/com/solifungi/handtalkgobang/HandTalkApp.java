package com.solifungi.handtalkgobang;

import com.solifungi.handtalkgobang.game.ChessBoardPane;
import com.solifungi.handtalkgobang.game.GobangGame;
import com.solifungi.handtalkgobang.util.handlers.FileHandler;
import com.solifungi.handtalkgobang.util.handlers.SoundHandler;
import com.solifungi.handtalkgobang.util.handlers.StageHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class HandTalkApp extends Application
{
    public static GobangGame currentGame;
    public static ChessBoardPane currentChessboard;

    @Override
    public void start(Stage primaryStage) {
        FileHandler.loadConfig();

        StageHandler handler = new StageHandler();
        handler.setPrimaryStage(primaryStage);

        SoundHandler.setAndPlayBGM();
    }

    public static void main(String[] args) {
        launch();
    }
}