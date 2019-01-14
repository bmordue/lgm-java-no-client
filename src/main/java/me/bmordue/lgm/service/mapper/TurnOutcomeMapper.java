package me.bmordue.lgm.service.mapper;

import me.bmordue.lgm.domain.*;
import me.bmordue.lgm.service.dto.TurnOutcomeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TurnOutcome and its DTO TurnOutcomeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TurnOutcomeMapper extends EntityMapper<TurnOutcomeDTO, TurnOutcome> {


    @Mapping(target = "turn", ignore = true)
    TurnOutcome toEntity(TurnOutcomeDTO turnOutcomeDTO);

    default TurnOutcome fromId(Long id) {
        if (id == null) {
            return null;
        }
        TurnOutcome turnOutcome = new TurnOutcome();
        turnOutcome.setId(id);
        return turnOutcome;
    }
}
