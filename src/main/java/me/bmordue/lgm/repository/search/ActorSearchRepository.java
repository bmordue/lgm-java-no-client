package me.bmordue.lgm.repository.search;

import me.bmordue.lgm.domain.Actor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Actor entity.
 */
public interface ActorSearchRepository extends ElasticsearchRepository<Actor, Long> {
}
