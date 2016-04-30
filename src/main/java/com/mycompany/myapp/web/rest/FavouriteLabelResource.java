package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.FavouriteLabel;
import com.mycompany.myapp.repository.FavouriteLabelRepository;
import com.mycompany.myapp.repository.search.FavouriteLabelSearchRepository;
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
 * REST controller for managing FavouriteLabel.
 */
@RestController
@RequestMapping("/api")
public class FavouriteLabelResource {

    private final Logger log = LoggerFactory.getLogger(FavouriteLabelResource.class);
        
    @Inject
    private FavouriteLabelRepository favouriteLabelRepository;
    
    @Inject
    private FavouriteLabelSearchRepository favouriteLabelSearchRepository;
    
    /**
     * POST  /favouriteLabels -> Create a new favouriteLabel.
     */
    @RequestMapping(value = "/favouriteLabels",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteLabel> createFavouriteLabel(@RequestBody FavouriteLabel favouriteLabel) throws URISyntaxException {
        log.debug("REST request to save FavouriteLabel : {}", favouriteLabel);
        if (favouriteLabel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("favouriteLabel", "idexists", "A new favouriteLabel cannot already have an ID")).body(null);
        }
        FavouriteLabel result = favouriteLabelRepository.save(favouriteLabel);
        favouriteLabelSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/favouriteLabels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("favouriteLabel", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /favouriteLabels -> Updates an existing favouriteLabel.
     */
    @RequestMapping(value = "/favouriteLabels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteLabel> updateFavouriteLabel(@RequestBody FavouriteLabel favouriteLabel) throws URISyntaxException {
        log.debug("REST request to update FavouriteLabel : {}", favouriteLabel);
        if (favouriteLabel.getId() == null) {
            return createFavouriteLabel(favouriteLabel);
        }
        FavouriteLabel result = favouriteLabelRepository.save(favouriteLabel);
        favouriteLabelSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("favouriteLabel", favouriteLabel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /favouriteLabels -> get all the favouriteLabels.
     */
    @RequestMapping(value = "/favouriteLabels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FavouriteLabel> getAllFavouriteLabels() {
        log.debug("REST request to get all FavouriteLabels");
        return favouriteLabelRepository.findAll();
            }

    /**
     * GET  /favouriteLabels/:id -> get the "id" favouriteLabel.
     */
    @RequestMapping(value = "/favouriteLabels/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteLabel> getFavouriteLabel(@PathVariable Long id) {
        log.debug("REST request to get FavouriteLabel : {}", id);
        FavouriteLabel favouriteLabel = favouriteLabelRepository.findOne(id);
        return Optional.ofNullable(favouriteLabel)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /favouriteLabels/:id -> delete the "id" favouriteLabel.
     */
    @RequestMapping(value = "/favouriteLabels/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFavouriteLabel(@PathVariable Long id) {
        log.debug("REST request to delete FavouriteLabel : {}", id);
        favouriteLabelRepository.delete(id);
        favouriteLabelSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("favouriteLabel", id.toString())).build();
    }

    /**
     * SEARCH  /_search/favouriteLabels/:query -> search for the favouriteLabel corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/favouriteLabels/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FavouriteLabel> searchFavouriteLabels(@PathVariable String query) {
        log.debug("REST request to search FavouriteLabels for query {}", query);
        return StreamSupport
            .stream(favouriteLabelSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
