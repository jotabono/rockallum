package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.FavouriteReview;
import com.mycompany.myapp.repository.FavouriteReviewRepository;
import com.mycompany.myapp.repository.search.FavouriteReviewSearchRepository;
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
 * REST controller for managing FavouriteReview.
 */
@RestController
@RequestMapping("/api")
public class FavouriteReviewResource {

    private final Logger log = LoggerFactory.getLogger(FavouriteReviewResource.class);
        
    @Inject
    private FavouriteReviewRepository favouriteReviewRepository;
    
    @Inject
    private FavouriteReviewSearchRepository favouriteReviewSearchRepository;
    
    /**
     * POST  /favouriteReviews -> Create a new favouriteReview.
     */
    @RequestMapping(value = "/favouriteReviews",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteReview> createFavouriteReview(@RequestBody FavouriteReview favouriteReview) throws URISyntaxException {
        log.debug("REST request to save FavouriteReview : {}", favouriteReview);
        if (favouriteReview.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("favouriteReview", "idexists", "A new favouriteReview cannot already have an ID")).body(null);
        }
        FavouriteReview result = favouriteReviewRepository.save(favouriteReview);
        favouriteReviewSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/favouriteReviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("favouriteReview", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /favouriteReviews -> Updates an existing favouriteReview.
     */
    @RequestMapping(value = "/favouriteReviews",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteReview> updateFavouriteReview(@RequestBody FavouriteReview favouriteReview) throws URISyntaxException {
        log.debug("REST request to update FavouriteReview : {}", favouriteReview);
        if (favouriteReview.getId() == null) {
            return createFavouriteReview(favouriteReview);
        }
        FavouriteReview result = favouriteReviewRepository.save(favouriteReview);
        favouriteReviewSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("favouriteReview", favouriteReview.getId().toString()))
            .body(result);
    }

    /**
     * GET  /favouriteReviews -> get all the favouriteReviews.
     */
    @RequestMapping(value = "/favouriteReviews",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FavouriteReview> getAllFavouriteReviews() {
        log.debug("REST request to get all FavouriteReviews");
        return favouriteReviewRepository.findAll();
            }

    /**
     * GET  /favouriteReviews/:id -> get the "id" favouriteReview.
     */
    @RequestMapping(value = "/favouriteReviews/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteReview> getFavouriteReview(@PathVariable Long id) {
        log.debug("REST request to get FavouriteReview : {}", id);
        FavouriteReview favouriteReview = favouriteReviewRepository.findOne(id);
        return Optional.ofNullable(favouriteReview)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /favouriteReviews/:id -> delete the "id" favouriteReview.
     */
    @RequestMapping(value = "/favouriteReviews/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFavouriteReview(@PathVariable Long id) {
        log.debug("REST request to delete FavouriteReview : {}", id);
        favouriteReviewRepository.delete(id);
        favouriteReviewSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("favouriteReview", id.toString())).build();
    }

    /**
     * SEARCH  /_search/favouriteReviews/:query -> search for the favouriteReview corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/favouriteReviews/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FavouriteReview> searchFavouriteReviews(@PathVariable String query) {
        log.debug("REST request to search FavouriteReviews for query {}", query);
        return StreamSupport
            .stream(favouriteReviewSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
