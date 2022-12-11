package com.solifungi.handtalkgobang;

import com.solifungi.handtalkgobang.game.ChessBoardPane;
import com.solifungi.handtalkgobang.game.GameConfigs;
import com.solifungi.handtalkgobang.game.GobangGame;
import com.solifungi.handtalkgobang.util.Reference;
import com.solifungi.handtalkgobang.util.handlers.FileHandler;
import com.solifungi.handtalkgobang.util.handlers.SoundHandler;
import com.solifungi.handtalkgobang.util.handlers.StageHandler;

import javafx.application.Application;
import javafx.stage.Stage;
import java.util.ResourceBundle;

public class HandTalkApp extends Application
{
    public static ResourceBundle i18n;
    public static GobangGame currentGame;
    public static ChessBoardPane currentChessboard;

    @Override
    public void start(Stage primaryStage) {
        i18n = ResourceBundle.getBundle(Reference.LANG_RESOURCE, GameConfigs.currentLocale);
        Reference.setTitleMap();

        FileHandler.loadConfig();
        StageHandler handler = new StageHandler();
        handler.setPrimaryStage(primaryStage);
        SoundHandler.setAndPlayBGM();
    }

    public static void main(String[] args) {
        launch();
    }
}