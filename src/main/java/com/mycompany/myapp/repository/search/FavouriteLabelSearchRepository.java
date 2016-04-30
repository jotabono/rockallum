package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.FavouriteLabel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FavouriteLabel entity.
 */
public interface FavouriteLabelSearchRepository extends ElasticsearchRepository<FavouriteLabel, Long> {
}
