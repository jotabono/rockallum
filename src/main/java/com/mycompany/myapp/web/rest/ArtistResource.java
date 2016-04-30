package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Artist;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.ArtistRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.search.ArtistSearchRepository;
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
 * REST controller for managing Artist.
 */
@RestController
@RequestMapping("/api")
public class ArtistResource {

    private final Logger log = LoggerFactory.getLogger(ArtistResource.class);

    @Inject
    private ArtistRepository artistRepository;

    @Inject
    private ArtistSearchRepository artistSearchRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /artists -> Create a new artist.
     */
    @RequestMapping(value = "/artists",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Artist> createArtist(@Valid @RequestBody Artist artist) throws URISyntaxException {
        log.debug("REST request to save Artist : {}", artist);
        if (artist.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("artist", "idexists", "A new artist cannot already have an ID")).body(null);
        }
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        artist.setUser(user);
        Artist result = artistRepository.save(artist);
        artistSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/artists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("artist", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /artists -> Updates an existing artist.
     */
    @RequestMapping(value = "/artists",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Artist> updateArtist(@Valid @RequestBody Artist artist) throws URISyntaxException {
        log.debug("REST request to update Artist : {}", artist);
        if (artist.getId() == null) {
            return createArtist(artist);
        }
        Artist result = artistRepository.save(artist);
        artistSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("artist", artist.getId().toString()))
            .body(result);
    }

    /**
     * GET  /artists -> get all the artists.
     */
    @RequestMapping(value = "/artists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Artist>> getAllArtists(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Artists");
        Page<Artist> page = artistRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/artists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /artists/:id -> get the "id" artist.
     */
    @RequestMapping(value = "/artists/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Artist> getArtist(@PathVariable Long id) {
        log.debug("REST request to get Artist : {}", id);
        Artist artist = artistRepository.findOne(id);
        return Optional.ofNullable(artist)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /artists/:id -> delete the "id" artist.
     */
    @RequestMapping(value = "/artists/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        log.debug("REST request to delete Artist : {}", id);
        artistRepository.delete(id);
        artistSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("artist", id.toString())).build();
    }

    /**
     * SEARCH  /_search/artists/:query -> search for the artist corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/artists/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Artist> searchArtists(@PathVariable String query) {
        log.debug("REST request to search Artists for query {}", query);
        return StreamSupport
            .stream(artistSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
