package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Collection;
import com.mycompany.myapp.repository.CollectionRepository;
import com.mycompany.myapp.repository.search.CollectionSearchRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Collection.
 */
@RestController
@RequestMapping("/api")
public class CollectionResource {

    private final Logger log = LoggerFactory.getLogger(CollectionResource.class);
        
    @Inject
    private CollectionRepository collectionRepository;
    
    @Inject
    private CollectionSearchRepository collectionSearchRepository;
    
    /**
     * POST  /collections -> Create a new collection.
     */
    @RequestMapping(value = "/collections",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Collection> createCollection(@RequestBody Collection collection) throws URISyntaxException {
        log.debug("REST request to save Collection : {}", collection);
        if (collection.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("collection", "idexists", "A new collection cannot already have an ID")).body(null);
        }
        Collection result = collectionRepository.save(collection);
        collectionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/collections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("collection", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /collections -> Updates an existing collection.
     */
    @RequestMapping(value = "/collections",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Collection> updateCollection(@RequestBody Collection collection) throws URISyntaxException {
        log.debug("REST request to update Collection : {}", collection);
        if (collection.getId() == null) {
            return createCollection(collection);
        }
        Collection result = collectionRepository.save(collection);
        collectionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("collection", collection.getId().toString()))
            .body(result);
    }

    /**
     * GET  /collections -> get all the collections.
     */
    @RequestMapping(value = "/collections",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Collection> getAllCollections() {
        log.debug("REST request to get all Collections");
        return collectionRepository.findAll();
            }

    /**
     * GET  /collections/:id -> get the "id" collection.
     */
    @RequestMapping(value = "/collections/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Collection> getCollection(@PathVariable Long id) {
        log.debug("REST request to get Collection : {}", id);
        Collection collection = collectionRepository.findOne(id);
        return Optional.ofNullable(collection)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /collections/:id -> delete the "id" collection.
     */
    @RequestMapping(value = "/collections/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCollection(@PathVariable Long id) {
        log.debug("REST request to delete Collection : {}", id);
        collectionRepository.delete(id);
        collectionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("collection", id.toString())).build();
    }

    /**
     * SEARCH  /_search/collections/:query -> search for the collection corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/collections/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Collection> searchCollections(@PathVariable String query) {
        log.debug("REST request to search Collections for query {}", query);
        return StreamSupport
            .stream(collectionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
