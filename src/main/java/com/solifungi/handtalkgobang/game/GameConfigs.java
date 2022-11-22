package com.solifungi.handtalkgobang.game;

import com.solifungi.handtalkgobang.util.handlers.EnumHandler.EnumBoardType;

public class GameConfigs
{
    /* General Configs */
    private int musicSound = 100;
    private int effectSound = 100;
    private boolean isFullScreen = false;

    /* Pregame Configs */
    private EnumBoardType boardType = EnumBoardType.DEFAULT;
    //AI难度
    //游戏规则
    private boolean tracer = false; //是否保存落子顺序

    /* Getters and Setters */
    public int getMusicSound() {
        return musicSound;
    }

    public void setMusicSound(int musicSound) {
        this.musicSound = musicSound;
    }

    public int getEffectSound() {
        return effectSound;
    }

    public void setEffectSound(int effectSound) {
        this.effectSound = effectSound;
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

    public EnumBoardType getBoardType() {
        return boardType;
    }

    public void setBoardType(EnumBoardType boardType) {
        this.boardType = boardType;
    }

    public boolean isTracer() {
        return tracer;
    }

    public void setTracer(boolean tracer) {
        this.tracer = tracer;
    }
}
