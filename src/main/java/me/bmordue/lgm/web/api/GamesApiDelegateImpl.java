package me.bmordue.lgm.web.api;

import me.bmordue.lgm.web.api.model.GameCreatedResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GamesApiDelegateImpl implements GamesApiDelegate {

    @Autowired
    GamesService gamesService;

    @Override
    public ResponseEntity<GameCreatedResponse> createGame() {
        return new ResponseEntity<>(gamesService.createGame(), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> joinGame(Long id) {
        gamesService.joinGame(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
