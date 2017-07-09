package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Review entity.
 */
public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Query("select review from Review review where review.user.login = ?#{principal.username}")
    List<Review> findByUserIsCurrentUser();

    @Query("select review from Review review where review.band.user.login = ?#{principal.username}")
    List<Review> findByUserIsCurrentUserAndBand();

/*    @Query("select review from Review review where review.album.user.login = ?#{principal.username}")
    List<Review> findByUserIsCurrentUserAndAlbum();*/

    @Query("select review from Review review where review.band.id = :band_id")
    List<Review> findReviewsByBand(@Param("band_id") Long id);

    @Query("select review from Review review where review.album.id = :album_id")
    List<Review> findReviewsByAlbum(@Param("album_id") Long id);
}
