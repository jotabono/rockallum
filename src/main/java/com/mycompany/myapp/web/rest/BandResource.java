package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Band;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.BandRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.search.BandSearchRepository;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Band.
 */
@RestController
@RequestMapping("/api")
public class BandResource {

    private final Logger log = LoggerFactory.getLogger(BandResource.class);

    @Inject
    private BandRepository bandRepository;

    @Inject
    private BandSearchRepository bandSearchRepository;

    @Inject
    private UserRepository userRepository;
    /**
     * POST  /bands -> Create a new band.
     */
    @RequestMapping(value = "/bands",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Band> createBand(@Valid @RequestBody Band band) throws URISyntaxException {
        log.debug("REST request to save Band : {}", band);
        if (band.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("band", "idexists", "A new band cannot already have an ID")).body(null);
        }
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        band.setUser(user);
        Band result = bandRepository.save(band);
        bandSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("band", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bands -> Updates an existing band.
     */
    @RequestMapping(value = "/bands",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Band> updateBand(@Valid @RequestBody Band band) throws URISyntaxException {
        log.debug("REST request to update Band : {}", band);
        if (band.getId() == null) {
            return createBand(band);
        }
        Band result = bandRepository.save(band);
        bandSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("band", band.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bands -> get all the bands.
     */
    @RequestMapping(value = "/bands",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Band>> getAllBands(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Bands");
        Page<Band> page = bandRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bands");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bands/:id -> get the "id" band.
     */
    @RequestMapping(value = "/bands/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Band> getBand(@PathVariable Long id) {
        log.debug("REST request to get Band : {}", id);
        Band band = bandRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(band)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bands/:id -> delete the "id" band.
     */
    @RequestMapping(value = "/bands/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBand(@PathVariable Long id) {
        log.debug("REST request to delete Band : {}", id);
        bandRepository.delete(id);
        bandSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("band", id.toString())).build();
    }

    /**
     * SEARCH  /_search/bands/:query -> search for the band corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/bands/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Band> searchBands(@PathVariable String query) {
        log.debug("REST request to search Bands for query {}", query);
        return StreamSupport
            .stream(bandSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
