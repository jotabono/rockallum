package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.FavouriteSong;
import com.mycompany.myapp.repository.FavouriteSongRepository;
import com.mycompany.myapp.repository.search.FavouriteSongSearchRepository;
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
 * REST controller for managing FavouriteSong.
 */
@RestController
@RequestMapping("/api")
public class FavouriteSongResource {

    private final Logger log = LoggerFactory.getLogger(FavouriteSongResource.class);
        
    @Inject
    private FavouriteSongRepository favouriteSongRepository;
    
    @Inject
    private FavouriteSongSearchRepository favouriteSongSearchRepository;
    
    /**
     * POST  /favouriteSongs -> Create a new favouriteSong.
     */
    @RequestMapping(value = "/favouriteSongs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteSong> createFavouriteSong(@RequestBody FavouriteSong favouriteSong) throws URISyntaxException {
        log.debug("REST request to save FavouriteSong : {}", favouriteSong);
        if (favouriteSong.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("favouriteSong", "idexists", "A new favouriteSong cannot already have an ID")).body(null);
        }
        FavouriteSong result = favouriteSongRepository.save(favouriteSong);
        favouriteSongSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/favouriteSongs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("favouriteSong", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /favouriteSongs -> Updates an existing favouriteSong.
     */
    @RequestMapping(value = "/favouriteSongs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteSong> updateFavouriteSong(@RequestBody FavouriteSong favouriteSong) throws URISyntaxException {
        log.debug("REST request to update FavouriteSong : {}", favouriteSong);
        if (favouriteSong.getId() == null) {
            return createFavouriteSong(favouriteSong);
        }
        FavouriteSong result = favouriteSongRepository.save(favouriteSong);
        favouriteSongSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("favouriteSong", favouriteSong.getId().toString()))
            .body(result);
    }

    /**
     * GET  /favouriteSongs -> get all the favouriteSongs.
     */
    @RequestMapping(value = "/favouriteSongs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FavouriteSong> getAllFavouriteSongs() {
        log.debug("REST request to get all FavouriteSongs");
        return favouriteSongRepository.findAll();
            }

    /**
     * GET  /favouriteSongs/:id -> get the "id" favouriteSong.
     */
    @RequestMapping(value = "/favouriteSongs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteSong> getFavouriteSong(@PathVariable Long id) {
        log.debug("REST request to get FavouriteSong : {}", id);
        FavouriteSong favouriteSong = favouriteSongRepository.findOne(id);
        return Optional.ofNullable(favouriteSong)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /favouriteSongs/:id -> delete the "id" favouriteSong.
     */
    @RequestMapping(value = "/favouriteSongs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFavouriteSong(@PathVariable Long id) {
        log.debug("REST request to delete FavouriteSong : {}", id);
        favouriteSongRepository.delete(id);
        favouriteSongSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("favouriteSong", id.toString())).build();
    }

    /**
     * SEARCH  /_search/favouriteSongs/:query -> search for the favouriteSong corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/favouriteSongs/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FavouriteSong> searchFavouriteSongs(@PathVariable String query) {
        log.debug("REST request to search FavouriteSongs for query {}", query);
        return StreamSupport
            .stream(favouriteSongSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
