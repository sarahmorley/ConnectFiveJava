package com.example.connectFive;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Dictionary;
import java.util.UUID;

@RestController
public class GameController {

    @PostMapping("/game")
    public GameContract JoinGame (@RequestBody Player newPlayer) {
        Game game = new Game();
        GameContract gameContract = new GameContract();
        gameContract = game.AddPlayerToGame(newPlayer);
        return gameContract;
    }
}
