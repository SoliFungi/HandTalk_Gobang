package com.solifungi.handtalkgobang.util.handlers;

import com.solifungi.handtalkgobang.util.Reference;

public class EnumHandler {
    public enum EnumSide{
        BLACK(1,"black", Reference.BLACK_IMAGE),
        WHITE(2,"white", Reference.WHITE_IMAGE);

        private final int sign;
        private final String name;
        private final String imageUrl;

        EnumSide(int sign, String name, String imageUrl){
            this.sign = sign;
            this.name = name;
            this.imageUrl = imageUrl;
        }

        public String getName(){
            return this.name;
        }

        public int getSign(){
            return this.sign;
        }

        public String getImageUrl() {
            return this.imageUrl;
        }

        @Override
        public String toString(){
            return "current_side: " + this.name;
        }

        public static EnumSide bySign(int sign){
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

        public static EnumSide byName(String name){
            if(name.equals("black")){
                return BLACK;
            }
            else if(name.equals("white")){
                return WHITE;
            }
            else{
                throw new IllegalArgumentException();
            }
        }
    }


    public enum EnumBoardType{
        LARGE(19),
        DEFAULT(15),
        SMALL(13);

        private final int size;

        EnumBoardType(int size){
            this.size = size;
        }

        public int getSize(){
            return this.size;
        }

        @Override
        public String toString(){
            return "type: " + this.size + " * " + this.size;
        }

        public static EnumBoardType bySize(int size){
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
}
