package com.example.connectFive;
import com.example.connectFive.contracts.GameContract;
import com.example.connectFive.contracts.GameStatus;
import com.example.connectFive.contracts.Player;
import com.example.connectFive.contracts.Turn;

import java.util.*;

public class Game {

    public static final int Rows = 6;
    public static final int Cols = 9;
    public static final int WinCount = 5;
    public static HashMap<UUID, GameContract> gameMap = new HashMap<UUID, GameContract>();

    public HashMap<UUID, GameContract> getGameMap() {
        return gameMap;
    }

    public void setGameMap(HashMap<UUID, GameContract> gameMap) {
        this.gameMap = gameMap;
    }

    public GameContract AddPlayerToGame(Player newPlayer) {
        GameContract game = new GameContract();
        String[] colours = new String[2];
        for (Map.Entry<UUID, GameContract> entry : gameMap.entrySet()){
            UUID key = entry.getKey();
            GameContract CreatedGame = entry.getValue();
            if (CreatedGame.getPlayer2() == null) {
                CreatedGame.setPlayer2(newPlayer);
                CreatedGame.setGameStatus(GameStatus.TWO_PLAYER_GAME);
                CreatedGame.player2.setPlayerId(newPlayer.getPlayerId());
                CreatedGame.player2.setColour(newPlayer.getColour());
                CreatedGame.setTurn(CreatedGame.getPlayer1().getColour());
                colours[0] = CreatedGame.getPlayer1().getColour();
                colours[1] =newPlayer.getColour();
                CreatedGame.setColours(colours);
                return CreatedGame;
            }
        }
        CreateNewGame(newPlayer.getPlayerId(), newPlayer.getColour(), game);
        return game;
    }

    public GameContract CreateNewGame(String playerId, String colour, GameContract game) {
        String[][] board = new String[Rows][Cols];
        Player player = new Player();
        player.setPlayerId(playerId);
        player.setColour(colour);
        game.setGameId(UUID.randomUUID());
        game.setPlayer1(player);
        game.setGameStatus(GameStatus.CREATED_WITH_ONE_PLAYER);
        game.setBoard(board);

        this.gameMap.put(game.gameId, game);
        return game;
    }

    public GameContract CheckTurn(UUID gameId) {
        GameContract game = new GameContract();
        for (Map.Entry<UUID, GameContract> entry : gameMap.entrySet()){
            UUID key = entry.getKey();
            GameContract currentGame = entry.getValue();
            if (key.equals(gameId)) {
                return currentGame;
            }
        }
        return game;
    }

    public GameContract PlayTurn(UUID gameId, Turn turn) {
        GameContract game = new GameContract();
        for (Map.Entry<UUID, GameContract> entry : gameMap.entrySet()){
            UUID key = entry.getKey();
            GameContract currentGame = entry.getValue();
            if (key.equals(gameId)) {
                game = currentGame;
            }
        }

        int coordinates[] = AddTurn(turn.getTurn()-1, game.getBoard(), turn.getColour());
        if(coordinates[0] != 10){
            for (Map.Entry<UUID, GameContract> entry : gameMap.entrySet()){
                UUID key = entry.getKey();
                GameContract currentGame = entry.getValue();
                if (key.equals(gameId)) {
                    currentGame.setBoard(game.getBoard());
                }
            }
        }
        else { //column the player tried to enter into is already full
            game.setGameStatus(GameStatus.FULL_COL);
        }

        Boolean win = CheckWin(game.getBoard(), coordinates, turn.getColour());
        if (win.equals(true)) {
            game.setGameStatus(GameStatus.COMPLETED);
            game.setWinner(game.getTurn());
        }
        else
            ChangeTurn(game);
        return game;
    }

    public void ChangeTurn(GameContract game){
        String[] gameColours = game.getColours();
        if (gameColours[0].equals(game.getTurn()))
            game.setTurn(gameColours[1]);
        else if (gameColours[1].equals(game.getTurn()))
            game.setTurn(gameColours[0]);
    }

