package me.bmordue.lgm.web.api;

import me.bmordue.lgm.domain.Player;
import me.bmordue.lgm.domain.PlayerTurn;
import me.bmordue.lgm.domain.RulesProcessor;
import me.bmordue.lgm.repository.GameRepository;
import me.bmordue.lgm.repository.PlayerRepository;
import me.bmordue.lgm.repository.PlayerTurnRepository;
import me.bmordue.lgm.repository.TurnOutcomeRepository;
import me.bmordue.lgm.security.IAuthenticationFacade;
import me.bmordue.lgm.service.mapper.PlayerTurnMapper;
import me.bmordue.lgm.service.mapper.TurnOutcomeMapper;
import me.bmordue.lgm.web.api.exceptions.PlayerNotFoundException;
import me.bmordue.lgm.web.api.exceptions.UserLoginNotFoundException;
import me.bmordue.lgm.web.api.model.TurnOrders;
import me.bmordue.lgm.web.api.model.TurnResultsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TurnsService {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    private TurnOutcomeRepository turnOutcomeRepository;

    @Autowired
    private TurnOutcomeMapper turnOutcomeMapper;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerTurnRepository playerTurnRepository;

    @Autowired
    private PlayerTurnMapper playerTurnMapper;

    @Autowired
    private RulesProcessor rulesProcessor;
    
    @Autowired
    private GameRepository gameRepository;

    void postOrders(Long id, TurnOrders turnOrders) {
        String userLogin = authenticationFacade.getCurrentUserLogin()
            .orElseThrow(UserLoginNotFoundException::new);
        Player player = playerRepository.findByNameAndGameId(userLogin, id)
            .orElseThrow(PlayerNotFoundException::new);
        PlayerTurn playerTurn = playerTurnMapper.turnOrdersToPlayerTurn(id, player, turnOrders);
        
        playerTurn.setPlayer(player);
        playerTurn.setGame(gameRepository.findById(id).orElseThrow(GameNotFoundException::new));
        
        playerTurnRepository.save(playerTurn);

        rulesProcessor.process(); // this should become asynchronous
    }

    TurnResultsResponse getTurnResults(Long id) {
        return turnOutcomeMapper.turnOutcomeToTurnResultsResponse(turnOutcomeRepository.getOne(id));
    }
}
