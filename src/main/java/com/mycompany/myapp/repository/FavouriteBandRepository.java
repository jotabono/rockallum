package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FavouriteBand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the FavouriteBand entity.
 */
public interface FavouriteBandRepository extends JpaRepository<FavouriteBand,Long> {

    @Query("select favouriteBand from FavouriteBand favouriteBand where favouriteBand.user.login = ?#{principal.username}")
    List<FavouriteBand> findByUserIsCurrentUser();

    @Query("select favouriteBand from FavouriteBand favouriteBand where favouriteBand.user.login = ?#{principal.username} AND favouriteBand.band.id = :band_id")
    FavouriteBand findExistUserLiked(@Param("band_id") Long id);

    @Query("select favouriteBand from FavouriteBand favouriteBand where favouriteBand.user.login = ?#{principal.username} AND favouriteBand.liked = true")
    Page<FavouriteBand> findLikesUserLogged(Pageable pageable);
}
