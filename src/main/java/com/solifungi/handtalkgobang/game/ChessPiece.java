package com.solifungi.handtalkgobang.game;

import com.solifungi.handtalkgobang.util.handlers.EnumHandler.Side;
import org.jetbrains.annotations.Nullable;

public class ChessPiece
{
    private final int posX,posY;
    private final Side side;

    public ChessPiece(Side side, int x, int y){
        this.side = side;
        this.posX = x;
        this.posY = y;
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

    public Side getSide(){
        return this.side;
    }

    @Override
    public boolean equals(Object anObject){
        if(this == anObject){
            return true;
        }
        if(anObject instanceof ChessPiece){
            ChessPiece anPiece = (ChessPiece) anObject;
            return this.side.equals(anPiece.side) && this.getX() == anPiece.getX() && this.getY() == anPiece.getY();
        }
        return false;
    }

    @Override
    public String toString(){
        return side.toString() + " (" + getX() + "," + getY() + ")";
    }

    @Nullable
    public static ChessPiece fromString(String s){
        String[] parts = s.split("[(,)]");
        try{
            return new ChessPiece(Side.byName(parts[0].trim()), Integer.parseInt(parts[1].trim()), Integer.parseInt(parts[1].trim()));
        }catch(IllegalArgumentException e){
            return null;
        }
    }
}
