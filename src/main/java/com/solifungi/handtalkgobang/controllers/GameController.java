package com.solifungi.handtalkgobang.controllers;

import com.solifungi.handtalkgobang.HandTalkApp;
import com.solifungi.handtalkgobang.game.ChessBoardPane;
import com.solifungi.handtalkgobang.game.GameConfigs;
import com.solifungi.handtalkgobang.game.GobangGame;
import com.solifungi.handtalkgobang.util.IHandleStage;
import com.solifungi.handtalkgobang.util.Reference;
import com.solifungi.handtalkgobang.util.Utilities;
import com.solifungi.handtalkgobang.util.handlers.FileHandler;
import com.solifungi.handtalkgobang.util.handlers.StageHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;

import java.io.File;

public class GameController implements IHandleStage
{
    public static double stageWidth, stageHeight;

    StageHandler handler;

    @Override
    public void setHandler(StageHandler handler) {
        this.handler = handler;
    }

    @FXML
    public Pane boardPane = new Pane();

    @FXML
    public BorderPane gamePane; // The pane fills the whole game scene. (Assigned before method <initialize>)

    @FXML
    private void initialize(){
        startNewGame();
        ChessBoardPane chessBoard = HandTalkApp.currentChessboard;
        chessBoard.setOnMouseClicked(event -> {
            double cl = chessBoard.getCellLength();
            int xPos = (int) (event.getX() / cl);
            int yPos = (int) (event.getY() / cl);
            GobangGame game = HandTalkApp.currentGame;
            if(game.playRound(new int[]{xPos, yPos})){
                chessBoard.renderNewPiece(game.getLastPiece());
                if(game.getWinningSide() != -1){
                    endGame(game);
                    chessBoard.setOnMouseClicked(null);
                }
            }
        });
    }

    public void startNewGame(){
        HandTalkApp.currentGame = new GobangGame();
        stageWidth = gamePane.getMinWidth();
        stageHeight = gamePane.getMinHeight();
        HandTalkApp.currentChessboard = new ChessBoardPane(HandTalkApp.currentGame);

        boardPane.getChildren().clear();
        boardPane.getChildren().add(HandTalkApp.currentChessboard);
    }

    public void endGame(GobangGame game){
        Dialog<String> endDialog = new Dialog<>();
        DialogPane dialogPane = endDialog.getDialogPane();
        dialogPane.setBackground(new Background(new BackgroundFill(Color.SANDYBROWN,null,null)));

        // Set text on the dialog
        Text winText = new Text(game.getWinningSide() == 0 ? "draw!" : (game.getLastPiece().getSide().getName() + " wins!"));
        winText.setFont(new Font("Lucida Handwriting Italic",30));
        winText.setStroke(game.getWinningSide() == 2 ? Color.WHITE : Color.BLACK); // 0->black 1->black 2->white
        winText.setFill(game.getWinningSide() == 1 ? Color.BLACK : Color.WHITE); // 0->white 1->black 2->white
        VBox textPane = new VBox(winText);
        textPane.setAlignment(Pos.CENTER);
        dialogPane.setContent(textPane);

        // Set buttons on the dialog
        ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType quit = new ButtonType("Quit", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(save, quit);
        Button btSave = (Button) dialogPane.lookupButton(save);

        // Add save-game fileChooser
        btSave.setOnAction(event -> {
            FileChooser fileSaver = new FileChooser();
            configFileSaver(fileSaver);
            File file = fileSaver.showSaveDialog(handler.getStage(Reference.GAME));
            if(file != null && FileHandler.saveGame(game, file)){
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setHeaderText("Saved successfully!");
                info.showAndWait();
            }
        });

        endDialog.showAndWait();
    }

    private static void configFileSaver(final FileChooser fileSaver){
        fileSaver.setTitle("Save Manual");
        fileSaver.setInitialDirectory(new File(System.getProperty("user.home")));
        fileSaver.setInitialFileName("untitled_game-" + Utilities.getFormattedTime());
        fileSaver.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT Doc","*.txt"));
        if(GameConfigs.isGameTraced()){
            fileSaver.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTG Game Record","*.htg"));
        }
    }

    @FXML
    protected void onGameSaved(){
        //save code
        handler.unloadStage(Reference.GAME);
        handler.getStage(Reference.MAIN).show();
    }

    @FXML
    protected void openInGameOptions() {
        //stop game(time count)
        handler.loadStage(Reference.OPTION_IN, "Options", Reference.OPTION_CSS, StageStyle.UTILITY);
    }

}