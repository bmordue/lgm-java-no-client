package me.bmordue.lgm.web.api;

import me.bmordue.lgm.domain.Game;
import org.springframework.stereotype.Component;

@Component
public class GameFactoryImpl implements GameFactory {

    @Override
    public Game build() {
        Game game = new Game();
        return game;
    }
}
