package me.bmordue.lgm.repository.search;

import me.bmordue.lgm.domain.Turn;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Turn entity.
 */
public interface TurnSearchRepository extends ElasticsearchRepository<Turn, Long> {
}
