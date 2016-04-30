package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FavouriteLabel;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FavouriteLabel entity.
 */
public interface FavouriteLabelRepository extends JpaRepository<FavouriteLabel,Long> {

    @Query("select favouriteLabel from FavouriteLabel favouriteLabel where favouriteLabel.user.login = ?#{principal.username}")
    List<FavouriteLabel> findByUserIsCurrentUser();

}
