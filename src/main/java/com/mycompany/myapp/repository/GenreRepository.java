package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Genre entity.
 */
public interface GenreRepository extends JpaRepository<Genre,Long> {

}
