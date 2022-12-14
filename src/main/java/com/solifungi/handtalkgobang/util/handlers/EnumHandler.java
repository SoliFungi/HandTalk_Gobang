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
            return this.size + "×" + this.size;
        }

        public static BoardType bySize(int size){
            switch(size){
                case 19: return LARGE;
                case 15: return DEFAULT;
                case 13: return SMALL;
                default: throw new IllegalArgumentException();
            }
        }
    }


    public enum AILevel {
        EASY("easy"),
        NORMAL("normal"),
        HARD("hard"),
        LUNATIC("lunatic");

        private final String name;

        AILevel(String name){
            this.name = name;
        }

        public String getName(){
            return this.name;
        }

        public static AILevel byName(String nameIn){
            switch(nameIn){
                case "easy": return EASY;
                case "normal": return NORMAL;
                case "hard": return HARD;
                case "lunatic": return LUNATIC;
                default: throw new IllegalArgumentException();
            }
        }
    }
}
