package com.example.connectFive;

import java.util.UUID;

public class GameContract {
    public UUID gameId;
    public String[][] board;
    public GameStatus gameStatus;
    public String turn;
    public Player player1;
    public Player player2;

    public String[][] getBoard() {
        return board;
    }
    public void setBoard(String[][] board) {
        this.board = board;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID id) {
        this.gameId = id;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn){
        this.turn = turn;
    }
}
