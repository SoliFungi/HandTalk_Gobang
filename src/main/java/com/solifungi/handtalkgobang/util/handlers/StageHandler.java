package com.solifungi.handtalkgobang.util.handlers;

import com.solifungi.handtalkgobang.HandTalkApp;
import com.solifungi.handtalkgobang.util.IHandleStage;
import com.solifungi.handtalkgobang.util.Reference;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class StageHandler
{
    public static final String MAIN = "main_menu";
    public static final String GAME = "game_stage";
    public static final String OPTION = "in_game_option";
    public static final String SAVE = "save_stage";

    private static final HashMap<String, Stage> stages = new HashMap<>();

    public void addStage(String name, Stage stage){
        stages.put(name, stage);
    }

    public Stage getStage(String name){
        return stages.get(name);
    }

    /**
     *
     * @param primaryStage The primary stage
     */
    public void setPrimaryStage(Stage primaryStage){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HandTalkApp.class.getResource(Reference.MAIN_FXML));
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

    public void loadStage(String stageName, String resourceName, String title, double width, double height, StageStyle style){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HandTalkApp.class.getResource(resourceName));
            Scene scene = new Scene(fxmlLoader.load(), width, height);
            Stage stage = new Stage(style);
            stage.setScene(scene);
            stage.setTitle(title);

            this.addStage(stageName, stage);
            stage.show();

            IHandleStage controller = fxmlLoader.getController();
            controller.setHandler(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void switchScene(String stageName, String resourceName){
        try{
            Stage stage = getStage(stageName);
            FXMLLoader fxmlLoader = new FXMLLoader(HandTalkApp.class.getResource(resourceName));
            Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
            getStage(stageName).setScene(scene);

            IHandleStage controller = fxmlLoader.getController();
            controller.setHandler(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void changeStage(String hideStage, String showStage){
        getStage(hideStage).close();
        getStage(showStage).show();
    }

    public void unloadStage(String name){
        try{
            getStage(name).close();
            stages.remove(name);
        }catch(Exception e){
            throw new NoSuchElementException("窗口不存在！");
        }
    }

}
