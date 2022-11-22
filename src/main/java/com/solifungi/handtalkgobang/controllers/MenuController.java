package com.solifungi.handtalkgobang.controllers;

import com.solifungi.handtalkgobang.util.IHandleStage;
import com.solifungi.handtalkgobang.util.Reference;
import com.solifungi.handtalkgobang.util.handlers.StageHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.stage.StageStyle;

public class MenuController implements IHandleStage
{
    StageHandler handler;

    @Override
    public void setHandler(StageHandler handler) {
        this.handler = handler;
    }

    @FXML
    protected void quitGame() {
        Platform.exit();
    }

    @FXML
    protected void mainMenuToOptions() {
        handler.switchScene(StageHandler.MAIN, Reference.OPTION_FXML);
    }

    @FXML
    protected void backToMainMenu() {
        handler.switchScene(StageHandler.MAIN, Reference.MAIN_FXML);
    }

    @FXML
    protected void onGameStarted() {
        handler.loadStage(StageHandler.GAME, Reference.GAME_FXML,"Gobang Game",960,720, StageStyle.DECORATED);
        handler.changeStage(StageHandler.MAIN, StageHandler.GAME);
    }

    @FXML
    protected void saveAndCloseInGameOptions(){
        //save code

        handler.unloadStage(StageHandler.OPTION);
    }

    @FXML
    protected void closeInGameOptions(){
        handler.unloadStage(StageHandler.OPTION);
    }

}