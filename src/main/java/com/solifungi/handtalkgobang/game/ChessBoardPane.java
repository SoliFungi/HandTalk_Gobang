package com.solifungi.handtalkgobang.game;

import com.solifungi.handtalkgobang.HandTalkApp;
import com.solifungi.handtalkgobang.controllers.GameController;
import com.solifungi.handtalkgobang.util.Debugging;
import com.solifungi.handtalkgobang.util.Reference;
import javafx.scene.Group;
import com.solifungi.handtalkgobang.util.handlers.EnumHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;

public class ChessBoardPane extends StackPane
{
    static GraphicsContext gc;
    private static Canvas side_canvas;
    private static Image blackImage, whiteImage;

    public ChessBoardPane(GobangGame game){
        this.boardSize = game.getBoardType().getSize();
        this.boardLength = GameController.stageHeight * 0.85;
        this.cellLength = boardLength / (boardSize - 1);
        this.gameManual = game.getGameManual();
        blackImage = new Image(Reference.BLACK_IMAGE, cellLength, cellLength,true,true);
        whiteImage = new Image(Reference.WHITE_IMAGE, cellLength, cellLength,true,true);
        renderAll();
    }

    public void renderAll(){
        this.getChildren().clear();
        renderBackground(); // 0
        renderBoard(); // 1
        renderAxis(); // 2
        renderPieces(); // 3
        // 4 = triangle or pieceNum
    }

    /**
     * Render the single new chess piece instead of all.
     *
     * @param newPiece The new piece to render (=lastPiece)
     */
    public void renderNewPiece(ChessPiece newPiece){
        gc = ((Canvas)this.getChildren().get(3)).getGraphicsContext2D();
        if(newPiece.getSide().getSign() == 1){
            gc.drawImage(blackImage, cellLength * newPiece.getX(), cellLength * newPiece.getY());
        }
        else{
            gc.drawImage(whiteImage, cellLength * newPiece.getX(), cellLength * newPiece.getY());
        }
        renderTriangle(newPiece);
    }

    public void delPieceOnCanvas(int xPos, int yPos){
        gc.clearRect(cellLength * xPos,cellLength * yPos, cellLength, cellLength);
        // Delete triangle
        if(this.getChildren().get(4) instanceof Pane){
            this.getChildren().remove(4);
        }
    }

    public void showPieceNum(boolean show){
        if(show){
            ArrayList<ChessPiece> list = HandTalkApp.currentGame.getPiecesList();
            AnchorPane numPane = new AnchorPane();
            numPane.setPrefSize(boardLength, boardLength);
            if(list != null){
                for(int i = 0; i < list.size(); i++){
                    Text text = new Text(String.valueOf(i + 1));
                    StackPane number = new StackPane(text);
                    number.setPrefSize(cellLength, cellLength);
                    numPane.getChildren().add(number);
                    AnchorPane.setTopAnchor(number, list.get(i).getX() * cellLength);
                    AnchorPane.setLeftAnchor(number, list.get(i).getY() * cellLength);
                }
            }
            try{
                getChildren().set(4, numPane);
            }catch(IndexOutOfBoundsException e){
                getChildren().add(4, numPane);
            }
        }
        else{
            getChildren().remove(4);
            renderTriangle(HandTalkApp.currentGame.getLastPiece());
        }
    }

    private final Group axis = new Group();
    private final int boardSize;
    private final double boardLength;
    private final double cellLength;
    private final int[][] gameManual;

    /**
     * Render the background image. Always goes first in {@link #renderAll}.
     */
    private void renderBackground(){
        ImageView background = new ImageView(new Image(Reference.BOARD_IMAGE, boardLength + cellLength,
                boardLength + cellLength,true,true));

        this.getChildren().add(0, background);
    }

    /**
     * Render board lines and stars. Always goes second in {@link #renderAll}.
     */
    private void renderBoard(){
        double ep = 2; // Extra pixels for borderlines to fully render.
        Canvas canvas = new Canvas(boardLength + ep * 2, boardLength + ep * 2);
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        context.setStroke(Color.BLACK);
        context.setLineWidth(1.5);
        context.setFill(Color.BLACK);
        // Render Gridlines
        for(int i = 0; i < boardSize; i++){
            context.strokeLine(cellLength * i + ep, ep,cellLength * i + ep, boardLength + ep); // Rows
            context.strokeLine(ep,cellLength * i + ep, boardLength + ep,cellLength * i + ep); // Lines
        }
        // Render Stars
        for(int[] pos : getStarList()){
            context.fillOval(pos[0] * cellLength - cellLength / 8 + ep, pos[1] * cellLength - cellLength / 8 + ep,cellLength / 4, cellLength / 4);
        }

        this.getChildren().add(1, canvas);
    }

