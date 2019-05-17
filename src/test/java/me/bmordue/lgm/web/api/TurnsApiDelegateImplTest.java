package me.bmordue.lgm.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.bmordue.lgm.web.api.exceptions.PlayerNotFoundException;
import me.bmordue.lgm.web.api.exceptions.UserLoginNotFoundException;
import me.bmordue.lgm.web.api.model.TurnOrders;
import me.bmordue.lgm.web.api.model.TurnResultsResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TurnsApiDelegateImplTest {

    @MockBean
    private TurnsService turnsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void postOrders() throws Exception {
        String jsonContent = objectMapper.writeValueAsString(new TurnOrders());

        mockMvc.perform(MockMvcRequestBuilders
            .post("/turns/1").contentType("application/json").content(jsonContent))
//            .andDo(print())
            .andExpect(status().isAccepted());
    }

    @Test
    public void turnResults() throws Exception {
        TurnResultsResponse mockResults = mock(TurnResultsResponse.class);
        when(turnsService.getTurnResults(any())).thenReturn(mockResults);

        mockMvc.perform(MockMvcRequestBuilders.get("/turns/1"))
//            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void postOrdersUserLoginNotFound() throws Exception {
        String jsonContent = objectMapper.writeValueAsString(new TurnOrders());
        doThrow(UserLoginNotFoundException.class).when(turnsService).postOrders(any(), any());

        mockMvc.perform(MockMvcRequestBuilders
            .post("/turns/1").contentType("application/json").content(jsonContent))
            .andExpect(status().isUnauthorized());
    }
            
    @Test
    public void postOrdersPlayerNotFound() throws Exception {
        String jsonContent = objectMapper.writeValueAsString(new TurnOrders());
        doThrow(PlayerNotFoundException.class).when(turnsService).postOrders(any(), any());

        mockMvc.perform(MockMvcRequestBuilders
            .post("/turns/1").contentType("application/json").content(jsonContent))
            .andExpect(status().isInternalServerError());
    }


}
