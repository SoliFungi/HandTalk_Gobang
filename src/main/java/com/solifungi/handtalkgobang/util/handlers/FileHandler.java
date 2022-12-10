package com.solifungi.handtalkgobang.util.handlers;

import com.solifungi.handtalkgobang.game.GameConfigs;
import com.solifungi.handtalkgobang.util.Utilities;
import com.solifungi.handtalkgobang.util.handlers.EnumHandler.Side;
import com.solifungi.handtalkgobang.game.ChessPiece;
import com.solifungi.handtalkgobang.game.GobangGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;

public class FileHandler
{
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean saveGame(GobangGame game, File file) {
        try{
            file.delete();
            file.createNewFile(); // Try creating the specified file
            if(file.toString().matches(".*\\.txt")){
                return writeInTXT(game, file);
            }
            else{
                return writeInHTG(game, file);
            }
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static GobangGame readGame(File file) {
        GobangGame game = new GobangGame();
        try{
            Scanner scanner = new Scanner(file);
            String analyzer;

            // Read from .txt
            if(file.toString().matches(".*\\.txt")){
                GameConfigs.setTracer(false);
                game.setGameTitle(scanner.nextLine().split(":")[1].trim());
                scanner.nextLine(); // Omit save time
                scanner.nextLine(); // Unfinished for opponents
                scanner.nextLine(); // Unfinished for rules
                game.setBoardType(Integer.parseInt(scanner.nextLine().split("×")[1].trim()));

                analyzer = scanner.nextLine().split(":")[1].trim();
                if(!analyzer.isEmpty()){ // If lastPiece is empty, currentSide uses default(Black)
                    game.setLastPiece(ChessPiece.fromString(analyzer));
                    if(game.getWinningSide() == -1){
                        game.setCurrentSide(Side.toOpposite(game.getLastPiece().getSide()));
                    }
                }

                analyzer = scanner.nextLine().split(":")[1].trim();
                if(analyzer.startsWith("Black wins")){
                    game.setWinningSide(1);
                    analyzer = analyzer.substring(12).split(" ")[0];
                }
                else if(analyzer.startsWith("White wins")){
                    game.setWinningSide(2);
                    analyzer = analyzer.substring(12).split(" ")[0];
                }
                else if(analyzer.startsWith("Draw")){
                    game.setWinningSide(0);
                    analyzer = analyzer.substring(6).split(" ")[0];
                }
                else{
                    analyzer = analyzer.substring(10).split(" ")[0];
                }
                game.setPieceCount(Integer.parseInt(analyzer));

                // Read digital manual
                scanner.nextLine();
                for(int i = 0; i < game.getBoardType().getSize(); i++){
                    for(int j = 0; j < game.getBoardType().getSize(); j++){
                        game.getGameManual()[j][i] = scanner.nextInt();
                    }
                    scanner.nextLine();
                }
            }

            // Read from .htg
            else{
                GameConfigs.setTracer(true);
                game.setGameTitle(scanner.nextLine());
                scanner.nextLine(); // Unfinished for opponents
                scanner.nextLine(); // Unfinished for rules
                game.setBoardType(Integer.parseInt(scanner.nextLine()));
                game.setWinningSide(Integer.parseInt(scanner.nextLine()));
                while(scanner.hasNextLine()){
                    analyzer = scanner.nextLine();
                    if(analyzer.equals("none") || analyzer.equals("")){
                        break;
                    }
                    game.getPiecesList().add(ChessPiece.fromString(analyzer));
                    game.setPieceCount(game.getPieceCount() + 1);
                }
                if(game.getPieceCount() > 0){
                    game.setLastPiece(game.getPiecesList().get(game.getPieceCount() - 1));
                    game.setCurrentSide(Side.toOpposite(game.getLastPiece().getSide()));
                }
            }
            return game;
        }catch(Exception e){
            throw new RuntimeException("Error loading game from this save file.");
        }
    }

    /**
     * Save game infos into a {@code .txt} file.
     *
     * @param game The game to be saved
     * @param file The file instance to save the game
     * @throws FileNotFoundException If the printWriter cannot be created successfully
     */
    private static boolean writeInTXT(GobangGame game, File file) throws FileNotFoundException {
        Date date = new Date();
        int boardSize = game.getBoardType().getSize();
        int[][] manual = game.getGameManual();
        try(PrintWriter writer = new PrintWriter(file)){
            writer.println("GameTitle: " + game.getGameTitle());
            writer.println("SaveTime: " + date.toGMTString());
            writer.println("Opponents: "); // unfinished
            writer.println("Rules: "); // unfinished
            writer.println("BoardSize: " + boardSize + "×" + boardSize);
            writer.println(lastPieceDesc(game));
            writer.println(resultDesc(game));
            writer.println();
            // Write game manual with 0,1,2
            for(int i = 0; i < boardSize; i++){
                for(int j = 0; j < boardSize; j++){
                    writer.print(manual[j][i]);
                    writer.print(" ");
                }
                writer.println();
            }
        }
        return true;
    }

    private static String lastPieceDesc(GobangGame game){
        StringBuilder desc = new StringBuilder("LastPiece: ");
        if(game.getLastPiece() != null){
            desc.append(game.getLastPiece().toString());
        }
        return desc.toString();
    }

    private static String resultDesc(GobangGame game){
        StringBuilder desc = new StringBuilder("Result: ");
        if (game.getWinningSide() == 1) {
            desc.append("Black wins (");
        } else if (game.getWinningSide() == 2) {
            desc.append("White wins (");
        } else if (game.getWinningSide() == 0){
            desc.append("Draw (");
        } else {
            desc.append("Underway (");
        }
        desc.append(game.getPieceCount());
        desc.append(" moves)");
        return desc.toString();
    }

    /**
     * Save the game in {@code .htg} form, with sequence of pieces but no prompts recorded.
     *
     * @param game The game to be saved
     * @param file The file instance to save the game
     * @throws FileNotFoundException If the printWriter cannot be created successfully
     */
    private static boolean writeInHTG(GobangGame game, File file) throws FileNotFoundException {
        if(!GameConfigs.isGameTraced() || !file.toString().matches(".*\\.htg")) return false; // Double check
        try(PrintWriter writer = new PrintWriter(file)){
            writer.println(game.getGameTitle());
            writer.println(); // unfinished - opponents
            writer.println(); // unfinished - rules
            writer.println(game.getBoardType().getSize());
            writer.println(game.getWinningSide());
            // Write game manual in proper sequence
            if(game.getPieceCount() == 0){
                writer.println("none");
            }
            else{
                for(int i = 0; i < game.getPiecesList().size(); i++)
                    writer.println(game.getPiecesList().get(i));
            }
        }
        return true;
    }

    public static void saveConfigs(){
        File dir = new File("configs");
        File config = new File("configs/game.config");
        try{
            if((!dir.exists() && dir.mkdirs() || dir.exists()) && (!config.exists() && config.createNewFile() || config.exists())){
                try(PrintWriter writer = new PrintWriter(config)){
                    writer.println("/***** The configs should be written carefully in proper form! *****/");
                    writer.println(" * Music Volume: volume of the BGM, value: integer from 0 to 100");
                    writer.println(" * musicVol=" + GameConfigs.musicVolume);
                    writer.println(" *");
                    writer.println(" * Effect Volume: volume of sound effects, value: integer from 0 to 100");
                    writer.println(" * effectVol=" + GameConfigs.effectVolume);
                    writer.println(" *");
                    writer.println(" * Screen Mode: fullscreen or not, value: fullscreen/window");
                    writer.println(" * scMode=" + (GameConfigs.isFullScreen ? "fullscreen" : "window"));
                    writer.println(" *");
                    writer.println(" * Language: current locale, value: lang[_country] (e.g. zh_CN, en_US, en)");
                    writer.println(" * locale=" + GameConfigs.currentLocale.toString());
                    writer.println(" *");
                    writer.println(" * Board Type: current board size, value: 13/15/19");
                    writer.println(" * boardType=" + GameConfigs.getBoardSize());
                    writer.println(" *");
                    writer.println(" * AI Difficulty: difficulty level of game AI, value: easy/normal/hard/lunatic");
                    writer.println(" * aiLvl=" + GameConfigs.getAILevel().getName());
                    writer.println(" *");
                    writer.println(" * Tracer: if the game traces piece order, value: true/false");
                    writer.println(" * tracer=" + GameConfigs.isGameTraced());
                    writer.println("/*******************************************************************/");
                }
            }
        }catch(IOException e){
            throw new RuntimeException("Config file cannot be found or created.");
        }

    }

    public static void loadConfig(){
        File config = new File("configs/game.config");
        try{
            Scanner scanner = new Scanner(config);
            scanner.nextLine();
            scanner.nextLine();
            String analyzer = scanner.nextLine();
            GameConfigs.musicVolume = Integer.parseInt(analyzer.split("=")[1]);
            scanner.nextLine();
            scanner.nextLine();
            analyzer = scanner.nextLine();
            GameConfigs.effectVolume = Integer.parseInt(analyzer.split("=")[1]);
            scanner.nextLine();
            scanner.nextLine();
            analyzer = scanner.nextLine();
            GameConfigs.isFullScreen = analyzer.split("=")[1].equalsIgnoreCase("fullscreen");
            scanner.nextLine();
            scanner.nextLine();
            analyzer = scanner.nextLine();
            GameConfigs.currentLocale = Utilities.localeFromString(analyzer.split("=")[1]);
            scanner.nextLine();
            scanner.nextLine();
            analyzer = scanner.nextLine();
            GameConfigs.setBoardType(Integer.parseInt(analyzer.split("=")[1]));
            scanner.nextLine();
            scanner.nextLine();
            analyzer = scanner.nextLine();
            GameConfigs.setAILevel(analyzer.split("=")[1]);
            scanner.nextLine();
            scanner.nextLine();
            analyzer = scanner.nextLine();
            GameConfigs.setTracer(Boolean.parseBoolean(analyzer.split("=")[1]));
        }catch(FileNotFoundException e){
            saveConfigs();
        }
    }
}
