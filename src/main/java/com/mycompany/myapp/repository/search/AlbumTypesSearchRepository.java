package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.AlbumTypes;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AlbumTypes entity.
 */
public interface AlbumTypesSearchRepository extends ElasticsearchRepository<AlbumTypes, Long> {
}
