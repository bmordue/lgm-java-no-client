package me.bmordue.lgm.repository;

import me.bmordue.lgm.domain.ActorCommand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ActorCommand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActorCommandRepository extends JpaRepository<ActorCommand, Long> {

}
