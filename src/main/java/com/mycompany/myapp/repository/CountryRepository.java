package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Country entity.
 */
public interface CountryRepository extends JpaRepository<Country,Long> {

}
