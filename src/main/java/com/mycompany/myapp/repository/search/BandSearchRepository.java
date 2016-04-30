package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Band;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Band entity.
 */
public interface BandSearchRepository extends ElasticsearchRepository<Band, Long> {
}
