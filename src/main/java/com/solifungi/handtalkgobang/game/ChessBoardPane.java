package com.solifungi.handtalkgobang.game;

import com.solifungi.handtalkgobang.util.Reference;
import com.solifungi.handtalkgobang.util.handlers.EnumHandler.EnumSide;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ChessBoardPane extends StackPane
{
    private final int boardSize;
    private final double boardLength;
    private final double cellLength;
    private final int[][] gameManual;

    public ChessBoardPane(GobangGame game){
        this.boardSize = game.getBoardType().getSize();
        this.boardLength = Math.min(this.getWidth(), this.getHeight()) * 0.9; //
        this.cellLength = boardLength / (boardSize - 1);
        this.gameManual = game.getGameManual();
        this.setPadding(new Insets(20));
        renderAll();
    }

    public void renderAll(){
        ImageView background = new ImageView(new Image(Reference.BOARD_IMAGE, boardLength, boardLength,true,true,true));
        Canvas board = drawBoard();
        GridPane pieces = drawPieces(gameManual);

        this.getChildren().clear();
        this.getChildren().addAll(background, board, pieces);
    }

    private Canvas drawBoard(){
        Canvas canvas = new Canvas(boardLength, boardLength);
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        context.setStroke(Color.BLACK);
        context.setFill(Color.BLACK);

        // Render Gridlines
        for(int i = 0; i < boardSize; i++){
            context.strokeLine(cellLength * i,0,cellLength * i, boardLength); //Rows
            context.strokeLine(0,cellLength * i, boardLength,cellLength * i); //Lines
        }

        // Render Stars
        for(int[] pos : getStarList()){
            context.fillOval(pos[0], pos[1], cellLength / 3, cellLength / 3);
        }

        return canvas;
    }

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

    private GridPane drawPieces(int[][] manual){
        GridPane pieces = new GridPane();
        ImageView blackImage = new ImageView(new Image(EnumSide.BLACK.getImageUrl(),cellLength / 2,cellLength / 2,true,true));
        ImageView whiteImage = new ImageView(new Image(EnumSide.WHITE.getImageUrl(),cellLength / 2,cellLength / 2,true,true));
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                if(manual[i][j] == 1){
                    pieces.add(blackImage, j, i);
                }
                else if(manual[i][j] == 2){
                    pieces.add(whiteImage, j, i);
                }
            }
        }
        return pieces;
    }

    public void rerenderPieces(){
        this.getChildren().remove(2);
        this.getChildren().add(drawPieces(gameManual));
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
}
