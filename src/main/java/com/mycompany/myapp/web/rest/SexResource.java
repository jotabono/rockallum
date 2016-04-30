package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Sex;
import com.mycompany.myapp.repository.SexRepository;
import com.mycompany.myapp.repository.search.SexSearchRepository;
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
 * REST controller for managing Sex.
 */
@RestController
@RequestMapping("/api")
public class SexResource {

    private final Logger log = LoggerFactory.getLogger(SexResource.class);
        
    @Inject
    private SexRepository sexRepository;
    
    @Inject
    private SexSearchRepository sexSearchRepository;
    
    /**
     * POST  /sexs -> Create a new sex.
     */
    @RequestMapping(value = "/sexs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sex> createSex(@RequestBody Sex sex) throws URISyntaxException {
        log.debug("REST request to save Sex : {}", sex);
        if (sex.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("sex", "idexists", "A new sex cannot already have an ID")).body(null);
        }
        Sex result = sexRepository.save(sex);
        sexSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sexs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sex", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sexs -> Updates an existing sex.
     */
    @RequestMapping(value = "/sexs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sex> updateSex(@RequestBody Sex sex) throws URISyntaxException {
        log.debug("REST request to update Sex : {}", sex);
        if (sex.getId() == null) {
            return createSex(sex);
        }
        Sex result = sexRepository.save(sex);
        sexSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sex", sex.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sexs -> get all the sexs.
     */
    @RequestMapping(value = "/sexs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Sex> getAllSexs() {
        log.debug("REST request to get all Sexs");
        return sexRepository.findAll();
            }

    /**
     * GET  /sexs/:id -> get the "id" sex.
     */
    @RequestMapping(value = "/sexs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sex> getSex(@PathVariable Long id) {
        log.debug("REST request to get Sex : {}", id);
        Sex sex = sexRepository.findOne(id);
        return Optional.ofNullable(sex)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sexs/:id -> delete the "id" sex.
     */
    @RequestMapping(value = "/sexs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSex(@PathVariable Long id) {
        log.debug("REST request to delete Sex : {}", id);
        sexRepository.delete(id);
        sexSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sex", id.toString())).build();
    }

    /**
     * SEARCH  /_search/sexs/:query -> search for the sex corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/sexs/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Sex> searchSexs(@PathVariable String query) {
        log.debug("REST request to search Sexs for query {}", query);
        return StreamSupport
            .stream(sexSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
