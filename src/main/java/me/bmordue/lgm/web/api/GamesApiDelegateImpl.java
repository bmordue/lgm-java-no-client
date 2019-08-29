package me.bmordue.lgm.web.api;

import me.bmordue.lgm.web.api.model.GameCreatedResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;

@Service
public class GamesApiDelegateImpl implements GamesApiDelegate {

    @Autowired
    GamesService gamesService;

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<GameCreatedResponse> createGame() {
        return new ResponseEntity<>(gamesService.createGame(), HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    public ResponseEntity<Void> joinGame(Long id) {
        gamesService.joinGame(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
