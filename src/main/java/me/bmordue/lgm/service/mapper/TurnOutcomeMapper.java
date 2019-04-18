package me.bmordue.lgm.service.mapper;

import me.bmordue.lgm.domain.TurnOutcome;
import me.bmordue.lgm.web.api.model.TurnResultsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TurnOutcomeMapper {
    TurnResultsResponse turnOutcomeToTurnResultsResponse(TurnOutcome source);
}
