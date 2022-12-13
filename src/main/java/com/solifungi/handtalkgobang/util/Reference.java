package com.solifungi.handtalkgobang.util;

import java.util.HashMap;

public class Reference {
    public static final String VERSION = "v1.0";

    /*FXML*/
    public static final String MAIN = "fxmls/main.fxml";
    public static final String OPTION = "fxmls/option.fxml";
    public static final String OPTION_IN = "fxmls/option_ingame.fxml";
    public static final String GAME = "fxmls/game.fxml";
    public static final String GAME_INFO = "fxmls/game_info.fxml";

    /*CSS*/
    public static final String MENU_CSS = "css/MenuStyle.css";
    public static final String OPTION_CSS = "css/OptionStyle.css";
    public static final String GAME_CSS = "css/GameStyle.css";

    /*Images*/
    public static final String GAME_ICON = "file:src/main/resources/com/solifungi/handtalkgobang/images/icon.png";
    public static final String BLACK_IMAGE = "file:src/main/resources/com/solifungi/handtalkgobang/images/B_stone.png";
    public static final String WHITE_IMAGE = "file:src/main/resources/com/solifungi/handtalkgobang/images/W_stone.png";
    public static final String BOARD_IMAGE = "file:src/main/resources/com/solifungi/handtalkgobang/images/board.png";

    /*Sounds*/
    public static final String BACKGROUND_MUSIC = "src/main/resources/com/solifungi/handtalkgobang/sounds/music.mp3";
    public static final String PIECE_PLACE_SOUND = "src/main/resources/com/solifungi/handtalkgobang/sounds/piece.wav";

    /*Locale*/
    public static final String LANG_RESOURCE = "com/solifungi/handtalkgobang/languages/lang";
    public static HashMap<String, String> stageTitleKeys = new HashMap<>();
    static {
        stageTitleKeys.put(MAIN, "title.primary_stage");
        stageTitleKeys.put(OPTION, "title.option");
        stageTitleKeys.put(OPTION_IN, "title.option_ingame");
        stageTitleKeys.put(GAME, "title.game");
        stageTitleKeys.put(GAME_INFO, "title.game_info");
    }
}
