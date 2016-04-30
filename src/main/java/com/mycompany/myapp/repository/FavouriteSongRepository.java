package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FavouriteSong;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FavouriteSong entity.
 */
public interface FavouriteSongRepository extends JpaRepository<FavouriteSong,Long> {

    @Query("select favouriteSong from FavouriteSong favouriteSong where favouriteSong.user.login = ?#{principal.username}")
    List<FavouriteSong> findByUserIsCurrentUser();

}
