package com.example.connectFive;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Game {
    public static HashMap<UUID, GameContract> gameMap = new HashMap<UUID, GameContract>();

    public HashMap<UUID, GameContract> getGameMap() {
        return gameMap;
    }

    public void setGameMap(HashMap<UUID, GameContract> gameMap) {
        this.gameMap = gameMap;
    }

    public GameContract AddPlayerToGame(Player newPlayer) {
        //If there is a game with only one player add the newPlayer to this game
        //Else create a new game and add the player
        GameContract game = new GameContract();
        if(gameMap.isEmpty())
            CreateNewGame(newPlayer.getPlayerId(), game);
        else {
            for (Map.Entry<UUID, GameContract> entry : gameMap.entrySet()){
                UUID key = entry.getKey();
                GameContract CreatedGame = entry.getValue();
                if (CreatedGame.getPlayer2() == null) {
                    CreatedGame.setPlayer2(newPlayer);
                    CreatedGame.setGameStatus(GameStatus.TWOPLAYERGAME);
                    CreatedGame.player2.setPlayerId(newPlayer.getPlayerId());
                    CreatedGame.player2.setColour("yellow");
                    return CreatedGame;
                }
            }
            CreateNewGame(newPlayer.getPlayerId(), game);
        }
        return game;
    }

    public GameContract CreateNewGame(String playerId, GameContract game) {
        String[][] board = new String[6][9];
        Player player = new Player();
        player.setPlayerId(playerId);
        player.setColour("red");

        game.setGameId(UUID.randomUUID());
        game.setPlayer1(player);
        game.setGameStatus(GameStatus.CREATEDWITHONEPLAYER);
        game.setTurn(playerId);
        game.setBoard(board);
        this.gameMap.put(game.gameId, game);
        return game;
    }


    public Boolean CheckForVerticalWin(String[][] board, int row, int col, String checkValue) {
        int counterV = 1;
        for (int i = 1; i <=5; i++)
        {
            try {
                if(board[row + i] [col] == checkValue)
                    counterV++;
                else
                    break;
            }
            catch (IndexOutOfBoundsException ex) {
                break;
            }
        }
        if (counterV == 5)
            return true;

        return false;
    }

    public Boolean CheckForHorizontalWin (String[][] board, int row, int col, String checkValue) {
        int counterH = 1;
        for (int i = 1; i <=5; i++)
        {
            try {
                if (board[row][col + i] == checkValue)
                    counterH++;
                else
                    break;
            }
            catch (IndexOutOfBoundsException ex) {
                break;
            }
        }
        if (counterH ==5)
            return true;

        for (int i = 1; i <=5; i++)
        {
            try {
                if (board[row][col - i] == checkValue) {
                    counterH++;
                    if (counterH ==5)
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
        for (int i = 1; i <=5; i++)
        {
            try {
                if (board[row - i][col + i] == checkValue)
                    counterDl++;
                else
                    break;
            }
            catch (IndexOutOfBoundsException ex) {
                break;
            }
        }
        if (counterDl ==5)
            return true;
        for (int i = 1; i <=5; i++)
        {
            try {
                if (board[row +i][col - i] == checkValue){
                    counterDl++;
                    if (counterDl == 4)
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
        for (int i = 1; i <=5; i++)
        {
            try {
                if (board[row + i][col + i] == checkValue)
                    counterDr++;
                else
                    break;
            }
            catch (IndexOutOfBoundsException ex) {
                break;
            }
        }
        if (counterDr ==5)
            return true;
        for (int i = 1; i <=5; i++)
        {
            try {
                if (board[row - i][col - i] == checkValue){
                    counterDr++;
                    if (counterDr == 4)
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
