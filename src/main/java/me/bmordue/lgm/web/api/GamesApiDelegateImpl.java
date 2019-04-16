package me.bmordue.lgm.web.api;

import me.bmordue.lgm.domain.Game;
import me.bmordue.lgm.repository.GameRepository;
import me.bmordue.lgm.repository.GameTurnRepository;
import me.bmordue.lgm.repository.PlayerTurnRepository;
import me.bmordue.lgm.repository.TurnOutcomeRepository;
import me.bmordue.lgm.web.api.model.GameCreatedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

public class GamesApiDelegateImpl implements GamesApiDelegate {

    @Autowired
    GameRepository gameRepository;

    @Override
    public ResponseEntity<GameCreatedResponse> createGame() {
        return null;
    }

    @Override
    public ResponseEntity<Void> joinGame(Integer id) {
        return null;
    }
}