package me.bmordue.lgm.web.api;

import me.bmordue.lgm.domain.Player;
import me.bmordue.lgm.domain.RulesProcessor;
import me.bmordue.lgm.domain.TurnOutcome;
import me.bmordue.lgm.repository.PlayerRepository;
import me.bmordue.lgm.repository.PlayerTurnRepository;
import me.bmordue.lgm.repository.TurnOutcomeRepository;
import me.bmordue.lgm.security.IAuthenticationFacade;
import me.bmordue.lgm.service.mapper.PlayerTurnMapper;
import me.bmordue.lgm.service.mapper.TurnOutcomeMapper;
import me.bmordue.lgm.web.api.model.TurnOrders;
import me.bmordue.lgm.web.api.model.TurnResultsResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class TurnsServiceTest {

    @Mock
    TurnOutcomeRepository turnOutcomeRepository;

    @Mock
    TurnOutcomeMapper turnOutcomeMapper;

    @Mock
    PlayerRepository playerRepository;

    @Mock
    PlayerTurnRepository playerTurnRepository;

    @Mock
    PlayerTurnMapper playerTurnMapper;

    @Mock
    RulesProcessor rulesProcessor;

    @Mock
    IAuthenticationFacade authenticationFacade;

    @InjectMocks
    TurnsService turnsService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void postOrders() {
        long id = 1L;
        String testUserLogin = "daffy";
        Player mockPlayer = mock(Player.class);
        doReturn(Optional.of(testUserLogin)).when(authenticationFacade).getCurrentUserLogin();
        doReturn(Optional.of(mockPlayer)).when(playerRepository).findByName(testUserLogin);
        TurnOrders mockOrders = mock(TurnOrders.class);

        turnsService.postOrders(id, mockOrders);

        Mockito.verify(rulesProcessor).process();
    }

    @Test
    public void getTurnResults() {
        long id = 1L;
        TurnOutcome mockOutcome = mock(TurnOutcome.class);
        doReturn(mockOutcome).when(turnOutcomeRepository).getOne(id);
        TurnResultsResponse mockResponse = mock(TurnResultsResponse.class);
        doReturn(mockResponse).when(turnOutcomeMapper).turnOutcomeToTurnResultsResponse(mockOutcome);

        TurnResultsResponse response = turnsService.getTurnResults(id);

        assertEquals(mockResponse, response);
    }
}
