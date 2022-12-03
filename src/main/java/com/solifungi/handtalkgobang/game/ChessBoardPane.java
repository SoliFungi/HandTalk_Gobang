package com.solifungi.handtalkgobang.game;

import com.solifungi.handtalkgobang.HandTalkApp;
import com.solifungi.handtalkgobang.controllers.GameController;
import com.solifungi.handtalkgobang.util.Reference;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ChessBoardPane extends StackPane
{
    private final int boardSize;
    private final double boardLength;
    private final double cellLength;
    private final int[][] gameManual;

    public ChessBoardPane(GobangGame game){
        this.boardSize = game.getBoardType().getSize();
        this.boardLength = GameController.stageHeight * 0.85;
        this.cellLength = boardLength / (boardSize - 1);
        this.gameManual = game.getGameManual();

        //this.getParent()

        renderAll();
    }

    public void renderAll(){
        this.getChildren().clear();
        renderBackground();
        renderBoard();
        renderPieces();
    }

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

    /**
     * Render all chess pieces. Always goes last in {@link #renderAll}.<br>
     * This can cause lagging, consider using {@link #renderNewPiece} first.
     */
    private void renderPieces(){
        Canvas canvas = new Canvas(boardLength + cellLength, boardLength + cellLength);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image blackImage = new Image(Reference.BLACK_IMAGE, cellLength, cellLength,true,true);
        Image whiteImage = new Image(Reference.WHITE_IMAGE, cellLength, cellLength,true,true);
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
            this.getChildren().set(2, canvas); // Rerender
        }catch(IndexOutOfBoundsException e){
            this.getChildren().add(canvas); // Initial render
        }
        renderTriangle(HandTalkApp.currentGame.getLastPiece());
    }

    /**
     * Render the single new chess piece instead of all.
     *
     * @param newPiece The new piece to render (=lastPiece)
     */
    public void renderNewPiece(ChessPiece newPiece){
        GraphicsContext gc = ((Canvas)this.getChildren().get(2)).getGraphicsContext2D();
        if(newPiece.getSide().getSign() == 1){
            Image blackImage = new Image(Reference.BLACK_IMAGE, cellLength, cellLength,true,true);
            gc.drawImage(blackImage, cellLength * newPiece.getX(), cellLength * newPiece.getY());
        }
        else{
            Image whiteImage = new Image(Reference.WHITE_IMAGE, cellLength, cellLength,true,true);
            gc.drawImage(whiteImage, cellLength * newPiece.getX(), cellLength * newPiece.getY());
        }
        renderTriangle(newPiece);
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
            this.getChildren().set(3, pane); // Rerender
        }catch(IndexOutOfBoundsException e){
            this.getChildren().add(pane); // Initial render
        }
    }

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
}
