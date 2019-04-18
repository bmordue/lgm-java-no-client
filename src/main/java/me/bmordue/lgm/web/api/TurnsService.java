package me.bmordue.lgm.web.api;

import me.bmordue.lgm.repository.GameTurnRepository;
import me.bmordue.lgm.repository.TurnOutcomeRepository;
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

    public void postOrders(Long id, TurnOrders turnOrders) {
// TODO IMPLEMENTATION
    }

    public TurnResultsResponse getTurnResults(Long id) {
        return turnOutcomeMapper.turnOutcomeToTurnResultsResponse(turnOutcomeRepository.getOne(id));
    }
}
