package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Genre;
import com.mycompany.myapp.repository.GenreRepository;
import com.mycompany.myapp.repository.search.GenreSearchRepository;
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

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Genre.
 */
@RestController
@RequestMapping("/api")
public class GenreResource {

    private final Logger log = LoggerFactory.getLogger(GenreResource.class);

    @Inject
    private GenreRepository genreRepository;

    @Inject
    private GenreSearchRepository genreSearchRepository;

    /**
     * POST  /genres -> Create a new genre.
     */
    @RequestMapping(value = "/genres",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) throws URISyntaxException {
        log.debug("REST request to save Genre : {}", genre);
        if (genre.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("genre", "idexists", "A new genre cannot already have an ID")).body(null);
        }
        Genre result = genreRepository.save(genre);
        genreSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/genres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("genre", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /genres -> Updates an existing genre.
     */
    @RequestMapping(value = "/genres",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genre> updateGenre(@RequestBody Genre genre) throws URISyntaxException {
        log.debug("REST request to update Genre : {}", genre);
        if (genre.getId() == null) {
            return createGenre(genre);
        }
        Genre result = genreRepository.save(genre);
        genreSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("genre", genre.getId().toString()))
            .body(result);
    }

    /**
     * GET  /countrys -> get all the countrys WITHOUT PAGINATION
     */
    @RequestMapping(value = "/genres",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Genre> getAllGenres()
        throws URISyntaxException {
        log.debug("REST request to get a page of Genres");
        return genreRepository.findAll();
    }

    /**
     * GET  /genres -> get all the genres.
     */
    @RequestMapping(value = "/genrespaginated",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Genre>> getAllGenresPaginated(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Genres");
        Page<Genre> page = genreRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/genres");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /genres/:id -> get the "id" genre.
     */
    @RequestMapping(value = "/genres/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genre> getGenre(@PathVariable Long id) {
        log.debug("REST request to get Genre : {}", id);
        Genre genre = genreRepository.findOne(id);
        return Optional.ofNullable(genre)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /genres/:id -> delete the "id" genre.
     */
    @RequestMapping(value = "/genres/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        log.debug("REST request to delete Genre : {}", id);
        genreRepository.delete(id);
        genreSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("genre", id.toString())).build();
    }

    /**
     * SEARCH  /_search/genres/:query -> search for the genre corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/genres/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Genre> searchGenres(@PathVariable String query) {
        log.debug("REST request to search Genres for query {}", query);
        return StreamSupport
            .stream(genreSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
