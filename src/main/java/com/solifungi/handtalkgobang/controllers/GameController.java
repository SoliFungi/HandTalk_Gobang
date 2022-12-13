package com.solifungi.handtalkgobang.controllers;

import com.solifungi.handtalkgobang.HandTalkApp;
import com.solifungi.handtalkgobang.game.ChessBoardPane;
import com.solifungi.handtalkgobang.game.ChessPiece;
import com.solifungi.handtalkgobang.game.GameConfigs;
import com.solifungi.handtalkgobang.game.GobangGame;
import com.solifungi.handtalkgobang.util.IHandleStage;
import com.solifungi.handtalkgobang.util.Reference;
import com.solifungi.handtalkgobang.util.Utilities;
import com.solifungi.handtalkgobang.util.handlers.EnumHandler.Side;
import com.solifungi.handtalkgobang.util.handlers.FileHandler;
import com.solifungi.handtalkgobang.util.handlers.SoundHandler;
import com.solifungi.handtalkgobang.util.handlers.StageHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class GameController implements IHandleStage
{
    public static double boardPaneHeight = 720; // Init not used, just in case
    public static boolean vsAI = false;

    @FXML public StackPane boardPane = new StackPane();
    @FXML public BorderPane gamePane; // The pane fills the whole game scene. (Assigned before method <initialize>)
    @FXML MenuBar menuBar;
    @FXML MenuItem switcher;
    @FXML CheckMenuItem pieceEraser, branchEraser, screenMode, showAxis, showPieceNum;

    /* StageHandler */
    StageHandler handler;

    @Override
    public void setHandler(StageHandler handler) {
        this.handler = handler;
    }

    /* Init Methods */
    @FXML
    private void initialize(){
        menuBar.setPrefWidth(gamePane.getWidth());
        gamePane.widthProperty().addListener(ob -> menuBar.setPrefWidth(gamePane.getWidth()));

        if(!vsAI) switcher.setDisable(true);
        if(GameConfigs.isGameTraced()){
            pieceEraser.setDisable(true);
            branchEraser.setDisable(false);
        }else{
            branchEraser.setDisable(true);
            pieceEraser.setDisable(false);
        }

        HandTalkApp.currentGame = new GobangGame();
        Button button = new Button(HandTalkApp.i18n.getString("button.start_game"));
        button.setFont(new Font(22));
        button.setOnAction(e -> initGame());
        boardPane.getChildren().add(button);
    }

    public void initGame(){
        GobangGame game = HandTalkApp.currentGame;
        boardPaneHeight = boardPane.getHeight();
        ChessBoardPane chessBoard = new ChessBoardPane(game);
        HandTalkApp.currentChessboard = chessBoard;
        boardPane.getChildren().clear();
        boardPane.getChildren().add(chessBoard);
        chessBoard.setOnMouseClicked(new ChessPlaceHandler());
    }

    /* Game Save Methods */
    public void endGame(@NotNull GobangGame game){
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
        btSave.setOnAction(event -> openGameSaver(game));
        endDialog.showAndWait();
    }

    private void openGameSaver(GobangGame game){
        FileChooser fileSaver = new FileChooser();
        fileSaver.setTitle("Save Manual");
        fileSaver.setInitialDirectory(new File(System.getProperty("user.home")));
        fileSaver.setInitialFileName("untitled_game-" + Utilities.getFormattedTime());
        fileSaver.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT Doc","*.txt"));
        if(GameConfigs.isGameTraced()){
            fileSaver.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTG Game Record","*.htg"));
        }

        File file = fileSaver.showSaveDialog(handler.getStage(Reference.GAME));
        if(file != null && FileHandler.saveGame(game, file)){
            HandTalkApp.currentGame.setSaveFile(file);
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setHeaderText("Saved successfully!");
            info.showAndWait();
        }
    }

    private void openGameLoader(){
        FileChooser fileLoader = new FileChooser();
        fileLoader.setTitle("Open File");
        fileLoader.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileLoader.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Save File","*.*"),
                new FileChooser.ExtensionFilter("TXT Doc","*.txt"),
                new FileChooser.ExtensionFilter("HTG Game Record","*.htg")
        );

        File file = fileLoader.showOpenDialog(handler.getStage(Reference.GAME));
        HandTalkApp.currentGame = FileHandler.readGame(file);
        initGame();
    }

    /* FXML EventHandler Methods */
    @FXML
    protected void restartGame(){
        if(HandTalkApp.currentGame.getSaveFile() == null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Game not saved yet. Want to save this game?");
            Button ok = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            ok.setOnAction(event -> openGameSaver(HandTalkApp.currentGame));
            alert.showAndWait();
        }
        HandTalkApp.currentGame = new GobangGame();
        initGame();
    }

    @FXML
    protected void loadGame(){
        openGameLoader();
    }

    @FXML
    protected void saveGame(){
        File file = HandTalkApp.currentGame.getSaveFile();
        if(file != null && FileHandler.isSaveFileValid(file)){
            FileHandler.saveGame(HandTalkApp.currentGame, file);
        }
        else{
            saveGameAs();
        }
    }

    @FXML
    protected void saveGameAs(){
        openGameSaver(HandTalkApp.currentGame);
    }

    @FXML
    protected void showGameInfo(){
        handler.loadStage(Reference.GAME_INFO, null, StageStyle.UTILITY);
    }

    @FXML
    protected void switchSide(){
        GobangGame game = HandTalkApp.currentGame;
        String bn = game.blackName;
        game.blackName = game.whiteName;
        game.whiteName = bn;
        game.setCurrentSide(Side.toOpposite(game.getCurrentSide()));
        // ai compute
    }

    @FXML
    protected void forfeit(){
        GobangGame game = HandTalkApp.currentGame;
        game.setWinningSide(Side.toOpposite(game.getCurrentSide()).getSign());
        endGame(game);
    }

    @FXML
    protected void setPVP(){
        vsAI = false;
        // stop AI engine
    }

    @FXML
    protected void setPVE(){
        vsAI = true;
        // load ai engine
    }

    @FXML
    protected void setInTurn(){
        GobangGame game = HandTalkApp.currentGame;
        game.sideLock = false;
        game.setCurrentSide(Side.toOpposite(game.getCurrentSide()));
    }

    @FXML
    protected void setBlackOnly(){
        HandTalkApp.currentGame.setCurrentSide(Side.BLACK);
        HandTalkApp.currentGame.sideLock = true;
    }

    @FXML
    protected void setWhiteOnly(){
        HandTalkApp.currentGame.setCurrentSide(Side.WHITE);
        HandTalkApp.currentGame.sideLock = true;
    }

    @FXML
    protected void pieceEraseMode(){
        HandTalkApp.currentChessboard.setOnMouseClicked(pieceEraser.isSelected() ? new ChessDeleteHandler() : new ChessPlaceHandler());
    }

    @FXML
    protected void branchEraseMode(){
        HandTalkApp.currentChessboard.setOnMouseClicked(branchEraser.isSelected() ? new BranchDeleteHandler() : new ChessPlaceHandler());
    }

    @FXML
    protected void setScreenMode(){
        GameConfigs.isFullScreen = screenMode.isSelected();
        handler.getStage(Reference.GAME).setFullScreen(screenMode.isSelected());
    }

    @FXML
    protected void setAxisVisibility(){
        HandTalkApp.currentChessboard.getAxis().setVisible(showAxis.isSelected());
    }

    @FXML
    protected void setPieceNumShown(){
        HandTalkApp.currentChessboard.setShowPieceNum(showPieceNum.isSelected());
    }

    @FXML
    protected void saveAndQuit(){
        saveGame();
        handler.unloadStage(Reference.GAME);
        handler.getStage(Reference.MAIN).show();
    }

    @FXML
    protected void openInGameOptions() {
        handler.loadStage(Reference.OPTION_IN, Reference.OPTION_CSS, StageStyle.UTILITY);
    }

    /* Chessboard Click Event Handlers */
    class ChessPlaceHandler implements EventHandler<MouseEvent>{
        GobangGame game = HandTalkApp.currentGame;
        ChessBoardPane chessBoard = HandTalkApp.currentChessboard;

        @Override
        public void handle(MouseEvent event) {
            double cl = chessBoard.getCellLength();
            int xPos = (int) (event.getX() / cl);
            int yPos = (int) (event.getY() / cl);
            if(game.playRound(xPos, yPos)){

//                //Some multithreading stuff
//
//                chessBoard.setOnMouseClicked(null);
//                final Runnable acceptInput = () -> {
//                    try{
//                        Thread.sleep(0);
//                    }catch(InterruptedException e){
//                        e.printStackTrace();
//                    }
//                    chessBoard.setOnMouseClicked(this);
//                };
//                new Thread(acceptInput).start();

                SoundHandler.audio.play();
                chessBoard.renderNewPiece(game.getLastPiece());
                if(game.getWinningSide() != -1){
                    endGame(game);
                    chessBoard.setOnMouseClicked(null);
                }
            }
        }
    }

    static class ChessDeleteHandler implements EventHandler<MouseEvent>{
        GobangGame game = HandTalkApp.currentGame;
        ChessBoardPane chessBoard = HandTalkApp.currentChessboard;

        @Override
        public void handle(MouseEvent event) {
            double cl = chessBoard.getCellLength();
            int xPos = (int) (event.getX() / cl);
            int yPos = (int) (event.getY() / cl);
            try{
                ChessPiece toRemove = new ChessPiece(Side.bySign(game.getGameManual()[xPos][yPos]), xPos, yPos);
                game.getPiecesList().remove(toRemove);
                game.setPieceCount(game.getPiecesList().size());
                game.rewriteManualFromList();
                chessBoard.delPieceOnCanvas(xPos, yPos);
            }
            catch(IllegalArgumentException e){
                System.out.println("No piece at(" + xPos + "," + yPos + ")");
            }
        }
    }

    static class BranchDeleteHandler implements EventHandler<MouseEvent>{
        GobangGame game = HandTalkApp.currentGame;
        ChessBoardPane chessBoard = HandTalkApp.currentChessboard;

        @Override
        public void handle(MouseEvent event) {
            double cl = chessBoard.getCellLength();
            int xPos = (int) (event.getX() / cl);
            int yPos = (int) (event.getY() / cl);
            try{
                System.out.println(game.getGameManual()[xPos][yPos]);
                ChessPiece startPiece = new ChessPiece(Side.bySign(game.getGameManual()[xPos][yPos]), xPos, yPos);

                ArrayList<ChessPiece> list = game.getPiecesList();
//                ArrayList<ChessPiece> delList = new ArrayList<>();
                int i = list.indexOf(startPiece);
                for(int j = i; j < list.size(); j++){
                    chessBoard.delPieceOnCanvas(list.get(j).getX(), list.get(j).getY());
                    list.remove(j);
//                    delList.add(list.get(j));
                }
//                list.removeAll(delList);
                game.setPieceCount(game.getPiecesList().size());
                game.rewriteManualFromList();
            }
            catch(IllegalArgumentException e){
                System.out.println("No piece at(" + xPos + "," + yPos + ")");
            }
        }
    }

}