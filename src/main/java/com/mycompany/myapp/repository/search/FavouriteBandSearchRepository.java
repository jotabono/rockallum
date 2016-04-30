package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.FavouriteBand;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FavouriteBand entity.
 */
public interface FavouriteBandSearchRepository extends ElasticsearchRepository<FavouriteBand, Long> {
}
