package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Song;
import com.mycompany.myapp.repository.SongRepository;
import com.mycompany.myapp.repository.search.SongSearchRepository;
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
 * REST controller for managing Song.
 */
@RestController
@RequestMapping("/api")
public class SongResource {

    private final Logger log = LoggerFactory.getLogger(SongResource.class);
        
    @Inject
    private SongRepository songRepository;
    
    @Inject
    private SongSearchRepository songSearchRepository;
    
    /**
     * POST  /songs -> Create a new song.
     */
    @RequestMapping(value = "/songs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Song> createSong(@Valid @RequestBody Song song) throws URISyntaxException {
        log.debug("REST request to save Song : {}", song);
        if (song.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("song", "idexists", "A new song cannot already have an ID")).body(null);
        }
        Song result = songRepository.save(song);
        songSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/songs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("song", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /songs -> Updates an existing song.
     */
    @RequestMapping(value = "/songs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Song> updateSong(@Valid @RequestBody Song song) throws URISyntaxException {
        log.debug("REST request to update Song : {}", song);
        if (song.getId() == null) {
            return createSong(song);
        }
        Song result = songRepository.save(song);
        songSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("song", song.getId().toString()))
            .body(result);
    }

    /**
     * GET  /songs -> get all the songs.
     */
    @RequestMapping(value = "/songs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Song>> getAllSongs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Songs");
        Page<Song> page = songRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/songs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /songs/:id -> get the "id" song.
     */
    @RequestMapping(value = "/songs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Song> getSong(@PathVariable Long id) {
        log.debug("REST request to get Song : {}", id);
        Song song = songRepository.findOne(id);
        return Optional.ofNullable(song)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /songs/:id -> delete the "id" song.
     */
    @RequestMapping(value = "/songs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        log.debug("REST request to delete Song : {}", id);
        songRepository.delete(id);
        songSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("song", id.toString())).build();
    }

    /**
     * SEARCH  /_search/songs/:query -> search for the song corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/songs/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Song> searchSongs(@PathVariable String query) {
        log.debug("REST request to search Songs for query {}", query);
        return StreamSupport
            .stream(songSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
