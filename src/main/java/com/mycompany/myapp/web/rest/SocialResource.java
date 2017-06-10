package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Social;
import com.mycompany.myapp.repository.SocialRepository;
import com.mycompany.myapp.repository.search.SocialSearchRepository;
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
 * REST controller for managing Social.
 */
@RestController
@RequestMapping("/api")
public class SocialResource {

    private final Logger log = LoggerFactory.getLogger(SocialResource.class);
        
    @Inject
    private SocialRepository socialRepository;
    
    @Inject
    private SocialSearchRepository socialSearchRepository;
    
    /**
     * POST  /socials -> Create a new social.
     */
    @RequestMapping(value = "/socials",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Social> createSocial(@RequestBody Social social) throws URISyntaxException {
        log.debug("REST request to save Social : {}", social);
        if (social.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("social", "idexists", "A new social cannot already have an ID")).body(null);
        }
        Social result = socialRepository.save(social);
        socialSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/socials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("social", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /socials -> Updates an existing social.
     */
    @RequestMapping(value = "/socials",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Social> updateSocial(@RequestBody Social social) throws URISyntaxException {
        log.debug("REST request to update Social : {}", social);
        if (social.getId() == null) {
            return createSocial(social);
        }
        Social result = socialRepository.save(social);
        socialSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("social", social.getId().toString()))
            .body(result);
    }

    /**
     * GET  /socials -> get all the socials.
     */
    @RequestMapping(value = "/socials",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Social> getAllSocials() {
        log.debug("REST request to get all Socials");
        return socialRepository.findAll();
            }

    /**
     * GET  /socials/:id -> get the "id" social.
     */
    @RequestMapping(value = "/socials/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Social> getSocial(@PathVariable Long id) {
        log.debug("REST request to get Social : {}", id);
        Social social = socialRepository.findOne(id);
        return Optional.ofNullable(social)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /socials/:id -> delete the "id" social.
     */
    @RequestMapping(value = "/socials/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSocial(@PathVariable Long id) {
        log.debug("REST request to delete Social : {}", id);
        socialRepository.delete(id);
        socialSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("social", id.toString())).build();
    }

    /**
     * SEARCH  /_search/socials/:query -> search for the social corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/socials/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Social> searchSocials(@PathVariable String query) {
        log.debug("REST request to search Socials for query {}", query);
        return StreamSupport
            .stream(socialSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
