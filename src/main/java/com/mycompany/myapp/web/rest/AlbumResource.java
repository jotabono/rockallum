package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Album;
import com.mycompany.myapp.domain.FavouriteAlbum;
import com.mycompany.myapp.repository.AlbumRepository;
import com.mycompany.myapp.repository.FavouriteAlbumRepository;
import com.mycompany.myapp.repository.search.AlbumSearchRepository;
import com.mycompany.myapp.web.rest.dto.AlbumDTO;
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
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing Album.
 */
@RestController
@RequestMapping("/api")
public class AlbumResource {

    private final Logger log = LoggerFactory.getLogger(AlbumResource.class);

    @Inject
    private AlbumRepository albumRepository;

    @Inject
    private AlbumSearchRepository albumSearchRepository;

    @Inject
    private FavouriteAlbumRepository favouriteAlbumRepository;

    /**
     * POST  /albums -> Create a new album.
     */
    @RequestMapping(value = "/albums",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Album> createAlbum(@Valid @RequestBody Album album) throws URISyntaxException {
        log.debug("REST request to save Album : {}", album);
        if (album.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("album", "idexists", "A new album cannot already have an ID")).body(null);
        }
        Album result = albumRepository.save(album);
        albumSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/albums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("album", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /albums -> Updates an existing album.
     */
    @RequestMapping(value = "/albums",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Album> updateAlbum(@Valid @RequestBody Album album) throws URISyntaxException {
        log.debug("REST request to update Album : {}", album);
        if (album.getId() == null) {
            return createAlbum(album);
        }
        Album result = albumRepository.save(album);
        albumSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("album", album.getId().toString()))
            .body(result);
    }

    /**
     * GET  /albums -> get all the albums.
     */
    @RequestMapping(value = "/albums",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Album>> getAllAlbums(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Albums");
        Page<Album> page = albumRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/albums");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /albums/:id -> get the "id" album.
     */
    @RequestMapping(value = "/albums/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Album> getAlbum(@PathVariable Long id) {
        log.debug("REST request to get Album : {}", id);
        Album album = albumRepository.findOne(id);
        return Optional.ofNullable(album)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /albums/:id -> delete the "id" album.
     */
    @RequestMapping(value = "/albums/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        log.debug("REST request to delete Album : {}", id);
        albumRepository.delete(id);
        albumSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("album", id.toString())).build();
    }

    /**
     * SEARCH  /_search/albums/:query -> search for the album corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/albums/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Album> searchAlbums(@PathVariable String query) {
        log.debug("REST request to search Albums for query {}", query);
        return StreamSupport
            .stream(albumSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

        /* GET ALBUM WITH DTO */

    @RequestMapping(value = "/albums/userlikedalbums",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AlbumDTO>> getAlbums(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a list of Albums");
        Page<Album> page = albumRepository.findAll(pageable);

        List<AlbumDTO> listAlbumDTO = getAlbumDTOs(page.getContent());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/albums");
        return new ResponseEntity<>(listAlbumDTO, headers, HttpStatus.OK);

    }

    private List<AlbumDTO> getAlbumDTOs(List<Album> list) {
        List<AlbumDTO> listAlbumDTO = new ArrayList<>();

        for (Album album : list) {
            FavouriteAlbum favorite = favouriteAlbumRepository.findExistUserLiked(album.getId());
            AlbumDTO albumDTO = new AlbumDTO();
            albumDTO.setAlbum(album);

            if (favorite == null || favorite.getLiked() == null || !favorite.getLiked()) {
                albumDTO.setLiked(false);
            } else {
                albumDTO.setLiked(true);
            }
            listAlbumDTO.add(albumDTO);

        }

        return listAlbumDTO;
    }

     /* Subir imagenes */

    @RequestMapping(value = "/uploadcover",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public void handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("title") String title) {
        log.debug("REST request to handleFileUpload");

        File theDir = new File("./src/main/webapp/uploads/albums");

        byte[] bytes;

        String albumTitle = "";

        try {

            if (!theDir.exists()) {
                System.out.println("creating directory: /uploads/albums");
                boolean result = false;

                try {
                    theDir.mkdir();
                    result = true;
                } catch (SecurityException se) {
                    //handle it
                }
                if (result) {
                    System.out.println("DIR created");
                }
            }


            file.getContentType();

            //Get name of file
            albumTitle = title;

            //Create new file in path
            BufferedOutputStream stream =
                new BufferedOutputStream(new FileOutputStream(new File("./src/main/webapp/uploads/albums/" + albumTitle + ".jpg")));

            stream.write(file.getBytes());
            stream.close();
            log.debug("You successfully uploaded " + file.getName() + "!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
