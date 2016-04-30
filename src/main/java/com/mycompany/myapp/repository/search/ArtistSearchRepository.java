package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Artist;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Artist entity.
 */
public interface ArtistSearchRepository extends ElasticsearchRepository<Artist, Long> {
}
