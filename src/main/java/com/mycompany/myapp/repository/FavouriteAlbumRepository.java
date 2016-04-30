package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FavouriteAlbum;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FavouriteAlbum entity.
 */
public interface FavouriteAlbumRepository extends JpaRepository<FavouriteAlbum,Long> {

    @Query("select favouriteAlbum from FavouriteAlbum favouriteAlbum where favouriteAlbum.user.login = ?#{principal.username}")
    List<FavouriteAlbum> findByUserIsCurrentUser();

}
