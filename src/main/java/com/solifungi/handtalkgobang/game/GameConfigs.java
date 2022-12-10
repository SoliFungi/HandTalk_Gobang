package com.solifungi.handtalkgobang.game;

import com.solifungi.handtalkgobang.util.handlers.EnumHandler.*;

import java.util.Locale;

public class GameConfigs
{
    /* General Configs */
    public static boolean musicMuted = false;
    public static int musicVolume = 100;
    public static int effectVolume = 100;
    public static boolean isFullScreen = false;
    public static Locale currentLocale = Locale.getDefault();

    /* Pregame Configs (protected ingame) */
    protected static BoardType currentType = BoardType.DEFAULT;
    protected static AILevel AI = AILevel.NORMAL;
        //游戏规则
    protected static boolean tracer = false; //是否保存落子顺序

    public static AILevel getAILevel(){
        return AI;
    }
    public static void setAILevel(String level){
        AI = AILevel.byName(level);
    }
    public static int getBoardSize(){
        return currentType.getSize();
    }
    public static void setBoardType(int size){
        currentType = BoardType.bySize(size);
    }
    public static boolean isGameTraced(){
        return tracer;
    }
    public static void setTracer(boolean isGameTraced){
        tracer = isGameTraced;
    }
}
