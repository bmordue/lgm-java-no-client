package me.bmordue.lgm.repository;

import me.bmordue.lgm.domain.GameOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GameOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameOrderRepository extends JpaRepository<GameOrder, Long> {

}
