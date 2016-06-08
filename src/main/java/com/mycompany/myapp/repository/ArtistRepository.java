package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Artist;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Artist entity.
 */
public interface ArtistRepository extends JpaRepository<Artist,Long> {

    @Query("select artist from Artist artist where artist.user.login = ?#{principal.username}")
    List<Artist> findByUserIsCurrentUser();

    @Query("select distinct artist from Artist artist left join fetch artist.socials left join fetch artist.instruments")
    List<Artist> findAllWithEagerRelationships();

    @Query("select artist from Artist artist left join fetch artist.socials left join fetch artist.instruments where artist.id =:id")
    Artist findOneWithEagerRelationships(@Param("id") Long id);

}
