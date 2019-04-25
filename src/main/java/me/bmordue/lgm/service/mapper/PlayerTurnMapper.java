package me.bmordue.lgm.service.mapper;

import me.bmordue.lgm.domain.Player;
import me.bmordue.lgm.domain.PlayerTurn;
import me.bmordue.lgm.web.api.model.TurnOrders;

public interface PlayerTurnMapper {
    PlayerTurn turnOrdersToPlayerTurn(Long id, Player player, TurnOrders turnOrders);
}
