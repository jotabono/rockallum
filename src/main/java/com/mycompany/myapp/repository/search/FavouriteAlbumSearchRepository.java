package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.FavouriteAlbum;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FavouriteAlbum entity.
 */
public interface FavouriteAlbumSearchRepository extends ElasticsearchRepository<FavouriteAlbum, Long> {
}
