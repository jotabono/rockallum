package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Album;
import com.mycompany.myapp.domain.FavouriteAlbum;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.AlbumRepository;
import com.mycompany.myapp.repository.FavouriteAlbumRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.search.FavouriteAlbumSearchRepository;
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

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing FavouriteAlbum.
 */
@RestController
@RequestMapping("/api")
public class FavouriteAlbumResource {

    private final Logger log = LoggerFactory.getLogger(FavouriteAlbumResource.class);

    @Inject
    private FavouriteAlbumRepository favouriteAlbumRepository;

    @Inject
    private FavouriteAlbumSearchRepository favouriteAlbumSearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private AlbumRepository albumRepository;

    /**
     * POST  /favouriteAlbums -> Create a new favouriteAlbum.
     */
    @RequestMapping(value = "/favouriteAlbums",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteAlbum> createFavouriteAlbum(@RequestBody FavouriteAlbum favouriteAlbum) throws URISyntaxException {
        log.debug("REST request to save FavouriteAlbum : {}", favouriteAlbum);
        if (favouriteAlbum.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("favouriteAlbum", "idexists", "A new favouriteAlbum cannot already have an ID")).body(null);
        }
        FavouriteAlbum result = favouriteAlbumRepository.save(favouriteAlbum);
        favouriteAlbumSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/favouriteAlbums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("favouriteAlbum", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /favouriteAlbums -> Updates an existing favouriteAlbum.
     */
    @RequestMapping(value = "/favouriteAlbums",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteAlbum> updateFavouriteAlbum(@RequestBody FavouriteAlbum favouriteAlbum) throws URISyntaxException {
        log.debug("REST request to update FavouriteAlbum : {}", favouriteAlbum);
        if (favouriteAlbum.getId() == null) {
            return createFavouriteAlbum(favouriteAlbum);
        }
        FavouriteAlbum result = favouriteAlbumRepository.save(favouriteAlbum);
        favouriteAlbumSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("favouriteAlbum", favouriteAlbum.getId().toString()))
            .body(result);
    }

    /**
     * GET  /favouriteAlbums -> get all the favouriteAlbums.
     */
    @RequestMapping(value = "/favouriteAlbums",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FavouriteAlbum> getAllFavouriteAlbums() {
        log.debug("REST request to get all FavouriteAlbums");
        return favouriteAlbumRepository.findAll();
            }

    /**
     * GET  /favouriteAlbums/:id -> get the "id" favouriteAlbum.
     */
    @RequestMapping(value = "/favouriteAlbums/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteAlbum> getFavouriteAlbum(@PathVariable Long id) {
        log.debug("REST request to get FavouriteAlbum : {}", id);
        FavouriteAlbum favouriteAlbum = favouriteAlbumRepository.findOne(id);
        return Optional.ofNullable(favouriteAlbum)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /favouriteAlbums/:id -> delete the "id" favouriteAlbum.
     */
    @RequestMapping(value = "/favouriteAlbums/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFavouriteAlbum(@PathVariable Long id) {
        log.debug("REST request to delete FavouriteAlbum : {}", id);
        favouriteAlbumRepository.delete(id);
        favouriteAlbumSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("favouriteAlbum", id.toString())).build();
    }

    /**
     * SEARCH  /_search/favouriteAlbums/:query -> search for the favouriteAlbum corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/favouriteAlbums/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FavouriteAlbum> searchFavouriteAlbums(@PathVariable String query) {
        log.debug("REST request to search FavouriteAlbums for query {}", query);
        return StreamSupport
            .stream(favouriteAlbumSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


    /* LIKE */

    @RequestMapping(value = "/favouritealbums/{id}/like",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FavouriteAlbum> likeAlbum(@PathVariable Long id) throws URISyntaxException {

        FavouriteAlbum exist = favouriteAlbumRepository.findExistUserLiked(id);

        if(exist != null){
            if(exist.getLiked() == null || exist.getLiked() == false){
                exist.setLiked(true);
            }else{
                exist.setLiked(false);
            }
            return updateFavouriteAlbum(exist);
        }

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        Album album = albumRepository.findOne(id);

        FavouriteAlbum favourite = new FavouriteAlbum();
        favourite.setUser(user);
        favourite.setAlbum(album);
        favourite.setLiked(true);

        FavouriteAlbum result = favouriteAlbumRepository.save(favourite);
        return ResponseEntity.created(new URI("/api/favouriteAlbums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("favouriteAlbums", result.getId().toString()))
            .body(result);
    }

    /* GET LIKED ALBUMS BY USER LOGGED */

    @RequestMapping(value = "/favouriteAlbumss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FavouriteAlbum>> getAllFaAlbumsUser(Pageable pageable)
        throws URISyntaxException {
        Page<FavouriteAlbum> page = favouriteAlbumRepository.findLikesUserLogged(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/favouriteAlbumss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
