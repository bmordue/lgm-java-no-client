package me.bmordue.lgm.web.api;

import me.bmordue.lgm.domain.Game;
import me.bmordue.lgm.domain.Player;
import me.bmordue.lgm.repository.GameRepository;
import me.bmordue.lgm.repository.PlayerRepository;
import me.bmordue.lgm.security.IAuthenticationFacade;
import me.bmordue.lgm.service.mapper.GameMapper;
import me.bmordue.lgm.service.mapper.PlayerMapper;
import me.bmordue.lgm.web.api.exceptions.GameNotFoundException;
import me.bmordue.lgm.web.api.exceptions.LgmServiceException;
import me.bmordue.lgm.web.api.exceptions.UserLoginNotFoundException;
import me.bmordue.lgm.web.api.model.GameCreatedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class GamesService {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameMapper gameMapper;
    
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerMapper playerMapper;

    GameCreatedResponse createGame() {
        Game game = new Game();
        return gameMapper.gameToGameCreatedResponse(gameRepository.save(game));
    }

    void joinGame(Long id) throws LgmServiceException {
        Game game = gameRepository.findById(id).orElseThrow(GameNotFoundException::new);

        String userLogin = authenticationFacade.getCurrentUserLogin()
            .orElseThrow(UserLoginNotFoundException::new);
        Player player = playerMapper.userLoginToPlayer(userLogin);
        playerRepository.save(player);

        game.addPlayer(player);
        gameRepository.save(game);
    }
}
