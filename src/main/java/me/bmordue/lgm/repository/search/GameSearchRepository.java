package me.bmordue.lgm.repository.search;

import me.bmordue.lgm.domain.Game;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Game entity.
 */
public interface GameSearchRepository extends ElasticsearchRepository<Game, Long> {
}
