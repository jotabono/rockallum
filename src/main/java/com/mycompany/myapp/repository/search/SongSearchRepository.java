package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Song;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Song entity.
 */
public interface SongSearchRepository extends ElasticsearchRepository<Song, Long> {
}
