package me.bmordue.lgm.repository;

import me.bmordue.lgm.domain.Landscape;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Landscape entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LandscapeRepository extends JpaRepository<Landscape, Long> {

}
