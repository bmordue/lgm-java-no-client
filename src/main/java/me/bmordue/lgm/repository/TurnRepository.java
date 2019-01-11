package me.bmordue.lgm.repository;

import me.bmordue.lgm.domain.Turn;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Turn entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TurnRepository extends JpaRepository<Turn, Long> {

}
