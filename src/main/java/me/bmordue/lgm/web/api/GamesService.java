package me.bmordue.lgm.web.api;

import me.bmordue.lgm.domain.Game;
import me.bmordue.lgm.domain.Player;
import me.bmordue.lgm.repository.GameRepository;
import me.bmordue.lgm.repository.PlayerRepository;
import me.bmordue.lgm.security.SecurityUtils;
import me.bmordue.lgm.service.mapper.GameMapper;
import me.bmordue.lgm.web.api.model.GameCreatedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class GamesService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameMapper mapper;
    
    @Autowired
    PlayerRepository playerRepository;

    GameCreatedResponse createGame() {
        Game game = new Game();
        return mapper.gameToGameCreatedResponse(gameRepository.save(game));
    }

    void joinGame(Long id) throws AuthenticationException {
        String userLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("bad auth"));
        Player player = playerRepository.findByName(userLogin)
            .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("bad auth"));

        Optional<Game> game = gameRepository.findById(id);
        if (game.isPresent()) {
            game.get().addPlayer(player);
            gameRepository.save(game.get());
        } // TODO: else log or throw!
    }
}
