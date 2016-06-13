package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FavouriteAlbum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the FavouriteAlbum entity.
 */
public interface FavouriteAlbumRepository extends JpaRepository<FavouriteAlbum,Long> {

    @Query("select favouriteAlbum from FavouriteAlbum favouriteAlbum where favouriteAlbum.user.login = ?#{principal.username}")
    List<FavouriteAlbum> findByUserIsCurrentUser();

    @Query("select favouriteAlbum from FavouriteAlbum favouriteAlbum where favouriteAlbum.user.login = ?#{principal.username} AND favouriteAlbum.album.id = :album_id")
    FavouriteAlbum findExistUserLiked(@Param("album_id") Long id);

    @Query("select favouriteAlbum from FavouriteAlbum favouriteAlbum where favouriteAlbum.user.login = ?#{principal.username} AND favouriteAlbum.liked = true")
    Page<FavouriteAlbum> findLikesUserLogged(Pageable pageable);

}
