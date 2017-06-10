package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Instrument;
import com.mycompany.myapp.repository.InstrumentRepository;
import com.mycompany.myapp.repository.search.InstrumentSearchRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Instrument.
 */
@RestController
@RequestMapping("/api")
public class InstrumentResource {

    private final Logger log = LoggerFactory.getLogger(InstrumentResource.class);
        
    @Inject
    private InstrumentRepository instrumentRepository;
    
    @Inject
    private InstrumentSearchRepository instrumentSearchRepository;
    
    /**
     * POST  /instruments -> Create a new instrument.
     */
    @RequestMapping(value = "/instruments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Instrument> createInstrument(@RequestBody Instrument instrument) throws URISyntaxException {
        log.debug("REST request to save Instrument : {}", instrument);
        if (instrument.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instrument", "idexists", "A new instrument cannot already have an ID")).body(null);
        }
        Instrument result = instrumentRepository.save(instrument);
        instrumentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/instruments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instrument", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instruments -> Updates an existing instrument.
     */
    @RequestMapping(value = "/instruments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Instrument> updateInstrument(@RequestBody Instrument instrument) throws URISyntaxException {
        log.debug("REST request to update Instrument : {}", instrument);
        if (instrument.getId() == null) {
            return createInstrument(instrument);
        }
        Instrument result = instrumentRepository.save(instrument);
        instrumentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instrument", instrument.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instruments -> get all the instruments.
     */
    @RequestMapping(value = "/instruments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Instrument> getAllInstruments() {
        log.debug("REST request to get all Instruments");
        return instrumentRepository.findAll();
            }

    /**
     * GET  /instruments/:id -> get the "id" instrument.
     */
    @RequestMapping(value = "/instruments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Instrument> getInstrument(@PathVariable Long id) {
        log.debug("REST request to get Instrument : {}", id);
        Instrument instrument = instrumentRepository.findOne(id);
        return Optional.ofNullable(instrument)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instruments/:id -> delete the "id" instrument.
     */
    @RequestMapping(value = "/instruments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstrument(@PathVariable Long id) {
        log.debug("REST request to delete Instrument : {}", id);
        instrumentRepository.delete(id);
        instrumentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instrument", id.toString())).build();
    }

    /**
     * SEARCH  /_search/instruments/:query -> search for the instrument corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/instruments/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Instrument> searchInstruments(@PathVariable String query) {
        log.debug("REST request to search Instruments for query {}", query);
        return StreamSupport
            .stream(instrumentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
