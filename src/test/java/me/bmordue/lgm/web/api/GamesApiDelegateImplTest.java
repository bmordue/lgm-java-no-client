package me.bmordue.lgm.web.api;

import me.bmordue.lgm.web.api.model.GameCreatedResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.security.test.context.support.WithAnonymousUser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GamesApiDelegateImplTest {

    @MockBean
    private GamesService gamesService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createGame() throws Exception {
        GameCreatedResponse mockResponse = mock(GameCreatedResponse.class);
        when(gamesService.createGame()).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/games"))
//            .andDo(print())
            .andExpect(status().isOk());
    }
    
    @Test
    public void joinGame() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/games/1"))
//            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    public void joinGameUserLoginNotFound() throws Exception {
        doThrow(UserLoginNotFoundException.class).when(gamesService).joinGame(anyLong());
        mockMvc.perform(MockMvcRequestBuilders.put("/games/1"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void joinGameNotFound() throws Exception {
        doThrow(GameNotFoundException.class).when(gamesService).joinGame(anyLong());
        mockMvc.perform(MockMvcRequestBuilders.put("/games/1"))
            .andExpect(status().isNotFound());
    }
}
