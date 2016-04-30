package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Band;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Band entity.
 */
public interface BandRepository extends JpaRepository<Band,Long> {

    @Query("select band from Band band where band.user.login = ?#{principal.username}")
    List<Band> findByUserIsCurrentUser();

    @Query("select distinct band from Band band left join fetch band.genres left join fetch band.artists")
    List<Band> findAllWithEagerRelationships();

    @Query("select band from Band band left join fetch band.genres left join fetch band.artists where band.id =:id")
    Band findOneWithEagerRelationships(@Param("id") Long id);

}
