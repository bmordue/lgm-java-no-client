package me.bmordue.lgm.repository;

import me.bmordue.lgm.domain.Game;
import me.bmordue.lgm.domain.GameTurn;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GameTurn entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameTurnRepository extends JpaRepository<GameTurn, Long> {
	GameTurn findFirstByGameOrderByTurnNumberDesc(Game game);
}
