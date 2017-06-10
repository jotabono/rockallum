package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Social;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Social entity.
 */
public interface SocialSearchRepository extends ElasticsearchRepository<Social, Long> {
}
