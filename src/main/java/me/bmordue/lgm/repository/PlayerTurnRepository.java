package me.bmordue.lgm.repository;

import me.bmordue.lgm.domain.PlayerTurn;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PlayerTurn entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerTurnRepository extends JpaRepository<PlayerTurn, Long> {

}
