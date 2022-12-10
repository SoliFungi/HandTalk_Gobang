package com.solifungi.handtalkgobang.controllers;

import com.solifungi.handtalkgobang.game.GameConfigs;
import com.solifungi.handtalkgobang.util.IHandleStage;
import com.solifungi.handtalkgobang.util.Reference;
import com.solifungi.handtalkgobang.util.handlers.SoundHandler;
import com.solifungi.handtalkgobang.util.handlers.StageHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

import java.util.Locale;

public class ConfigController implements IHandleStage
{
    /* StageHandler */
    StageHandler handler;

    @Override
    public void setHandler(StageHandler handler) {
        this.handler = handler;
    }


    /* FXML Controls */
    @FXML ChoiceBox<String> languages;
    @FXML Slider music, effect;
    @FXML Text musicNum, effectNum;
    @FXML ToggleGroup fullscreen, boardSize, aiLevel, tracer;

    @FXML
    private void initialize(){
        // Init volume values
        music.setValue(GameConfigs.musicVolume);
        musicNum.setText(String.valueOf((int) music.getValue()));
        music.valueProperty().addListener(ob -> {
            musicNum.setText(String.valueOf((int) music.getValue()));
            GameConfigs.musicVolume = (int) music.getValue();
            SoundHandler.player.setVolume(music.getValue() / 100);
        });
        effect.setValue(GameConfigs.effectVolume);
        effectNum.setText(String.valueOf((int) effect.getValue()));
        effect.valueProperty().addListener(ob -> {
            effectNum.setText(String.valueOf((int) effect.getValue()));
            GameConfigs.effectVolume = (int) effect.getValue();
        });

        // Init choice box selection
        if(GameConfigs.currentLocale.toString().equals("zh_CN")){
            languages.getSelectionModel().select(0);
        }
        else if(GameConfigs.currentLocale.toString().equals("en_US")){
            languages.getSelectionModel().select(1);
        }
        else{
            languages.getSelectionModel().select(2);
        }
        languages.getSelectionModel().selectedIndexProperty().addListener(ob -> {
            switch(languages.getSelectionModel().getSelectedIndex()){
                case 0: GameConfigs.currentLocale = new Locale("zh","CN"); break;
                case 1: GameConfigs.currentLocale = new Locale("en","US"); break;
                default: GameConfigs.currentLocale = new Locale("en");
            }
            handler.refreshLocale();
        });

        // Init others
        fullscreen.getToggles().get(GameConfigs.isFullScreen ? 0 : 1).setSelected(true);
        tracer.getToggles().get(GameConfigs.isGameTraced() ? 0 : 1).setSelected(true);
        switch(GameConfigs.getBoardSize()){
            case 19: boardSize.getToggles().get(0).setSelected(true); break;
            case 15: boardSize.getToggles().get(1).setSelected(true); break;
            case 13: boardSize.getToggles().get(2).setSelected(true);
        }
        switch(GameConfigs.getAILevel()){
            case EASY: aiLevel.getToggles().get(0).setSelected(true); break;
            case NORMAL: aiLevel.getToggles().get(1).setSelected(true); break;
            case HARD: aiLevel.getToggles().get(2).setSelected(true); break;
            case LUNATIC: aiLevel.getToggles().get(3).setSelected(true);
        }
    }

    @FXML
    protected void fullscreen(){
        GameConfigs.isFullScreen = true;
        //set fullscreen
    }

    @FXML
    protected void window(){
        GameConfigs.isFullScreen = false;
        //set windowed
    }

    @FXML
    protected void setSize(){
        if(boardSize.getToggles().get(0).isSelected()){
            GameConfigs.setBoardType(19);
        }
        else if(boardSize.getToggles().get(0).isSelected()){
            GameConfigs.setBoardType(15);
        }
        else{
            GameConfigs.setBoardType(13);
        }
    }

    @FXML
    protected void setLevel(){
        if(aiLevel.getToggles().get(0).isSelected()){
            GameConfigs.setAILevel("easy");
        }
        else if(aiLevel.getToggles().get(1).isSelected()){
            GameConfigs.setAILevel("normal");
        }
        else if(aiLevel.getToggles().get(2).isSelected()){
            GameConfigs.setAILevel("hard");
        }
        else{
            GameConfigs.setAILevel("lunatic");
        }
    }

    @FXML
    protected void setTracer(){
        GameConfigs.setTracer(tracer.getToggles().get(0).isSelected());
    }

    @FXML
    protected void backToMenu() {
        handler.switchScene(Reference.OPTION, Reference.MAIN, Reference.MENU_CSS);
        // config file i/o
    }
}