    public int[] AddTurn(int turn, String[][] board, String colour){
        int[] coordinates = new int[2];
        //Check to see if the column is already full
        if (board[0][turn] != null) {
            coordinates[0] = 10;
            return coordinates;
        }
        for (int i = Rows-1; i >= 0; i--)
        {
            if (board[i][turn] == null) {
                board[i][turn] = "[" + colour + "]";
                coordinates[0] = i; //row
                coordinates[1] = turn; //col
                return coordinates;
            }
        }
        return coordinates;
    }

    public Boolean CheckWin(String[][] board, int[] coordinates, String colour){
        String checkValue = "[" + colour + "]";
        int row = coordinates[0];
        int col = coordinates[1];
        if (CheckForVerticalWin(board, row, col, checkValue))
            return true;
        if (CheckForHorizontalWin(board, row, col, checkValue))
             return true;
        if (CheckForDiagonalLeftWin(board, row, col, checkValue))
            return true;
        if (CheckForDiagonalRightWin(board, row, col, checkValue))
            return true;
        else
            return false;
    }

    public Boolean CheckForVerticalWin(String[][] board, int row, int col, String checkValue) {
        int counterV = 1;
        for (int i = 1; i <= WinCount; i++)
        {
            try {
                if(board[row + i] [col].equals(checkValue))
                    counterV++;
                else
                    break;
            }
            catch (IndexOutOfBoundsException ex) {
                break;
            }
        }
        if (counterV == WinCount)
            return true;

        return false;
    }

    public Boolean CheckForHorizontalWin (String[][] board, int row, int col, String checkValue) {
        int counterH = 1;
        for (int i = 1; i <= WinCount; i++)
        {
            try {
                if (board[row][col + i] != null && board[row][col + i].equals(checkValue))
                    counterH++;
                else
                    break;
            }
            catch (IndexOutOfBoundsException ex) {
                break;
            }
        }
        if (counterH == WinCount)
            return true;

        for (int i = 1; i <= WinCount; i++)
        {
            try {
                if (board[row][col - i] != null && board[row][col - i].equals(checkValue)) {
                    counterH++;
                    if (counterH == WinCount)
                        return true;
                }
            }
            catch (IndexOutOfBoundsException ex) {
                break;
            }
        }
        return false;
    }

    public Boolean CheckForDiagonalLeftWin (String[][] board, int row, int col, String checkValue) {
        int counterDl = 1;
        for (int i = 1; i <= WinCount; i++)
        {
            try {
                if (board[row - i][col + i] != null && board[row - i][col + i].equals(checkValue))
                    counterDl++;
                else
                    break;
            }
            catch (IndexOutOfBoundsException ex) {
                break;
            }
        }
        if (counterDl == WinCount)
            return true;
        for (int i = 1; i <= WinCount; i++)
        {
            try {
                if (board[row +i][col - i] != null && board[row +i][col - i].equals(checkValue)){
                    counterDl++;
                    if (counterDl == WinCount)
                        return true;
                }
            }
            catch (IndexOutOfBoundsException ex) {
                break;
            }
        }
        return false;
    }

    public Boolean CheckForDiagonalRightWin (String[][] board, int row, int col, String checkValue) {
        int counterDr = 1;
        for (int i = 1; i <= WinCount; i++)
        {
            try {
                if (board[row + i][col + i]!= null && board[row + i][col + i].equals(checkValue))
                    counterDr++;
                else
                    break;
            }
            catch (IndexOutOfBoundsException ex) {
                break;
            }
        }
        if (counterDr == WinCount)
            return true;
        for (int i = 1; i <= WinCount; i++)
        {
            try {
                if (board[row - i][col - i] != null && board[row - i][col - i].equals(checkValue)){
                    counterDr++;
                    if (counterDr == WinCount)
                        return true;
                }
            }
            catch (IndexOutOfBoundsException ex) {
                break;
            }
        }
        return false;
    }
}