package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FavouriteArtist;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FavouriteArtist entity.
 */
public interface FavouriteArtistRepository extends JpaRepository<FavouriteArtist,Long> {

    @Query("select favouriteArtist from FavouriteArtist favouriteArtist where favouriteArtist.user.login = ?#{principal.username}")
    List<FavouriteArtist> findByUserIsCurrentUser();

}
