package me.bmordue.lgm.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of PlayerSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PlayerSearchRepositoryMockConfiguration {

    @MockBean
    private PlayerSearchRepository mockPlayerSearchRepository;

}
