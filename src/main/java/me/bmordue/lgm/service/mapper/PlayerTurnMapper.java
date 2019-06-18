package me.bmordue.lgm.service.mapper;

import me.bmordue.lgm.domain.Player;
import me.bmordue.lgm.domain.PlayerTurn;
import me.bmordue.lgm.web.api.model.TurnOrders;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerTurnMapper {
    default PlayerTurn turnOrdersToPlayerTurn(Long id, Player player, TurnOrders turnOrders) {
        return new PlayerTurn();
    }
}
