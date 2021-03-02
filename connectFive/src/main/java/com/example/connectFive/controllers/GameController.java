package com.example.connectFive.controllers;
import com.example.connectFive.Game;
import com.example.connectFive.contracts.GameContract;
import com.example.connectFive.contracts.Player;
import com.example.connectFive.contracts.Turn;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
public class GameController {

    @Autowired
    private Game game;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/game")
    public GameContract JoinGame (@RequestBody Player newPlayer) {
        GameContract gameContract = new GameContract();
        gameContract = game.AddPlayerToGame(newPlayer);
        return gameContract;
    }

    @GetMapping("/game/{gameId}")
    public GameContract CheckTurn (@PathVariable UUID gameId) {
        GameContract gameContract = new GameContract();
        gameContract = game.CheckTurn(gameId);
        return gameContract;
    }

    @PatchMapping("/game/{gameId}")
    public GameContract PlayTurn(@PathVariable UUID gameId, @RequestBody Turn turn) {
        GameContract gameContract = new GameContract();
        gameContract = game.PlayTurn(gameId, turn);
        return gameContract;
    }
}
