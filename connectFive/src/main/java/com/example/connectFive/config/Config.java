package com.example.connectFive.config;
import com.example.connectFive.Game;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public Game game() {
        return new Game();
    }
}
