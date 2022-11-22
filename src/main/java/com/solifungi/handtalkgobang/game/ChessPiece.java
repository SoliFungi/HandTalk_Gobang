package com.solifungi.handtalkgobang.game;

import com.solifungi.handtalkgobang.util.handlers.EnumHandler.EnumSide;

public class ChessPiece
{
    private final int posX,posY;
    private final EnumSide side;

    public ChessPiece(EnumSide side, int[] pos){
        this.side = side;
        this.posX = pos[0];
        this.posY = pos[1];
    }

    public int[] getPos(){
        return new int[]{posX, posY};
    }

    public int getX(){
        return posX;
    }

    public int getY(){
        return posY;
    }

    public EnumSide getSide(){
        return this.side;
    }

}
