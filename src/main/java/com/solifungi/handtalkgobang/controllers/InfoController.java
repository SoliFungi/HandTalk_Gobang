package com.solifungi.handtalkgobang.controllers;

import com.solifungi.handtalkgobang.HandTalkApp;
import com.solifungi.handtalkgobang.util.IHandleStage;
import com.solifungi.handtalkgobang.util.Reference;
import com.solifungi.handtalkgobang.util.handlers.StageHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class InfoController implements IHandleStage
{
    StageHandler handler;

    @Override
    public void setHandler(StageHandler handler) {
        this.handler = handler;
    }

    @FXML
    private void initialize(){
        gameName.setText(HandTalkApp.currentGame.getGameTitle());
        opponents.setText(getOpponents());
        gameRule.setText("unfinished"); // gameRule
        boardSize.setText(HandTalkApp.currentGame.getBoardType().toString());
        result.setText(getResult() + " " + getPieceNum());
    }

    private String getOpponents(){
        return HandTalkApp.i18n.getString("side.black") + " " + HandTalkApp.currentGame.blackName + " - " +
                HandTalkApp.i18n.getString("side.white") + " " + HandTalkApp.currentGame.whiteName;
    }

    private String getResult(){
        switch(HandTalkApp.currentGame.getWinningSide()){
            case 0: return HandTalkApp.i18n.getString("result.draw");
            case 1: return HandTalkApp.i18n.getString("result.black");
            case 2: return HandTalkApp.i18n.getString("result.white");
            default: return HandTalkApp.i18n.getString("result.underway");
        }
    }

    private String getPieceNum(){
        return String.format("(%d%s)", HandTalkApp.currentGame.getPieceCount(), HandTalkApp.i18n.getString("word.move"));
    }

    private String getAllInfo(){
        return HandTalkApp.i18n.getString("text.game_title") + ": " +
                HandTalkApp.currentGame.getGameTitle() + "\n" +
                HandTalkApp.i18n.getString("text.opponents") + ": " +
                getOpponents() + "\n" +
                HandTalkApp.i18n.getString("text.game_rule") + ": " +
                "unfinished" + "\n" +
                HandTalkApp.i18n.getString("text.board_size") + ": " +
                HandTalkApp.currentGame.getBoardType().toString() + "\n" +
                HandTalkApp.i18n.getString("text.result") + ": " +
                getResult() + " " + getPieceNum();
    }

    @FXML
    Label gameName, opponents, gameRule, boardSize, result;

    @FXML
    protected void exit(){
        handler.unloadStage(Reference.GAME_INFO);
    }

    @FXML
    protected void copyToClipboard(){
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(getAllInfo());
        clipboard.setContent(content);
    }

}
