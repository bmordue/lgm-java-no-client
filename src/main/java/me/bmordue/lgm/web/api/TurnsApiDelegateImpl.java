package me.bmordue.lgm.web.api;

import me.bmordue.lgm.web.api.model.TurnOrders;
import me.bmordue.lgm.web.api.model.TurnResultsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@Service
public class TurnsApiDelegateImpl implements TurnsApiDelegate {

    @Autowired
    TurnsService turnsService;

    @Override
    public ResponseEntity<Void> postOrders(Integer id, TurnOrders turnOrders) {
        turnsService.postOrders(id, turnOrders);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<TurnResultsResponse> turnResults(Integer id) {
        return new ResponseEntity<TurnResultsResponse>(turnsService.getTurnResults(id));
    }
}
