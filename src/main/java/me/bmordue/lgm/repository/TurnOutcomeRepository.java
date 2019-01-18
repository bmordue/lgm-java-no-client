package me.bmordue.lgm.repository;

import me.bmordue.lgm.domain.TurnOutcome;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TurnOutcome entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TurnOutcomeRepository extends JpaRepository<TurnOutcome, Long> {

}
