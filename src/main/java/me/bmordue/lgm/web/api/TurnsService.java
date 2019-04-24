package me.bmordue.lgm.web.api;

import me.bmordue.lgm.repository.GameTurnRepository;
import me.bmordue.lgm.repository.TurnOutcomeRepository;
import me.bmordue.lgm.security.SecurityUtils;
import me.bmordue.lgm.service.mapper.TurnOutcomeMapper;
import me.bmordue.lgm.web.api.model.TurnOrders;
import me.bmordue.lgm.web.api.model.TurnResultsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TurnsService {

    @Autowired
    GameTurnRepository gameTurnRepository;

    @Autowired
    TurnOutcomeRepository turnOutcomeRepository;

    @Autowired
    TurnOutcomeMapper turnOutcomeMapper;
    
    @Autowired
    PlayerRepository playerRepository;
    
    @Autowired
    PlayerTurnRepository playerTurnRepository;
    
    @Autowired
    PlayerTurnMapper playerTurnMapper;
    
    @Autowired
    RulesProcessor rulesProcessor();

    public void postOrders(Long id, TurnOrders turnOrders) throws AuthenticationException {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(AuthenticationException::new);
        Player player = playerRepository.findByLogin(userLogin).orElseThrow(AuthenticationException::new);
        PlayerTurn playerTurn = playerTurnMapper.turnOrdersToPlayerTurn(player, turnOrders);
        playerTurnRepository.save(playerTurn);
        rulesProcessor.process(); // this should become asynchronous
    }

    public TurnResultsResponse getTurnResults(Long id) {
        return turnOutcomeMapper.turnOutcomeToTurnResultsResponse(turnOutcomeRepository.getOne(id));
    }
}
