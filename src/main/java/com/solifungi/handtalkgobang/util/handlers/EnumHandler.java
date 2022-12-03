package com.solifungi.handtalkgobang.util.handlers;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class EnumHandler {
    public enum Side {
        BLACK(1,"black", Color.BLACK),
        WHITE(2,"white", Color.WHITE);

        private final int sign;
        private final String name;
        private final Paint color;

        Side(int sign, String name, Paint color){
            this.sign = sign;
            this.name = name;
            this.color = color;
        }

        public String getName(){
            return this.name;
        }

        public int getSign(){
            return this.sign;
        }

        public Paint getColor(){
            return this.color;
        }

        @Override
        public String toString(){
            return this.name;
        }

        public static Side toOpposite(Side side){
            return side.equals(Side.BLACK) ? Side.WHITE : Side.BLACK;
        }

        public static Side bySign(int sign){
            if(sign == 1){
                return BLACK;
            }
            else if(sign == 2){
                return WHITE;
            }
            else{
                throw new IllegalArgumentException();
            }
        }

        public static Side byName(String name){
            if(name.equalsIgnoreCase("black")){
                return BLACK;
            }
            else if(name.equalsIgnoreCase("white")){
                return WHITE;
            }
            else{
                throw new IllegalArgumentException();
            }
        }
    }


    public enum BoardType {
        LARGE(19),
        DEFAULT(15),
        SMALL(13);

        private final int size;

        BoardType(int size){
            this.size = size;
        }

        public int getSize(){
            return this.size;
        }

        @Override
        public String toString(){
            return "type: " + this.size + " * " + this.size;
        }

        public static BoardType bySize(int size){
            if(size == 19){
                return LARGE;
            }
            else if(size == 15){
                return DEFAULT;
            }
            else if(size == 13){
                return SMALL;
            }
            else{
                throw new IllegalArgumentException();
            }
        }
    }


    public enum AILevel {
        EASY, NORMAL, HARD, LUNATIC
    }
}
