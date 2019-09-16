package me.bmordue.lgm.web.api;

import me.bmordue.lgm.web.api.model.GameInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GamesApiDelegateImpl implements GamesApiDelegate {

    @Autowired
    private GamesService gamesService;

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<GameInfoResponse> createGame() {
        return new ResponseEntity<>(gamesService.createGame(), HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<GameInfoResponse> getGame(Long id) {
        return new ResponseEntity<>(gamesService.getGameInfo(id), HttpStatus.OK);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> joinGame(Long id) {
        gamesService.joinGame(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
