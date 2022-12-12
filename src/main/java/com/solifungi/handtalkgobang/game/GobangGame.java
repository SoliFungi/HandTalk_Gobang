package com.solifungi.handtalkgobang.game;

import com.solifungi.handtalkgobang.util.handlers.EnumHandler.BoardType;
import com.solifungi.handtalkgobang.util.handlers.EnumHandler.Side;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GobangGame
{
    private String gameTitle = "untitled";
    public String blackName = "unknown", whiteName = "unknown";
    private BoardType boardType = GameConfigs.currentType;
    private Side currentSide = Side.BLACK;
    private int winningSide = -1; // -1:underway 0:draw 1:black 2:white
    private ArrayList<ChessPiece> piecesList = new ArrayList<>();
    private int pieceCount = 0;
    private int[][] gameManual = new int[boardType.getSize()][boardType.getSize()];
    private ChessPiece lastPiece = null;
    private File saveFile = null;
    public boolean sideLock = false;


    /* RunGame Methods */
    public boolean playRound(int xPos, int yPos){
        if(gameManual[xPos][yPos] == 0){
            gameManual[xPos][yPos] = currentSide.getSign();
            lastPiece = new ChessPiece(currentSide, xPos, yPos);
            pieceCount += 1;
            if(!sideLock) {changeSide();}
            piecesList.add(lastPiece);
            if(isWinning()){
                setWinningSide(lastPiece.getSide().getSign());
            }else if(pieceCount == boardType.getSize() * boardType.getSize()){
                setWinningSide(0);
            }
            return true;
        }
        return false;
    }

    private void changeSide(){
        setCurrentSide(Side.toOpposite(currentSide));
    }

    public boolean isWinning(){
        return judgeRow(lastPiece) || judgeColumn(lastPiece) || judgeMainDiag(lastPiece) || judgeSubDiag(lastPiece);
    }

    private boolean ex_Winning(ChessPiece piece){
        final Side side = piece.getSide();
        //白子不受影响
        if (side ==null || side.getSign() == 2 ){
            return false;
        }

        //棋盘大小
        final int size = this.getBoardType().getSize()-1;
        final int SIDE_POS_X = piece.getPos()[0];
        final int SIDE_POS_Y = piece.getPos()[1];
        int isFree = 0;
        ArrayList<int[]> pos_array = new ArrayList<>();

        //三三禁手
        //判断是否存在三子/四子连接情况
        //水平
        for(int x = SIDE_POS_X;x>=0 && x>=SIDE_POS_X-4;){
            x = x -1;
            if(gameManual[x][SIDE_POS_Y] == side.getSign()){
                pos_array.add(new int[]{x, SIDE_POS_Y});
            }else {
                break;
            }
        }
        for(int x = SIDE_POS_X;x<=size && x>=SIDE_POS_X+4;){
            x = x -1;
            if(gameManual[x][SIDE_POS_Y] == side.getSign()){
                pos_array.add(new int[]{x, SIDE_POS_Y});
            }else {
                break;
            }
        }
        //遍历子关联
        for (int[] side_array: pos_array) {
            int count = -1;
            int x = side_array[0];
            int y = side_array[1];

        }

        return true;
    }

//    private boolean isFreeSide(int FreeType, ChessPiece piece, int[] side_pos){
//
//        //水平→判定
//        for (int pos_x = SIDE_POS_X; pos_x>=0;){
//            pos_x+= FreeType-1;
//            if(gameManual[pos_x][SIDE_POS_Y] == 1 || gameManual[pos_x][SIDE_POS_Y] == 2) {
//                return false;
//            };
//            return true;
//        }


    private boolean judgeRow(ChessPiece piece){
        int count= -1;
        Side side = piece.getSide();
        int x = piece.getPos()[0];
        int y = piece.getPos()[1];
        while(y >= 0){
            if(gameManual[x][y] != side.getSign()) break;
            count += 1;
            y -= 1;
        }
        y = piece.getPos()[1];
        while(y < boardType.getSize()){
            if(gameManual[x][y] != side.getSign()) break;
            count += 1;
            y += 1;
        }
        return count == 5;
    }

    private boolean judgeColumn(ChessPiece piece){
        int count= -1;
        Side side = piece.getSide();
        int x = piece.getPos()[0];
        int y = piece.getPos()[1];
        while(x >= 0){
            if(gameManual[x][y] != side.getSign()) break;
            count += 1;
            x -= 1;
        }
        x = piece.getPos()[0];
        while(x < boardType.getSize()){
            if(gameManual[x][y] != side.getSign()) break;
            count += 1;
            x += 1;
        }
        return count == 5;
    }

    private boolean judgeMainDiag(ChessPiece piece){
        int count= -1;
        Side side = piece.getSide();
        int x = piece.getPos()[0];
        int y = piece.getPos()[1];
        while(x >= 0 && y >= 0){
            if(gameManual[x][y] != side.getSign()) break;
            count += 1;
            x -= 1;
            y -= 1;
        }
        x = piece.getPos()[0];
        y = piece.getPos()[1];
        while(x < boardType.getSize() && y < boardType.getSize()){
            if(gameManual[x][y] != side.getSign()) break;
            count += 1;
            x += 1;
            y += 1;
        }
        return count == 5;
    }

    private boolean judgeSubDiag(ChessPiece piece){
        int count= -1;
        Side side = piece.getSide();
        int x = piece.getPos()[0];
        int y = piece.getPos()[1];
        while(x >= 0 && y < boardType.getSize()){
            if(gameManual[x][y] != side.getSign()) break;
            count += 1;
            x -= 1;
            y += 1;
        }
        x = piece.getPos()[0];
        y = piece.getPos()[1];
        while(x < boardType.getSize() && y >= 0){
            if(gameManual[x][y] != side.getSign()) break;
            count += 1;
            x += 1;
            y -= 1;
        }
        return count == 5;
    }

    public void rewriteManualFromList(){
        gameManual = new int[boardType.getSize()][boardType.getSize()];
        piecesList.forEach(piece -> gameManual[piece.getX()][piece.getY()] = piece.getSide().getSign());
    }


    /* Sets and Gets */
    public void setCurrentSide(Side side){
        this.currentSide = side;
    }

    public Side getCurrentSide(){
        return this.currentSide;
    }

    public String getGameTitle(){
        return this.gameTitle;
    }

    public void setGameTitle(String title){
        this.gameTitle = title;
    }

    public BoardType getBoardType(){
        return this.boardType;
    }

    public void setBoardType(int size){
        this.boardType = BoardType.bySize(size);
        this.gameManual = new int[boardType.getSize()][boardType.getSize()];
    }

    public int[][] getGameManual(){
        return this.gameManual;
    }

    public ChessPiece getLastPiece(){
        return this.lastPiece;
    }

    public void setLastPiece(ChessPiece piece){
        this.lastPiece = piece;
    }

    public int getWinningSide() {
        return winningSide;
    }

    public void setWinningSide(int side) {
        this.winningSide = side;
    }

    public int getPieceCount() {
        return pieceCount;
    }

    public void setPieceCount(int count) {
        this.pieceCount = count;
    }

    public ArrayList<ChessPiece> getPiecesList(){
        return piecesList;
    }

    public File getSaveFile(){
        return saveFile;
    }

    public void setSaveFile(File file) {
        this.saveFile = file;
    }
}
