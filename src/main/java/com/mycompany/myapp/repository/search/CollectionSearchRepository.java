package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Collection;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Collection entity.
 */
public interface CollectionSearchRepository extends ElasticsearchRepository<Collection, Long> {
}
