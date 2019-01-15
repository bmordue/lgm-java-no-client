package me.bmordue.lgm.service.mapper;

import me.bmordue.lgm.domain.*;
import me.bmordue.lgm.service.dto.GameTurnDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity GameTurn and its DTO GameTurnDTO.
 */
@Mapper(componentModel = "spring", uses = {TurnOutcomeMapper.class, LandscapeMapper.class, GameMapper.class})
public interface GameTurnMapper extends EntityMapper<GameTurnDTO, GameTurn> {

    @Mapping(source = "outcome.id", target = "outcomeId")
    @Mapping(source = "landscape.id", target = "landscapeId")
    @Mapping(source = "game.id", target = "gameId")
    GameTurnDTO toDto(GameTurn gameTurn);

    @Mapping(source = "outcomeId", target = "outcome")
    @Mapping(source = "landscapeId", target = "landscape")
    @Mapping(target = "actors", ignore = true)
    @Mapping(target = "landscapes", ignore = true)
    @Mapping(target = "playerTurns", ignore = true)
    @Mapping(source = "gameId", target = "game")
    GameTurn toEntity(GameTurnDTO gameTurnDTO);

    default GameTurn fromId(Long id) {
        if (id == null) {
            return null;
        }
        GameTurn gameTurn = new GameTurn();
        gameTurn.setId(id);
        return gameTurn;
    }
}
