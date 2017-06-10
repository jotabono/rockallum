package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Instrument;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Instrument entity.
 */
public interface InstrumentRepository extends JpaRepository<Instrument,Long> {

}
