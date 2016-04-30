package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FavouriteReview;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FavouriteReview entity.
 */
public interface FavouriteReviewRepository extends JpaRepository<FavouriteReview,Long> {

    @Query("select favouriteReview from FavouriteReview favouriteReview where favouriteReview.user.login = ?#{principal.username}")
    List<FavouriteReview> findByUserIsCurrentUser();

}
