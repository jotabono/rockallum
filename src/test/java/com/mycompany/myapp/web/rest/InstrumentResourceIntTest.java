package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Instrument;
import com.mycompany.myapp.repository.InstrumentRepository;
import com.mycompany.myapp.repository.search.InstrumentSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the InstrumentResource REST controller.
 *
 * @see InstrumentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class InstrumentResourceIntTest {

    private static final String DEFAULT_INSTRUMENT = "AAAAA";
    private static final String UPDATED_INSTRUMENT = "BBBBB";

    @Inject
    private InstrumentRepository instrumentRepository;

    @Inject
    private InstrumentSearchRepository instrumentSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInstrumentMockMvc;

    private Instrument instrument;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstrumentResource instrumentResource = new InstrumentResource();
        ReflectionTestUtils.setField(instrumentResource, "instrumentSearchRepository", instrumentSearchRepository);
        ReflectionTestUtils.setField(instrumentResource, "instrumentRepository", instrumentRepository);
        this.restInstrumentMockMvc = MockMvcBuilders.standaloneSetup(instrumentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        instrument = new Instrument();
        instrument.setInstrument(DEFAULT_INSTRUMENT);
    }

    @Test
    @Transactional
    public void createInstrument() throws Exception {
        int databaseSizeBeforeCreate = instrumentRepository.findAll().size();

        // Create the Instrument

        restInstrumentMockMvc.perform(post("/api/instruments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instrument)))
                .andExpect(status().isCreated());

        // Validate the Instrument in the database
        List<Instrument> instruments = instrumentRepository.findAll();
        assertThat(instruments).hasSize(databaseSizeBeforeCreate + 1);
        Instrument testInstrument = instruments.get(instruments.size() - 1);
        assertThat(testInstrument.getInstrument()).isEqualTo(DEFAULT_INSTRUMENT);
    }

    @Test
    @Transactional
    public void getAllInstruments() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        // Get all the instruments
        restInstrumentMockMvc.perform(get("/api/instruments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instrument.getId().intValue())))
                .andExpect(jsonPath("$.[*].instrument").value(hasItem(DEFAULT_INSTRUMENT.toString())));
    }

    @Test
    @Transactional
    public void getInstrument() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        // Get the instrument
        restInstrumentMockMvc.perform(get("/api/instruments/{id}", instrument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(instrument.getId().intValue()))
            .andExpect(jsonPath("$.instrument").value(DEFAULT_INSTRUMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstrument() throws Exception {
        // Get the instrument
        restInstrumentMockMvc.perform(get("/api/instruments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstrument() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

		int databaseSizeBeforeUpdate = instrumentRepository.findAll().size();

        // Update the instrument
        instrument.setInstrument(UPDATED_INSTRUMENT);

        restInstrumentMockMvc.perform(put("/api/instruments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instrument)))
                .andExpect(status().isOk());

        // Validate the Instrument in the database
        List<Instrument> instruments = instrumentRepository.findAll();
        assertThat(instruments).hasSize(databaseSizeBeforeUpdate);
        Instrument testInstrument = instruments.get(instruments.size() - 1);
        assertThat(testInstrument.getInstrument()).isEqualTo(UPDATED_INSTRUMENT);
    }

    @Test
    @Transactional
    public void deleteInstrument() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

		int databaseSizeBeforeDelete = instrumentRepository.findAll().size();

        // Get the instrument
        restInstrumentMockMvc.perform(delete("/api/instruments/{id}", instrument.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Instrument> instruments = instrumentRepository.findAll();
        assertThat(instruments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
