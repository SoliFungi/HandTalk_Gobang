package com.solifungi.handtalkgobang.util.handlers;

import com.solifungi.handtalkgobang.HandTalkApp;
import com.solifungi.handtalkgobang.game.GameConfigs;
import com.solifungi.handtalkgobang.util.Debugging;
import com.solifungi.handtalkgobang.util.IHandleStage;
import com.solifungi.handtalkgobang.util.Reference;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

public class StageHandler
{
    private static final HashMap<String, Stage> stages = new HashMap<>();

    private void addStage(String resourceName, Stage stage){
        stages.put(resourceName, stage);
    }

    private void changeName(String previousName, String newName, Stage stage){
        stages.remove(previousName);
        stages.put(newName, stage);
    }

    public Stage getStage(String resourceName){
        return stages.get(resourceName);
    }

    /**
     * Initialize the primary stage from FXML, and add it to stagMap.
     *
     * @param primaryStage The primary stage instantiated initially
     */
    public void setPrimaryStage(Stage primaryStage){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(
                    HandTalkApp.class.getResource(Reference.MAIN),
                    ResourceBundle.getBundle(Reference.LANG_RESOURCE, GameConfigs.currentLocale)
            );
            Scene scene = new Scene(fxmlLoader.load(), 480, 360);
            URL css = HandTalkApp.class.getResource(Reference.MENU_CSS);
            if(css != null){
                scene.getStylesheets().add(css.toExternalForm());
            }
            else{
                System.out.println("CSS file load failure: " + Reference.MENU_CSS);
            }
            primaryStage.setScene(scene);
            primaryStage.setTitle("HandTalk Gobang");
            primaryStage.getIcons().add(new Image(Reference.GAME_ICON));
            primaryStage.setResizable(false);
            primaryStage.show();

            addStage(Reference.MAIN, primaryStage);

            IHandleStage controller = fxmlLoader.getController();
            controller.setHandler(this);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Load a new stage from FXML, and add it to stageMap.
     *
     * @param resourceName Corresponding FXML resource location
     * @param title Displayed stage title
     * @param cssModifier CSS file which modifies the scene style
     * @param style The stage style
     */
    public void loadStage(String resourceName, String title, @Nullable String cssModifier, StageStyle style){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(
                    HandTalkApp.class.getResource(resourceName),
                    ResourceBundle.getBundle(Reference.LANG_RESOURCE, GameConfigs.currentLocale)
            );
            Scene scene = new Scene(fxmlLoader.load());
            if(cssModifier != null){
                URL css = HandTalkApp.class.getResource(cssModifier);
                if(css != null){
                    scene.getStylesheets().add(css.toExternalForm());
                }
                else{
                    System.out.println("CSS file load failure: " + cssModifier);
                }
            }
            Stage stage = new Stage(style);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.getIcons().add(new Image(Reference.GAME_ICON));
            stage.setResizable(false);
            addStage(resourceName, stage);
            stage.show();

            IHandleStage controller = fxmlLoader.getController();
            controller.setHandler(this);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Set a new scene from FXML to a specified stage.
     */
    public void switchScene(String previousName, String resourceName, @Nullable String cssModifier){
        try{
            Stage stage = getStage(previousName);
            FXMLLoader fxmlLoader = new FXMLLoader(
                    HandTalkApp.class.getResource(resourceName),
                    ResourceBundle.getBundle(Reference.LANG_RESOURCE, GameConfigs.currentLocale)
            );
            Scene scene = new Scene(fxmlLoader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
            stage.setScene(scene);
            if(cssModifier != null){
                URL css = HandTalkApp.class.getResource(cssModifier);
                if(css != null){
                    scene.getStylesheets().add(css.toExternalForm());
                }
                else{
                    System.out.println("CSS file load failure: " + cssModifier);
                }
            }
            changeName(previousName, resourceName, stage);

            IHandleStage controller = fxmlLoader.getController();
            controller.setHandler(this);
        }catch(Exception e){
            throw new RuntimeException(e);
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
    public void unloadStage(String resourceName){
        try{
            getStage(resourceName).close();
            stages.remove(resourceName);
        }catch(Exception e){
            throw new NoSuchElementException("窗口不存在！");
        }
    }

    /**
     * Refresh the locale display of all stages.
     */
    public void refreshLocale(){
        stages.forEach((key,value) -> {
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(
                        HandTalkApp.class.getResource(key),
                        ResourceBundle.getBundle(Reference.LANG_RESOURCE, GameConfigs.currentLocale)
                );
                value.getScene().setRoot(fxmlLoader.load());
                IHandleStage controller = fxmlLoader.getController();
                controller.setHandler(this);
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * List current stage names.
     *
     * @return String of all stage register names
     */
    @Debugging
    public String stageList(){
        StringBuilder sb = new StringBuilder();
        for(String name : stages.keySet()){
            sb.append(name);
            sb.append(" ");
        }
        return sb.toString();
    }

}
