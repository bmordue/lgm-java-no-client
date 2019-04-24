package me.bmordue.lgm.web.api;

import me.bmordue.lgm.domain.Game;
import me.bmordue.lgm.repository.GameRepository;
import me.bmordue.lgm.service.mapper.GameMapper;
import me.bmordue.lgm.web.api.model.GameCreatedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(AuthenticationException::new);
        Player player = playerRepository.findByLogin(userLogin).orElseThrow(AuthenticationException::new);
        
        Optional<Game> game = gameRepository.findById(id);
        if (game.isPresent()) {
            game.addPlayer(player);
            gameRepository.save(game);
        } // else log or throw!
    }
}
