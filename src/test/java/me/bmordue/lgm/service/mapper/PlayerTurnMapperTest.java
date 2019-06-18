package me.bmordue.lgm.service.mapper;


import me.bmordue.lgm.domain.Player;
import me.bmordue.lgm.domain.PlayerTurn;
import me.bmordue.lgm.web.api.model.TurnOrders;
import me.bmordue.lgm.LgmApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the PlayerTurnMapper.
 *
 * @see PlayerTurnMapper
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LgmApp.class)
public class PlayerTurnMapperTest {

    @Autowired
    private PlayerTurnMapper playerTurnMapper;

    @Test
    public void mapperCreatesNewPlayerTurn(){
        Long id = 13L;
        Player player = new Player();
        TurnOrders turnOrders = new TurnOrders();
        assertThat(playerTurnMapper.turnOrdersToPlayerTurn(id, player, turnOrders)).isNotNull();
    }
}
