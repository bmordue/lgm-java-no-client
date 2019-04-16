package me.bmordue.lgm.web.api;

import me.bmordue.lgm.repository.GameTurnRepository;
import me.bmordue.lgm.repository.PlayerTurnRepository;
import me.bmordue.lgm.repository.TurnOutcomeRepository;
import me.bmordue.lgm.web.api.model.TurnOrders;
import me.bmordue.lgm.web.api.model.TurnResultsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

public class TurnsApiDelegateImpl implements TurnsApiDelegate {

    @Autowired
    GameTurnRepository gameTurnRepository;

    @Autowired
    PlayerTurnRepository playerTurnRepository;

    @Autowired
    TurnOutcomeRepository turnOutcomeRepository;

    @Override
    public ResponseEntity<Void> postOrders(Integer id, TurnOrders turnOrders) {
        return null;
    }

    @Override
    public ResponseEntity<TurnResultsResponse> turnResults(Integer id) {
        return null;
    }
}
