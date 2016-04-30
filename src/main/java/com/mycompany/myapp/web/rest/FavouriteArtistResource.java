package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.FavouriteArtist;
import com.mycompany.myapp.repository.FavouriteArtistRepository;
import com.mycompany.myapp.repository.search.FavouriteArtistSearchRepository;
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
 * REST controller for managing FavouriteArtist.
 */
@RestController
@RequestMapping("/api")
public class FavouriteArtistResource {

    private final Logger log = LoggerFactory.getLogger(FavouriteArtistResource.class);
        
    @Inject
    private FavouriteArtistRepository favouriteArtistRepository;
    
    @Inject
    private FavouriteArtistSearchRepository favouriteArtistSearchRepository;
    
    /**
     * POST  /favouriteArtists -> Create a new favouriteArtist.
     */
    @RequestMapping(value = "/favouriteArtists",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteArtist> createFavouriteArtist(@RequestBody FavouriteArtist favouriteArtist) throws URISyntaxException {
        log.debug("REST request to save FavouriteArtist : {}", favouriteArtist);
        if (favouriteArtist.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("favouriteArtist", "idexists", "A new favouriteArtist cannot already have an ID")).body(null);
        }
        FavouriteArtist result = favouriteArtistRepository.save(favouriteArtist);
        favouriteArtistSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/favouriteArtists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("favouriteArtist", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /favouriteArtists -> Updates an existing favouriteArtist.
     */
    @RequestMapping(value = "/favouriteArtists",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteArtist> updateFavouriteArtist(@RequestBody FavouriteArtist favouriteArtist) throws URISyntaxException {
        log.debug("REST request to update FavouriteArtist : {}", favouriteArtist);
        if (favouriteArtist.getId() == null) {
            return createFavouriteArtist(favouriteArtist);
        }
        FavouriteArtist result = favouriteArtistRepository.save(favouriteArtist);
        favouriteArtistSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("favouriteArtist", favouriteArtist.getId().toString()))
            .body(result);
    }

    /**
     * GET  /favouriteArtists -> get all the favouriteArtists.
     */
    @RequestMapping(value = "/favouriteArtists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FavouriteArtist> getAllFavouriteArtists() {
        log.debug("REST request to get all FavouriteArtists");
        return favouriteArtistRepository.findAll();
            }

    /**
     * GET  /favouriteArtists/:id -> get the "id" favouriteArtist.
     */
    @RequestMapping(value = "/favouriteArtists/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteArtist> getFavouriteArtist(@PathVariable Long id) {
        log.debug("REST request to get FavouriteArtist : {}", id);
        FavouriteArtist favouriteArtist = favouriteArtistRepository.findOne(id);
        return Optional.ofNullable(favouriteArtist)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /favouriteArtists/:id -> delete the "id" favouriteArtist.
     */
    @RequestMapping(value = "/favouriteArtists/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFavouriteArtist(@PathVariable Long id) {
        log.debug("REST request to delete FavouriteArtist : {}", id);
        favouriteArtistRepository.delete(id);
        favouriteArtistSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("favouriteArtist", id.toString())).build();
    }

    /**
     * SEARCH  /_search/favouriteArtists/:query -> search for the favouriteArtist corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/favouriteArtists/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FavouriteArtist> searchFavouriteArtists(@PathVariable String query) {
        log.debug("REST request to search FavouriteArtists for query {}", query);
        return StreamSupport
            .stream(favouriteArtistSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
