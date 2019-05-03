package me.bmordue.lgm.service.mapper;

import me.bmordue.lgm.domain.Game;
import me.bmordue.lgm.web.api.model.GameCreatedResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameMapper {
    GameCreatedResponse gameToGameCreatedResponse(Game source);
}
