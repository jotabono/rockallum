package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.FavouriteArtist;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FavouriteArtist entity.
 */
public interface FavouriteArtistSearchRepository extends ElasticsearchRepository<FavouriteArtist, Long> {
}
