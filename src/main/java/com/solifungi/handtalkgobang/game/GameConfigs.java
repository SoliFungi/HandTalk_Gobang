package com.solifungi.handtalkgobang.game;

import com.solifungi.handtalkgobang.util.handlers.EnumHandler.*;

import java.util.Locale;

public class GameConfigs
{
    /* General Configs */
    public static int musicSound = 100;
    public static int effectSound = 100;
    public static boolean isFullScreen = false;
    public static Locale currentLocale = Locale.getDefault();

    /* Pregame Configs (protected ingame) */
    protected static BoardType currentType = BoardType.DEFAULT;
    protected static AILevel AI = AILevel.NORMAL;
        //游戏规则
    protected static boolean tracer = false; //是否保存落子顺序

    public static boolean isGameTraced(){
        return tracer;
    }
}
