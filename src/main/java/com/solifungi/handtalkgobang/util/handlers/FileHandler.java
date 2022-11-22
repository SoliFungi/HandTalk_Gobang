package com.solifungi.handtalkgobang.util.handlers;

import com.solifungi.handtalkgobang.game.GobangGame;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileHandler {
    public void saveGame(EnumHandler.EnumBoardType type, String saveName, EnumHandler.EnumSide currentSide, int[][] manual) throws IOException {
        File save = new File("saves/" + saveName + ".txt");
        if(save.exists()){
            throw new IOException("File already exists");
        }
        try (PrintWriter saver = new PrintWriter(saveName)) {
            saver.println(type.toString());
            saver.println(currentSide.toString());
            for(int i = 0; i < type.getSize(); i++){
                for(int j = 0; j < type.getSize(); j++){
                    saver.print(manual[i][j] + " ");
                }
                saver.println("");
            }
        }
    }

    public void saveGame(GobangGame game) throws IOException {
        saveGame(game.getBoardType(), game.getGameName(), game.getCurrentSide(), game.getGameManual());
    }
}
