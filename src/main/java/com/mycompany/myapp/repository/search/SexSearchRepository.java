package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Sex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Sex entity.
 */
public interface SexSearchRepository extends ElasticsearchRepository<Sex, Long> {
}
