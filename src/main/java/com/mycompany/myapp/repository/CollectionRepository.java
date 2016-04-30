package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Collection;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Collection entity.
 */
public interface CollectionRepository extends JpaRepository<Collection,Long> {

    @Query("select collection from Collection collection where collection.user.login = ?#{principal.username}")
    List<Collection> findByUserIsCurrentUser();

}
