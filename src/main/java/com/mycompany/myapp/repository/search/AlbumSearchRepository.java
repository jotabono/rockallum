package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Album;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Album entity.
 */
public interface AlbumSearchRepository extends ElasticsearchRepository<Album, Long> {
}
