package me.bmordue.lgm.service.mapper;

import me.bmordue.lgm.domain.*;
import me.bmordue.lgm.service.dto.LandscapeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Landscape and its DTO LandscapeDTO.
 */
@Mapper(componentModel = "spring", uses = {GameTurnMapper.class})
public interface LandscapeMapper extends EntityMapper<LandscapeDTO, Landscape> {

    @Mapping(source = "turn.id", target = "turnId")
    LandscapeDTO toDto(Landscape landscape);

    @Mapping(target = "turn", ignore = true)
    @Mapping(source = "turnId", target = "turn")
    Landscape toEntity(LandscapeDTO landscapeDTO);

    default Landscape fromId(Long id) {
        if (id == null) {
            return null;
        }
        Landscape landscape = new Landscape();
        landscape.setId(id);
        return landscape;
    }
}
