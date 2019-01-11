package me.bmordue.lgm.repository.search;

import me.bmordue.lgm.domain.GameOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the GameOrder entity.
 */
public interface GameOrderSearchRepository extends ElasticsearchRepository<GameOrder, Long> {
}
