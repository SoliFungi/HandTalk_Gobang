package com.solifungi.handtalkgobang.util.handlers;

import com.solifungi.handtalkgobang.HandTalkApp;
import com.solifungi.handtalkgobang.game.GameConfigs;
import com.solifungi.handtalkgobang.util.Debugging;
import com.solifungi.handtalkgobang.util.IHandleStage;
import com.solifungi.handtalkgobang.util.Reference;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

public class StageHandler
{
    public static final String MAIN = "main_menu";
    public static final String GAME = "game";
    public static final String OPTION = "in_game_option";

    private static final HashMap<String, Stage> stages = new HashMap<>();

    public void addStage(String name, Stage stage){
        stages.put(name, stage);
    }

    public Stage getStage(String name){
        return stages.get(name);
    }

    /**
     * Initialize the primary stage from FXML, and add it to stagMap.
     *
     * @param primaryStage The primary stage instantiated initially
     */
    public void setPrimaryStage(Stage primaryStage){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(
                    HandTalkApp.class.getResource(Reference.MAIN_FXML),
                    ResourceBundle.getBundle(Reference.LANG_RESOURCE, GameConfigs.currentLocale)
            );
            Scene scene = new Scene(fxmlLoader.load(), 480, 360);
            primaryStage.setScene(scene);
            primaryStage.setTitle("HandTalk Gobang");
            primaryStage.setResizable(false);

            this.addStage(MAIN, primaryStage);
            primaryStage.show();

            IHandleStage controller = fxmlLoader.getController();
            controller.setHandler(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Load a new stage from FXML, and add it to stageMap.
     *
     * @param stageName Register name of the stage in stageMap
     * @param resourceName Corresponding FXML resource location
     * @param title Displayed stage title
     * @param style The stage style
     */
    public void loadStage(String stageName, String resourceName, String title, StageStyle style){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(
                    HandTalkApp.class.getResource(resourceName),
                    ResourceBundle.getBundle(Reference.LANG_RESOURCE, GameConfigs.currentLocale)
            );
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage(style);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setResizable(false);

            this.addStage(stageName, stage);
            stage.show();

            IHandleStage controller = fxmlLoader.getController();
            controller.setHandler(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Set a new scene from FXML to a specified stage.
     */
    public void switchScene(String stageName, String resourceName){
        try{
            Stage stage = getStage(stageName);
            FXMLLoader fxmlLoader = new FXMLLoader(
                    HandTalkApp.class.getResource(resourceName),
                    ResourceBundle.getBundle(Reference.LANG_RESOURCE, GameConfigs.currentLocale)
            );
            Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
            getStage(stageName).setScene(scene);

            IHandleStage controller = fxmlLoader.getController();
            controller.setHandler(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Show a specified stage and hide another.
     */
    public void changeStage(String hideStage, String showStage){
        getStage(hideStage).close();
        getStage(showStage).show();
    }

    /**
     * Close a specified stage and remove it in stageMap.
     */
    public void unloadStage(String name){
        try{
            getStage(name).close();
            stages.remove(name);
        }catch(Exception e){
            throw new NoSuchElementException("窗口不存在！");
        }
    }

    /**
     * List current stage names.
     *
     * @return String of all stage register names
     */
    @Debugging
    public String listStages(){
        StringBuilder sb = new StringBuilder();
        for(String name : stages.keySet()){
            sb.append(name);
            sb.append(" ");
        }
        return sb.toString();
    }

}
