package me.bmordue.lgm.web.api;

import me.bmordue.lgm.web.api.model.TurnOrders;
import me.bmordue.lgm.web.api.model.TurnResultsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class TurnsApiDelegateImpl implements TurnsApiDelegate {

    @Autowired
    TurnsService turnsService;

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> postOrders(Long id, TurnOrders turnOrders) {
        turnsService.postOrders(id, turnOrders);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TurnResultsResponse> turnResults(Long id) {
        return new ResponseEntity<>(turnsService.getTurnResults(id), HttpStatus.OK);
    }
}
