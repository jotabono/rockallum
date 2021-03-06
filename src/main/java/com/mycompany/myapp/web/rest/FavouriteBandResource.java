package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Band;
import com.mycompany.myapp.domain.FavouriteBand;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.BandRepository;
import com.mycompany.myapp.repository.FavouriteBandRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.search.FavouriteBandSearchRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing FavouriteBand.
 */
@RestController
@RequestMapping("/api")
public class FavouriteBandResource {

    private final Logger log = LoggerFactory.getLogger(FavouriteBandResource.class);

    @Inject
    private FavouriteBandRepository favouriteBandRepository;

    @Inject
    private FavouriteBandSearchRepository favouriteBandSearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private BandRepository bandRepository;

    /**
     * POST  /favouriteBands -> Create a new favouriteBand.
     */
    @RequestMapping(value = "/favouriteBands",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteBand> createFavouriteBand(@RequestBody FavouriteBand favouriteBand) throws URISyntaxException {
        log.debug("REST request to save FavouriteBand : {}", favouriteBand);
        if (favouriteBand.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("favouriteBand", "idexists", "A new favouriteBand cannot already have an ID")).body(null);
        }
        FavouriteBand result = favouriteBandRepository.save(favouriteBand);
        favouriteBandSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/favouriteBands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("favouriteBand", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /favouriteBands -> Updates an existing favouriteBand.
     */
    @RequestMapping(value = "/favouriteBands",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteBand> updateFavouriteBand(@RequestBody FavouriteBand favouriteBand) throws URISyntaxException {
        log.debug("REST request to update FavouriteBand : {}", favouriteBand);
        if (favouriteBand.getId() == null) {
            return createFavouriteBand(favouriteBand);
        }
        FavouriteBand result = favouriteBandRepository.save(favouriteBand);
        favouriteBandSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("favouriteBand", favouriteBand.getId().toString()))
            .body(result);
    }

    /**
     * GET  /favouriteBands -> get all the favouriteBands.
     */
    @RequestMapping(value = "/favouriteBands",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FavouriteBand> getAllFavouriteBands() {
        log.debug("REST request to get all FavouriteBands");
        return favouriteBandRepository.findAll();
            }

    /**
     * GET  /favouriteBands/:id -> get the "id" favouriteBand.
     */
    @RequestMapping(value = "/favouriteBands/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteBand> getFavouriteBand(@PathVariable Long id) {
        log.debug("REST request to get FavouriteBand : {}", id);
        FavouriteBand favouriteBand = favouriteBandRepository.findOne(id);
        return Optional.ofNullable(favouriteBand)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /favouriteBands/:id -> delete the "id" favouriteBand.
     */
    @RequestMapping(value = "/favouriteBands/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFavouriteBand(@PathVariable Long id) {
        log.debug("REST request to delete FavouriteBand : {}", id);
        favouriteBandRepository.delete(id);
        favouriteBandSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("favouriteBand", id.toString())).build();
    }

    /**
     * SEARCH  /_search/favouriteBands/:query -> search for the favouriteBand corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/favouriteBands/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FavouriteBand> searchFavouriteBands(@PathVariable String query) {
        log.debug("REST request to search FavouriteBands for query {}", query);
        return StreamSupport
            .stream(favouriteBandSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /* LIKE */

    @RequestMapping(value = "/favorites/{id}/like",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteBand> likeBand(@PathVariable Long id) throws URISyntaxException {

        FavouriteBand exist = favouriteBandRepository.findExistUserLiked(id);

        if(exist != null){
            if(exist.getLiked() == null || exist.getLiked() == false){
                exist.setLiked(true);
            }else{
                exist.setLiked(false);
            }
            return updateFavouriteBand(exist);
        }

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        Band band = bandRepository.findOne(id);

        FavouriteBand favourite = new FavouriteBand();
        favourite.setUser(user);
        favourite.setBand(band);
        favourite.setLiked(true);

        FavouriteBand result = favouriteBandRepository.save(favourite);
        return ResponseEntity.created(new URI("/api/favouriteBands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("favouriteBands", result.getId().toString()))
            .body(result);
    }

    /* GET LIKED BANDS BY USER LOGGED */

    @RequestMapping(value = "/favouriteBandss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FavouriteBand>> getAllFaBandsUser(Pageable pageable)
        throws URISyntaxException {
        Page<FavouriteBand> page = favouriteBandRepository.findLikesUserLogged(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/favouriteBandss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
