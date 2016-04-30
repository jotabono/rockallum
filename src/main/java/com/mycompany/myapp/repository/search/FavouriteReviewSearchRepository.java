package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.FavouriteReview;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FavouriteReview entity.
 */
public interface FavouriteReviewSearchRepository extends ElasticsearchRepository<FavouriteReview, Long> {
}
