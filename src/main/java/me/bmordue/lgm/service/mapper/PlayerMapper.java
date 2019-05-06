package me.bmordue.lgm.service.mapper;
import me.bmordue.lgm.domain.Player;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    Player userLoginToPlayer(String userLogin);
}
