package me.bmordue.lgm.web.api;

import me.bmordue.lgm.domain.Game;
import me.bmordue.lgm.domain.Player;
import me.bmordue.lgm.repository.GameRepository;
import me.bmordue.lgm.repository.PlayerRepository;
import me.bmordue.lgm.security.IAuthenticationFacade;
import me.bmordue.lgm.service.mapper.GameMapper;
import me.bmordue.lgm.service.mapper.PlayerMapper;
import me.bmordue.lgm.web.api.exceptions.GameNotFoundException;
import me.bmordue.lgm.web.api.exceptions.UserLoginNotFoundException;
import me.bmordue.lgm.web.api.model.GameInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Autowired
    private GameFactory gameFactory;

    private Player findOrCreatePlayerForCurrentUserLogin(Game game) {
        String userLogin = authenticationFacade.getCurrentUserLogin()
            .orElseThrow(UserLoginNotFoundException::new);
        Player loginPlayer = playerMapper.userLoginToPlayer(userLogin);
        loginPlayer.setName(userLogin);
        loginPlayer.setGame(game);

        // or use find by example
        Optional<Player> existingPlayer = playerRepository.findByNameAndGameId(loginPlayer.getName(), game.getId());
        return existingPlayer.orElse(playerRepository.save(loginPlayer));
    }

    GameInfoResponse createGame() {
        Game game = gameFactory.build();
        return gameMapper.gameToGameInfoResponse(gameRepository.save(game));
    }

    @Transactional(readOnly = true)
    void joinGame(Long id) {
        Game game = gameRepository.findById(id)
            .orElseThrow(GameNotFoundException::new);
        game.addPlayer(findOrCreatePlayerForCurrentUserLogin(game));
        gameRepository.save(game);
    }

    @Transactional(readOnly = true)
    GameInfoResponse getGameInfo(Long id) {
        Game game = gameRepository.findById(id)
            .orElseThrow(GameNotFoundException::new);
        return gameMapper.gameToGameInfoResponse(game);
    }
}
