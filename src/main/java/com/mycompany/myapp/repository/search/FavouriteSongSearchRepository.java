package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.FavouriteSong;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FavouriteSong entity.
 */
public interface FavouriteSongSearchRepository extends ElasticsearchRepository<FavouriteSong, Long> {
}
