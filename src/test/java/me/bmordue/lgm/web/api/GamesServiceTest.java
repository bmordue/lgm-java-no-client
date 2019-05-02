package me.bmordue.lgm.web.api;

import me.bmordue.lgm.domain.Game;
import me.bmordue.lgm.domain.Player;
import me.bmordue.lgm.repository.GameRepository;
import me.bmordue.lgm.repository.PlayerRepository;
import me.bmordue.lgm.security.IAuthenticationFacade;
import me.bmordue.lgm.service.mapper.GameMapper;
import me.bmordue.lgm.web.api.model.GameCreatedResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class GamesServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper mapper;

    @Mock
    PlayerRepository playerRepository;

    @Mock
    IAuthenticationFacade authenticationFacade;

    @InjectMocks
    private GamesService gamesService;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void createGame() {
        GameCreatedResponse mockResponse = mock(GameCreatedResponse.class);
        doReturn(mockResponse).when(mapper).gameToGameCreatedResponse(any());

        GameCreatedResponse created = gamesService.createGame();
        assertNotNull(created);
    }

    @Test
    public void joinGame() {
        String testUserLogin = "daffy";
        Player mockPlayer = mock(Player.class);

        GameCreatedResponse mockGameCreated = mock(GameCreatedResponse.class);

        long gameId = mockGameCreated.getId();
        doReturn(Optional.of(testUserLogin)).when(authenticationFacade).getCurrentUserLogin();
        doReturn(Optional.of(mockPlayer)).when(playerRepository).findByName(testUserLogin);

        gamesService.joinGame(gameId);

        verify(gameRepository).findById(gameId);
    }
}
