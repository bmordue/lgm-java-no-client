package me.bmordue.lgm.repository.search;

import me.bmordue.lgm.domain.Player;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Player entity.
 */
public interface PlayerSearchRepository extends ElasticsearchRepository<Player, Long> {
}
