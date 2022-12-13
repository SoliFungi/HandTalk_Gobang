package com.solifungi.handtalkgobang.game;

import com.solifungi.handtalkgobang.HandTalkApp;
import com.solifungi.handtalkgobang.controllers.GameController;
import com.solifungi.handtalkgobang.util.Debugging;
import com.solifungi.handtalkgobang.util.Reference;
import com.solifungi.handtalkgobang.util.handlers.EnumHandler.Side;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;

public class ChessBoardPane extends StackPane
{
    static GraphicsContext gc;
    private static Canvas side_canvas, board_canvas;
    private static Image blackImage, whiteImage;

    public ChessBoardPane(GobangGame game){
        this.boardLength = GameController.boardPaneHeight * 0.85;
        this.boardSize = game.getBoardType().getSize();
        this.cellLength = boardLength / (boardSize - 1);
        setMaxSize(boardLength + cellLength,boardLength + cellLength);
        this.gameManual = game.getGameManual();
        blackImage = new Image(Reference.BLACK_IMAGE, cellLength, cellLength,true,true);
        whiteImage = new Image(Reference.WHITE_IMAGE, cellLength, cellLength,true,true);
        renderAll();
    }

    public void renderAll(){
        getChildren().clear();
        if(side_canvas != null){
            gc.clearRect(0,0, side_canvas.getWidth(), side_canvas.getHeight());
        }
        if(board_canvas != null){
            board_canvas.getGraphicsContext2D().clearRect(0, 0, board_canvas.getWidth(), board_canvas.getHeight());
        }

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

        if(showPieceNum){
            renderPieceNum();
        }
        else{
            renderTriangle(newPiece);
        }
    }

    public void delPieceOnCanvas(int xPos, int yPos){
        //获取当前棋子表
        ArrayList<ChessPiece> chess_list = new ArrayList<>();
        boolean lock = true;
        //删除终末焦点下标
        int last_piece = -1;
        for(ChessPiece chessPiece : HandTalkApp.currentGame.getPiecesList()){
            if(chessPiece.getX()==xPos && chessPiece.getY()==yPos){
                lock=false;
            }

            if(lock){
                chess_list.add(chessPiece);
                last_piece += 1;
                continue;
            }
            //删除点后的棋子
            gc.clearRect(cellLength * chessPiece.getX(),cellLength * chessPiece.getY(), cellLength, cellLength);
        }
        //更改全局渲染表
        HandTalkApp.currentGame.setPiecesList(chess_list);
        //更改全局判定表
        int[][] local_manual = new int[boardSize][boardSize];
        for (ChessPiece chessPiece: HandTalkApp.currentGame.getPiecesList()){
            local_manual[chessPiece.getX()][chessPiece.getY()] = chessPiece.getSide().getSign();
        }
        HandTalkApp.currentGame.setGameManual(local_manual);

        //获取落子焦点
        ChessPiece chessPiece2 = HandTalkApp.currentGame.getPiecesList().get(last_piece);
        //渲染指示器终点
        renderTriangle(chessPiece2);




        // Delete triangle or unwanted numbers
//        try{
//            if(getChildren().get(4) instanceof AnchorPane){
//                renderPieceNum();
//            }
//            else{
//                this.getChildren().remove(4);
//            }
//        }catch(IndexOutOfBoundsException ignore){}
    }

    private final Group axis = new Group();
    private final int boardSize;
    private double boardLength, cellLength;
    private final int[][] gameManual;
    private boolean showPieceNum = false;

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
        board_canvas = new Canvas(boardLength + ep * 2, boardLength + ep * 2);
        GraphicsContext context = board_canvas.getGraphicsContext2D();
        context.clearRect(0,0, board_canvas.getWidth(), board_canvas.getHeight());
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

        this.getChildren().add(1, board_canvas);
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

    // 绘制1-A型坐标图层
    private void renderAxis(){
        if(!axis.getChildren().isEmpty()){
            axis.getChildren().clear();
        }

        VBox center = new VBox();
        VBox numColLeft = new VBox(cellLength * 0.635), numColRight = new VBox(cellLength * 0.635);
        HBox letterRowUp = new HBox(cellLength * 0.8), letterRowDown = new HBox(cellLength * 0.8);
        for(int i = 0; i < boardSize; i ++){
            numColLeft.getChildren().add(new Label(String.valueOf(boardSize - i)));
            numColRight.getChildren().add(new Label(String.valueOf(boardSize - i)));
            letterRowUp.getChildren().add(new Label(String.valueOf((char)('A' + i))));
            letterRowDown.getChildren().add(new Label(String.valueOf((char)('A' + i))));
        }
        BorderPane axisPane = new BorderPane(center, letterRowUp, numColRight, letterRowDown, numColLeft);
        numColLeft.setMinWidth(cellLength * 0.5);
        numColRight.setMinWidth(cellLength * 0.5);
        letterRowUp.setMinHeight(cellLength * 0.5);
        letterRowDown.setMinHeight(cellLength * 0.5);
        axisPane.setPrefSize(boardLength + cellLength,boardLength + cellLength);

        numColLeft.setAlignment(Pos.CENTER);
        numColRight.setAlignment(Pos.CENTER);
        letterRowUp.setAlignment(Pos.CENTER);
        letterRowDown.setAlignment(Pos.CENTER);

        axis.getChildren().add(axisPane);
        axis.setVisible(false); // Default
        getChildren().add(2, axis);
    }

    /**
     * Render all chess pieces. Always goes last in {@link #renderAll}.<br>
     * This can cause lagging, consider using {@link #renderNewPiece} first.
     */
    private void renderPieces(){
        if (side_canvas == null){
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
        pane.setMaxSize(boardLength + cellLength, boardLength + cellLength);
        try{
            this.getChildren().set(4, pane); // Rerender
        }catch(IndexOutOfBoundsException e){
            this.getChildren().add(pane); // Initial render
        }
    }

    private void renderPieceNum(){
        ArrayList<ChessPiece> list = HandTalkApp.currentGame.getPiecesList();
        AnchorPane numPane = new AnchorPane();
        numPane.setPrefSize(boardLength, boardLength);
        if(list != null){
            for(int i = 0; i < list.size(); i++){
                Label lbl = new Label(String.valueOf(i + 1));
                lbl.setTextFill(i == list.size() - 1 ? Color.RED : Side.toOpposite(list.get(i).getSide()).getColor());
                lbl.setStyle("-fx-font-size: 20; -fx-font-weight: bold");
                StackPane number = new StackPane(lbl);
                number.setPrefSize(cellLength, cellLength);
                numPane.getChildren().add(number);
                AnchorPane.setTopAnchor(number, list.get(i).getY() * cellLength);
                AnchorPane.setLeftAnchor(number, list.get(i).getX() * cellLength);
            }
        }
        try{
            getChildren().set(4, numPane);
        }catch(IndexOutOfBoundsException e){
            getChildren().add(4, numPane);
        }
    }

    /* Getter & Setter */
    public double getCellLength(){
        return this.cellLength;
    }

    public void setBoardLength(double value){
        this.boardLength = value;
        this.cellLength = boardLength / (boardSize - 1);
        renderAll();
    }

    public Group getAxis(){
        return this.axis;
    }

    public void setShowPieceNum(boolean showPieceNum) {
        this.showPieceNum = showPieceNum;
        if(showPieceNum){
            renderPieceNum();
        }
        else{
            renderTriangle(HandTalkApp.currentGame.getLastPiece());
        }
    }

    @Debugging
    private void printChildrenClass(){
        this.getChildren().forEach(node -> System.out.println(node.getClass()));
    }
}
