package me.bmordue.lgm.repository.search;

import me.bmordue.lgm.domain.Landscape;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Landscape entity.
 */
public interface LandscapeSearchRepository extends ElasticsearchRepository<Landscape, Long> {
}
