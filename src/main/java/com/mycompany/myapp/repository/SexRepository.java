package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Sex;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sex entity.
 */
public interface SexRepository extends JpaRepository<Sex,Long> {

}
