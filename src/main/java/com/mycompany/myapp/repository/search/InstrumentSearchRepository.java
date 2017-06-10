package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Instrument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Instrument entity.
 */
public interface InstrumentSearchRepository extends ElasticsearchRepository<Instrument, Long> {
}
