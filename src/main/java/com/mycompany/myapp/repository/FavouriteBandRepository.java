package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FavouriteBand;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FavouriteBand entity.
 */
public interface FavouriteBandRepository extends JpaRepository<FavouriteBand,Long> {

    @Query("select favouriteBand from FavouriteBand favouriteBand where favouriteBand.user.login = ?#{principal.username}")
    List<FavouriteBand> findByUserIsCurrentUser();

}
