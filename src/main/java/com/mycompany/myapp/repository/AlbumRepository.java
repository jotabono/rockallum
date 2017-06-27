package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Album entity.
 */
public interface AlbumRepository extends JpaRepository<Album,Long> {

}
