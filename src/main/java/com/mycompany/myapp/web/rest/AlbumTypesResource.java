package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.AlbumTypes;
import com.mycompany.myapp.repository.AlbumTypesRepository;
import com.mycompany.myapp.repository.search.AlbumTypesSearchRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing AlbumTypes.
 */
@RestController
@RequestMapping("/api")
public class AlbumTypesResource {

    private final Logger log = LoggerFactory.getLogger(AlbumTypesResource.class);
        
    @Inject
    private AlbumTypesRepository albumTypesRepository;
    
    @Inject
    private AlbumTypesSearchRepository albumTypesSearchRepository;
    
    /**
     * POST  /albumTypess -> Create a new albumTypes.
     */
    @RequestMapping(value = "/albumTypess",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AlbumTypes> createAlbumTypes(@Valid @RequestBody AlbumTypes albumTypes) throws URISyntaxException {
        log.debug("REST request to save AlbumTypes : {}", albumTypes);
        if (albumTypes.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("albumTypes", "idexists", "A new albumTypes cannot already have an ID")).body(null);
        }
        AlbumTypes result = albumTypesRepository.save(albumTypes);
        albumTypesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/albumTypess/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("albumTypes", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /albumTypess -> Updates an existing albumTypes.
     */
    @RequestMapping(value = "/albumTypess",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AlbumTypes> updateAlbumTypes(@Valid @RequestBody AlbumTypes albumTypes) throws URISyntaxException {
        log.debug("REST request to update AlbumTypes : {}", albumTypes);
        if (albumTypes.getId() == null) {
            return createAlbumTypes(albumTypes);
        }
        AlbumTypes result = albumTypesRepository.save(albumTypes);
        albumTypesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("albumTypes", albumTypes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /albumTypess -> get all the albumTypess.
     */
    @RequestMapping(value = "/albumTypess",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AlbumTypes> getAllAlbumTypess() {
        log.debug("REST request to get all AlbumTypess");
        return albumTypesRepository.findAll();
            }

    /**
     * GET  /albumTypess/:id -> get the "id" albumTypes.
     */
    @RequestMapping(value = "/albumTypess/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AlbumTypes> getAlbumTypes(@PathVariable Long id) {
        log.debug("REST request to get AlbumTypes : {}", id);
        AlbumTypes albumTypes = albumTypesRepository.findOne(id);
        return Optional.ofNullable(albumTypes)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /albumTypess/:id -> delete the "id" albumTypes.
     */
    @RequestMapping(value = "/albumTypess/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAlbumTypes(@PathVariable Long id) {
        log.debug("REST request to delete AlbumTypes : {}", id);
        albumTypesRepository.delete(id);
        albumTypesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("albumTypes", id.toString())).build();
    }

    /**
     * SEARCH  /_search/albumTypess/:query -> search for the albumTypes corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/albumTypess/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AlbumTypes> searchAlbumTypess(@PathVariable String query) {
        log.debug("REST request to search AlbumTypess for query {}", query);
        return StreamSupport
            .stream(albumTypesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