    /**
     * Get "star" positions for different board size.
     *
     * @return ArrayList contains all "star" coordinates
     */
    private ArrayList<int[]> getStarList(){
        ArrayList<int[]> starList = new ArrayList<>();
        // Corner Stars
        starList.add(new int[]{3, 3});
        starList.add(new int[]{3, boardSize - 4});
        starList.add(new int[]{boardSize - 4, 3});
        starList.add(new int[]{boardSize - 4, boardSize - 4});
        // Tengen
        if(boardSize > 14){
            starList.add(new int[]{boardSize / 2, boardSize / 2});
        }
        // Side Stars
        if(boardSize > 18){
            starList.add(new int[]{boardSize / 2, 3});
            starList.add(new int[]{boardSize / 2, boardSize - 4});
            starList.add(new int[]{3, boardSize / 2});
            starList.add(new int[]{boardSize - 4, boardSize / 2});
        }
        return starList;
    }

    private void renderAxis(){
        if(!axis.getChildren().isEmpty()){
            axis.getChildren().clear();
        }

        VBox center = new VBox();
        center.setPrefSize(boardLength + cellLength * 0.1,boardLength + cellLength * 0.1);
        VBox numColLeft = new VBox(cellLength * 0.5), numColRight = new VBox(cellLength * 0.5);
        HBox letterRowUp = new HBox(cellLength * 0.5), letterRowDown = new HBox(cellLength * 0.5);
        for(int i = 0; i < boardSize; i ++){
            numColLeft.getChildren().add(new Label(String.valueOf(boardLength - i)));
            numColRight.getChildren().add(new Label(String.valueOf(boardLength - i)));
            letterRowUp.getChildren().add(new Label(String.valueOf('A' + i)));
            letterRowDown.getChildren().add(new Label(String.valueOf('A' + i)));
        }
        BorderPane axisPane = new BorderPane(center, letterRowUp, numColRight, letterRowDown, numColLeft);
        axisPane.setPrefSize(boardLength + cellLength,boardLength + cellLength);

        axis.getChildren().add(axisPane);
//        axis.setVisible(false); // Default
        getChildren().add(2, axis);
    }

    /**
     * Render all chess pieces. Always goes last in {@link #renderAll}.<br>
     * This can cause lagging, consider using {@link #renderNewPiece} first.
     */
    private void renderPieces(){
        if (side_canvas==null){
            side_canvas = new Canvas(boardLength + cellLength, boardLength + cellLength);
        }
        GraphicsContext gc = side_canvas.getGraphicsContext2D();
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                if(gameManual[i][j] == 1){
                    gc.drawImage(blackImage, cellLength * i, cellLength * j);
                }
                else if(gameManual[i][j] == 2){
                    gc.drawImage(whiteImage, cellLength * i, cellLength * j);
                }
            }
        }
        try{
            this.getChildren().set(3, side_canvas); // Rerender
        }catch(IndexOutOfBoundsException e){
            this.getChildren().add(side_canvas); // Initial render
        }
        renderTriangle(HandTalkApp.currentGame.getLastPiece());
    }

    /**
     * Add a red triangle sign on the last piece placed on the board.
     *
     * @param lastOne The last piece on which the sign should be. (If null, no triangle will be rendered)
     */
    private void renderTriangle(@Nullable ChessPiece lastOne){
        if(lastOne == null) return;
        Polygon triangle = new Polygon(cellLength * (lastOne.getX() + 0.75), cellLength * (lastOne.getY() + 0.6443),
                                               cellLength * (lastOne.getX() + 0.25), cellLength * (lastOne.getY() + 0.6443),
                                               cellLength * (lastOne.getX() + 0.5), cellLength * (lastOne.getY() + 0.2113));
        triangle.setStroke(Color.RED);
        triangle.setFill(Color.RED);
        Pane pane = new Pane(triangle);
        try{
            this.getChildren().set(4, pane); // Rerender
        }catch(IndexOutOfBoundsException e){
            this.getChildren().add(pane); // Initial render
        }
    }

    /* Getter & Setter */
    @Override
    public void setWidth(double value) {
        super.setWidth(value);
        renderAll();
    }

    @Override
    public void setHeight(double value) {
        super.setHeight(value);
        renderAll();
    }

    public double getCellLength(){
        return this.cellLength;
    }

    public Group getAxis(){
        return this.axis;
    }

    @Debugging
    private void printChildrenClass(){
        this.getChildren().forEach(node -> System.out.println(node.getClass()));
    }
}
