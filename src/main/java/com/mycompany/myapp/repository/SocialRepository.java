package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Social;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Social entity.
 */
public interface SocialRepository extends JpaRepository<Social,Long> {

}
