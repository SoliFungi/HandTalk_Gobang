package com.solifungi.handtalkgobang.game;

import com.solifungi.handtalkgobang.util.handlers.EnumHandler.BoardType;
import com.solifungi.handtalkgobang.util.handlers.EnumHandler.Side;

import java.util.ArrayList;

public class GobangGame
{
    private String gameTitle = "untitled";
    private BoardType boardType = GameConfigs.currentType;
    private Side currentSide = Side.BLACK;
    private int winningSide = -1; // -1:underway 0:draw 1:black 2:white
    private ArrayList<ChessPiece> piecesList;
    private int pieceCount = 0;
    private int[][] gameManual = new int[boardType.getSize()][boardType.getSize()];
    private ChessPiece lastPiece = null;
    public boolean sideLock = false;

    public GobangGame(){
        if(GameConfigs.tracer){
            piecesList = new ArrayList<>();
        }
    }

    /* RunGame Methods */
    public boolean playRound(int[] pos){
        if(gameManual[pos[0]][pos[1]] == 0){
            gameManual[pos[0]][pos[1]] = currentSide.getSign();
            lastPiece = new ChessPiece(currentSide, pos);
            pieceCount += 1;
            if(GameConfigs.tracer) piecesList.add(lastPiece);
            if(!sideLock) changeSide();
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
}
