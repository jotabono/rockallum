package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Status;
import com.mycompany.myapp.repository.StatusRepository;
import com.mycompany.myapp.repository.search.StatusSearchRepository;
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
 * REST controller for managing Status.
 */
@RestController
@RequestMapping("/api")
public class StatusResource {

    private final Logger log = LoggerFactory.getLogger(StatusResource.class);
        
    @Inject
    private StatusRepository statusRepository;
    
    @Inject
    private StatusSearchRepository statusSearchRepository;
    
    /**
     * POST  /statuss -> Create a new status.
     */
    @RequestMapping(value = "/statuss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Status> createStatus(@RequestBody Status status) throws URISyntaxException {
        log.debug("REST request to save Status : {}", status);
        if (status.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("status", "idexists", "A new status cannot already have an ID")).body(null);
        }
        Status result = statusRepository.save(status);
        statusSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/statuss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("status", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /statuss -> Updates an existing status.
     */
    @RequestMapping(value = "/statuss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Status> updateStatus(@RequestBody Status status) throws URISyntaxException {
        log.debug("REST request to update Status : {}", status);
        if (status.getId() == null) {
            return createStatus(status);
        }
        Status result = statusRepository.save(status);
        statusSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("status", status.getId().toString()))
            .body(result);
    }

    /**
     * GET  /statuss -> get all the statuss.
     */
    @RequestMapping(value = "/statuss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Status> getAllStatuss() {
        log.debug("REST request to get all Statuss");
        return statusRepository.findAll();
            }

    /**
     * GET  /statuss/:id -> get the "id" status.
     */
    @RequestMapping(value = "/statuss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Status> getStatus(@PathVariable Long id) {
        log.debug("REST request to get Status : {}", id);
        Status status = statusRepository.findOne(id);
        return Optional.ofNullable(status)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /statuss/:id -> delete the "id" status.
     */
    @RequestMapping(value = "/statuss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStatus(@PathVariable Long id) {
        log.debug("REST request to delete Status : {}", id);
        statusRepository.delete(id);
        statusSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("status", id.toString())).build();
    }

    /**
     * SEARCH  /_search/statuss/:query -> search for the status corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/statuss/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Status> searchStatuss(@PathVariable String query) {
        log.debug("REST request to search Statuss for query {}", query);
        return StreamSupport
            .stream(statusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
