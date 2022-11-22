package com.solifungi.handtalkgobang.controllers;

import com.solifungi.handtalkgobang.HandTalkApp;
import com.solifungi.handtalkgobang.game.ChessBoardPane;
import com.solifungi.handtalkgobang.game.GobangGame;
import com.solifungi.handtalkgobang.util.IHandleStage;
import com.solifungi.handtalkgobang.util.Reference;
import com.solifungi.handtalkgobang.util.handlers.StageHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;

public class GameController implements IHandleStage
{
    StageHandler handler;

    @Override
    public void setHandler(StageHandler handler) {
        this.handler = handler;
    }

    @FXML
    public Pane boardPane = new Pane();

    @FXML
    private void initialize(){
        HandTalkApp.setCurrentGame(new GobangGame());
        newChessBoard(new ChessBoardPane(HandTalkApp.getCurrentGame()));
    }

    public void newChessBoard(ChessBoardPane chessBoard){
        boardPane.getChildren().clear();
        boardPane.getChildren().add(chessBoard);
    }

    @FXML
    protected void onGameSaved(){
        //save code
        handler.unloadStage(StageHandler.GAME);
        handler.getStage(StageHandler.MAIN).show();
    }

    @FXML
    protected void openInGameOptions() {
        //stop game(time count)
        handler.loadStage(StageHandler.OPTION, Reference.OPTION_IN_FXML, "Options", 480, 360, StageStyle.UTILITY);
    }

}
