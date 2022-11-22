package com.solifungi.handtalkgobang.game;

import com.solifungi.handtalkgobang.HandTalkApp;
import com.solifungi.handtalkgobang.controllers.GameController;
import com.solifungi.handtalkgobang.util.Reference;
import com.solifungi.handtalkgobang.util.handlers.EnumHandler.EnumBoardType;
import com.solifungi.handtalkgobang.util.handlers.EnumHandler.EnumSide;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GobangGame {
    private String gameName = "untitled";
    private EnumBoardType boardType;
    private EnumSide currentSide = EnumSide.BLACK;
    private boolean sideLock = false;
    private int[][] gameManual;
    private ChessPiece lastPiece;

    public GobangGame(EnumBoardType type, String filename){
        this.boardType = type;
        if(filename.equals("new")){
            initGame();
        }
        else{
            readGameFromSave(filename);
        }
    }

    public GobangGame(EnumBoardType type){
        this(type, "new");
    }

    public GobangGame(){
        this(EnumBoardType.DEFAULT);
    }


    /* RunGame Methods */
    public void initGame(){
        this.gameManual = new int[boardType.getSize()][boardType.getSize()];
        for(int i = 0; i < boardType.getSize(); i++){
            for(int j = 0; j < boardType.getSize(); j++)
                this.gameManual[i][j] = 0;
        }
    }

    public void readGameFromSave(String filename){
        try {
            File save = new File(filename);
            this.gameName = filename;
            Scanner saveLoader = new Scanner(save);
            saveLoader.next(); //Discard prompts
            this.boardType = EnumBoardType.bySize(saveLoader.nextInt());
            saveLoader.nextLine(); //Line break
            saveLoader.next(); //Discard prompts
            this.currentSide = EnumSide.byName(saveLoader.nextLine());

            this.gameManual = new int[boardType.getSize()][boardType.getSize()];
            for(int i = 0; i < boardType.getSize(); i++){
                for(int j = 0; j < boardType.getSize(); j++){
                    this.gameManual[i][j] = saveLoader.nextInt();
                }
                saveLoader.nextLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void playRound(int[] pos){
        if(this.gameManual[pos[0]][pos[1]] != 0){
            throw new UnsupportedOperationException();
        }
        else{
            this.gameManual[pos[0]][pos[1]] = this.currentSide.getSign();
            this.lastPiece = new ChessPiece(this.currentSide, pos);
            if(!this.sideLock) {
                changeSide();
            }
        }
    }

    private void changeSide(){
        setCurrentSide(this.currentSide == EnumSide.BLACK ? EnumSide.WHITE : EnumSide.BLACK);
    }

    private boolean isWinning(){
        return judgeRow(lastPiece) || judgeColumn(lastPiece) || judgeMainDiag(lastPiece) || judgeSubDiag(lastPiece);
    }

    private boolean judgeRow(ChessPiece piece){
        int count= -1;
        EnumSide side = piece.getSide();
        int x = piece.getPos()[0];
        int y = piece.getPos()[1];
        while(y >= 0){
            if(this.gameManual[x][y] != side.getSign()) break;
            count += 1;
            y -= 1;
        }
        y = piece.getPos()[1];
        while(y < this.boardType.getSize()){
            if(this.gameManual[x][y] != side.getSign()) break;
            count += 1;
            y += 1;
        }
        return count == 5;
    }

    private boolean judgeColumn(ChessPiece piece){
        int count= -1;
        EnumSide side = piece.getSide();
        int x = piece.getPos()[0];
        int y = piece.getPos()[1];
        while(x >= 0){
            if(this.gameManual[x][y] != side.getSign()) break;
            count += 1;
            x -= 1;
        }
        x = piece.getPos()[0];
        while(x < this.boardType.getSize()){
            if(this.gameManual[x][y] != side.getSign()) break;
            count += 1;
            x += 1;
        }
        return count == 5;
    }

    private boolean judgeMainDiag(ChessPiece piece){
        int count= -1;
        EnumSide side = piece.getSide();
        int x = piece.getPos()[0];
        int y = piece.getPos()[1];
        while(x >= 0 && y >= 0){
            if(this.gameManual[x][y] != side.getSign()) break;
            count += 1;
            x -= 1;
            y -= 1;
        }
        x = piece.getPos()[0];
        y = piece.getPos()[1];
        while(x < this.boardType.getSize() && y < this.boardType.getSize()){
            if(this.gameManual[x][y] != side.getSign()) break;
            count += 1;
            x += 1;
            y += 1;
        }
        return count == 5;
    }

    private boolean judgeSubDiag(ChessPiece piece){
        int count= -1;
        EnumSide side = piece.getSide();
        int x = piece.getPos()[0];
        int y = piece.getPos()[1];
        while(x >= 0 && y < this.boardType.getSize()){
            if(this.gameManual[x][y] != side.getSign()) break;
            count += 1;
            x -= 1;
            y += 1;
        }
        x = piece.getPos()[0];
        y = piece.getPos()[1];
        while(x < this.boardType.getSize() && y >= 0){
            if(this.gameManual[x][y] != side.getSign()) break;
            count += 1;
            x -= 1;
            y += 1;
        }
        return count == 5;
    }


    /* Sets and Gets */
    public void setCurrentSide(EnumSide side){
        this.currentSide = side;
    }

    public EnumSide getCurrentSide(){
        return this.currentSide;
    }

    public void setSideLock(boolean isLock){
        this.sideLock = isLock;
    }

    public String getGameName(){
        return this.gameName;
    }

    public EnumBoardType getBoardType(){
        return this.boardType;
    }

    public int[][] getGameManual(){
        return this.gameManual;
    }

    public ChessPiece getLastPiece(){
        return this.lastPiece;
    }
}
