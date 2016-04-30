package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Country;
import com.mycompany.myapp.repository.CountryRepository;
import com.mycompany.myapp.repository.search.CountrySearchRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Country.
 */
@RestController
@RequestMapping("/api")
public class CountryResource {

    private final Logger log = LoggerFactory.getLogger(CountryResource.class);

    @Inject
    private CountryRepository countryRepository;

    @Inject
    private CountrySearchRepository countrySearchRepository;

    /**
     * POST  /countrys -> Create a new country.
     */
    @RequestMapping(value = "/countrys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Country> createCountry(@RequestBody Country country) throws URISyntaxException {
        log.debug("REST request to save Country : {}", country);
        if (country.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("country", "idexists", "A new country cannot already have an ID")).body(null);
        }
        Country result = countryRepository.save(country);
        countrySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/countrys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("country", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /countrys -> Updates an existing country.
     */
    @RequestMapping(value = "/countrys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Country> updateCountry(@RequestBody Country country) throws URISyntaxException {
        log.debug("REST request to update Country : {}", country);
        if (country.getId() == null) {
            return createCountry(country);
        }
        Country result = countryRepository.save(country);
        countrySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("country", country.getId().toString()))
            .body(result);
    }

    /**
     * GET  /countrys -> get all the countrys WITHOUT PAGINATION
     */
    @RequestMapping(value = "/countrys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Country> getAllCountrys()
        throws URISyntaxException {
        log.debug("REST request to get a page of Countrys");
        return countryRepository.findAll();
    }

    /**
     * GET  /countrys -> get all the countrys.
     */
    @RequestMapping(value = "/countryspaginated",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Country>> getAllCountrysPaginated(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Countrys");
        Page<Country> page = countryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/countrys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /countrys/:id -> get the "id" country.
     */
    @RequestMapping(value = "/countrys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Country> getCountry(@PathVariable Long id) {
        log.debug("REST request to get Country : {}", id);
        Country country = countryRepository.findOne(id);
        return Optional.ofNullable(country)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /countrys/:id -> delete the "id" country.
     */
    @RequestMapping(value = "/countrys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        log.debug("REST request to delete Country : {}", id);
        countryRepository.delete(id);
        countrySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("country", id.toString())).build();
    }

    /**
     * SEARCH  /_search/countrys/:query -> search for the country corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/countrys/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Country> searchCountrys(@PathVariable String query) {
        log.debug("REST request to search Countrys for query {}", query);
        return StreamSupport
            .stream(countrySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
