package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Band;
import com.mycompany.myapp.repository.BandRepository;
import com.mycompany.myapp.repository.search.BandSearchRepository;

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
 * Test class for the BandResource REST controller.
 *
 * @see BandResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BandResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;
    private static final String DEFAULT_FOUNDING_DATE = "AAAAA";
    private static final String UPDATED_FOUNDING_DATE = "BBBBB";
    private static final String DEFAULT_YEARS_ACTIVE = "AAAAA";
    private static final String UPDATED_YEARS_ACTIVE = "BBBBB";
    private static final String DEFAULT_LYRICAL_THEMES = "AAAAA";
    private static final String UPDATED_LYRICAL_THEMES = "BBBBB";

    private static final Boolean DEFAULT_INDEPENDENT = false;
    private static final Boolean UPDATED_INDEPENDENT = true;
    private static final String DEFAULT_PICTURE = "AAAAA";
    private static final String UPDATED_PICTURE = "BBBBB";
    private static final String DEFAULT_LOGO = "AAAAA";
    private static final String UPDATED_LOGO = "BBBBB";
    private static final String DEFAULT_BIO = "AAAAA";
    private static final String UPDATED_BIO = "BBBBB";

    @Inject
    private BandRepository bandRepository;

    @Inject
    private BandSearchRepository bandSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBandMockMvc;

    private Band band;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BandResource bandResource = new BandResource();
        ReflectionTestUtils.setField(bandResource, "bandSearchRepository", bandSearchRepository);
        ReflectionTestUtils.setField(bandResource, "bandRepository", bandRepository);
        this.restBandMockMvc = MockMvcBuilders.standaloneSetup(bandResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        band = new Band();
        band.setName(DEFAULT_NAME);
        band.setLocation(DEFAULT_LOCATION);
        band.setLatitude(DEFAULT_LATITUDE);
        band.setLongitude(DEFAULT_LONGITUDE);
        band.setFoundingDate(DEFAULT_FOUNDING_DATE);
        band.setYearsActive(DEFAULT_YEARS_ACTIVE);
        band.setLyricalThemes(DEFAULT_LYRICAL_THEMES);
        band.setIndependent(DEFAULT_INDEPENDENT);
        band.setPicture(DEFAULT_PICTURE);
        band.setLogo(DEFAULT_LOGO);
        band.setBio(DEFAULT_BIO);
    }

    @Test
    @Transactional
    public void createBand() throws Exception {
        int databaseSizeBeforeCreate = bandRepository.findAll().size();

        // Create the Band

        restBandMockMvc.perform(post("/api/bands")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(band)))
                .andExpect(status().isCreated());

        // Validate the Band in the database
        List<Band> bands = bandRepository.findAll();
        assertThat(bands).hasSize(databaseSizeBeforeCreate + 1);
        Band testBand = bands.get(bands.size() - 1);
        assertThat(testBand.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBand.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testBand.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testBand.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testBand.getFoundingDate()).isEqualTo(DEFAULT_FOUNDING_DATE);
        assertThat(testBand.getYearsActive()).isEqualTo(DEFAULT_YEARS_ACTIVE);
        assertThat(testBand.getLyricalThemes()).isEqualTo(DEFAULT_LYRICAL_THEMES);
        assertThat(testBand.getIndependent()).isEqualTo(DEFAULT_INDEPENDENT);
        assertThat(testBand.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testBand.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testBand.getBio()).isEqualTo(DEFAULT_BIO);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bandRepository.findAll().size();
        // set the field null
        band.setName(null);

        // Create the Band, which fails.

        restBandMockMvc.perform(post("/api/bands")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(band)))
                .andExpect(status().isBadRequest());

        List<Band> bands = bandRepository.findAll();
        assertThat(bands).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = bandRepository.findAll().size();
        // set the field null
        band.setLocation(null);

        // Create the Band, which fails.

        restBandMockMvc.perform(post("/api/bands")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(band)))
                .andExpect(status().isBadRequest());

        List<Band> bands = bandRepository.findAll();
        assertThat(bands).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBands() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bands
        restBandMockMvc.perform(get("/api/bands?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(band.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
                .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].foundingDate").value(hasItem(DEFAULT_FOUNDING_DATE.toString())))
                .andExpect(jsonPath("$.[*].yearsActive").value(hasItem(DEFAULT_YEARS_ACTIVE.toString())))
                .andExpect(jsonPath("$.[*].lyricalThemes").value(hasItem(DEFAULT_LYRICAL_THEMES.toString())))
                .andExpect(jsonPath("$.[*].independent").value(hasItem(DEFAULT_INDEPENDENT.booleanValue())))
                .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE.toString())))
                .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO.toString())))
                .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO.toString())));
    }

    @Test
    @Transactional
    public void getBand() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get the band
        restBandMockMvc.perform(get("/api/bands/{id}", band.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(band.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.foundingDate").value(DEFAULT_FOUNDING_DATE.toString()))
            .andExpect(jsonPath("$.yearsActive").value(DEFAULT_YEARS_ACTIVE.toString()))
            .andExpect(jsonPath("$.lyricalThemes").value(DEFAULT_LYRICAL_THEMES.toString()))
            .andExpect(jsonPath("$.independent").value(DEFAULT_INDEPENDENT.booleanValue()))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE.toString()))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO.toString()))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBand() throws Exception {
        // Get the band
        restBandMockMvc.perform(get("/api/bands/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBand() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

		int databaseSizeBeforeUpdate = bandRepository.findAll().size();

        // Update the band
        band.setName(UPDATED_NAME);
        band.setLocation(UPDATED_LOCATION);
        band.setLatitude(UPDATED_LATITUDE);
        band.setLongitude(UPDATED_LONGITUDE);
        band.setFoundingDate(UPDATED_FOUNDING_DATE);
        band.setYearsActive(UPDATED_YEARS_ACTIVE);
        band.setLyricalThemes(UPDATED_LYRICAL_THEMES);
        band.setIndependent(UPDATED_INDEPENDENT);
        band.setPicture(UPDATED_PICTURE);
        band.setLogo(UPDATED_LOGO);
        band.setBio(UPDATED_BIO);

        restBandMockMvc.perform(put("/api/bands")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(band)))
                .andExpect(status().isOk());

        // Validate the Band in the database
        List<Band> bands = bandRepository.findAll();
        assertThat(bands).hasSize(databaseSizeBeforeUpdate);
        Band testBand = bands.get(bands.size() - 1);
        assertThat(testBand.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBand.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testBand.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testBand.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testBand.getFoundingDate()).isEqualTo(UPDATED_FOUNDING_DATE);
        assertThat(testBand.getYearsActive()).isEqualTo(UPDATED_YEARS_ACTIVE);
        assertThat(testBand.getLyricalThemes()).isEqualTo(UPDATED_LYRICAL_THEMES);
        assertThat(testBand.getIndependent()).isEqualTo(UPDATED_INDEPENDENT);
        assertThat(testBand.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testBand.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testBand.getBio()).isEqualTo(UPDATED_BIO);
    }

    @Test
    @Transactional
    public void deleteBand() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

		int databaseSizeBeforeDelete = bandRepository.findAll().size();

        // Get the band
        restBandMockMvc.perform(delete("/api/bands/{id}", band.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Band> bands = bandRepository.findAll();
        assertThat(bands).hasSize(databaseSizeBeforeDelete - 1);
    }
}
