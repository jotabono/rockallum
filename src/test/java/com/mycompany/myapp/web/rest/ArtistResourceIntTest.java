package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Artist;
import com.mycompany.myapp.repository.ArtistRepository;
import com.mycompany.myapp.repository.search.ArtistSearchRepository;

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
 * Test class for the ArtistResource REST controller.
 *
 * @see ArtistResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ArtistResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_REAL_NAME = "AAAAA";
    private static final String UPDATED_REAL_NAME = "BBBBB";
    private static final String DEFAULT_BORN_IN = "AAAAA";
    private static final String UPDATED_BORN_IN = "BBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;
    private static final String DEFAULT_BIO = "AAAAA";
    private static final String UPDATED_BIO = "BBBBB";
    private static final String DEFAULT_ROLE = "AAAAA";
    private static final String UPDATED_ROLE = "BBBBB";
    private static final String DEFAULT_YEARS_ACTIVE = "AAAAA";
    private static final String UPDATED_YEARS_ACTIVE = "BBBBB";
    private static final String DEFAULT_LINKS = "AAAAA";
    private static final String UPDATED_LINKS = "BBBBB";
    private static final String DEFAULT_PICTURE = "AAAAA";
    private static final String UPDATED_PICTURE = "BBBBB";

    @Inject
    private ArtistRepository artistRepository;

    @Inject
    private ArtistSearchRepository artistSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restArtistMockMvc;

    private Artist artist;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ArtistResource artistResource = new ArtistResource();
        ReflectionTestUtils.setField(artistResource, "artistSearchRepository", artistSearchRepository);
        ReflectionTestUtils.setField(artistResource, "artistRepository", artistRepository);
        this.restArtistMockMvc = MockMvcBuilders.standaloneSetup(artistResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        artist = new Artist();
        artist.setName(DEFAULT_NAME);
        artist.setRealName(DEFAULT_REAL_NAME);
        artist.setBornIn(DEFAULT_BORN_IN);
        artist.setAge(DEFAULT_AGE);
        artist.setBio(DEFAULT_BIO);
        artist.setRole(DEFAULT_ROLE);
        artist.setYearsActive(DEFAULT_YEARS_ACTIVE);
        artist.setLinks(DEFAULT_LINKS);
        artist.setPicture(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    public void createArtist() throws Exception {
        int databaseSizeBeforeCreate = artistRepository.findAll().size();

        // Create the Artist

        restArtistMockMvc.perform(post("/api/artists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artist)))
                .andExpect(status().isCreated());

        // Validate the Artist in the database
        List<Artist> artists = artistRepository.findAll();
        assertThat(artists).hasSize(databaseSizeBeforeCreate + 1);
        Artist testArtist = artists.get(artists.size() - 1);
        assertThat(testArtist.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testArtist.getRealName()).isEqualTo(DEFAULT_REAL_NAME);
        assertThat(testArtist.getBornIn()).isEqualTo(DEFAULT_BORN_IN);
        assertThat(testArtist.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testArtist.getBio()).isEqualTo(DEFAULT_BIO);
        assertThat(testArtist.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testArtist.getYearsActive()).isEqualTo(DEFAULT_YEARS_ACTIVE);
        assertThat(testArtist.getLinks()).isEqualTo(DEFAULT_LINKS);
        assertThat(testArtist.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = artistRepository.findAll().size();
        // set the field null
        artist.setName(null);

        // Create the Artist, which fails.

        restArtistMockMvc.perform(post("/api/artists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artist)))
                .andExpect(status().isBadRequest());

        List<Artist> artists = artistRepository.findAll();
        assertThat(artists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllArtists() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artists
        restArtistMockMvc.perform(get("/api/artists?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(artist.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].realName").value(hasItem(DEFAULT_REAL_NAME.toString())))
                .andExpect(jsonPath("$.[*].bornIn").value(hasItem(DEFAULT_BORN_IN.toString())))
                .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
                .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO.toString())))
                .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
                .andExpect(jsonPath("$.[*].yearsActive").value(hasItem(DEFAULT_YEARS_ACTIVE.toString())))
                .andExpect(jsonPath("$.[*].links").value(hasItem(DEFAULT_LINKS.toString())))
                .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE.toString())));
    }

    @Test
    @Transactional
    public void getArtist() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get the artist
        restArtistMockMvc.perform(get("/api/artists/{id}", artist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(artist.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.realName").value(DEFAULT_REAL_NAME.toString()))
            .andExpect(jsonPath("$.bornIn").value(DEFAULT_BORN_IN.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO.toString()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.yearsActive").value(DEFAULT_YEARS_ACTIVE.toString()))
            .andExpect(jsonPath("$.links").value(DEFAULT_LINKS.toString()))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArtist() throws Exception {
        // Get the artist
        restArtistMockMvc.perform(get("/api/artists/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtist() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

		int databaseSizeBeforeUpdate = artistRepository.findAll().size();

        // Update the artist
        artist.setName(UPDATED_NAME);
        artist.setRealName(UPDATED_REAL_NAME);
        artist.setBornIn(UPDATED_BORN_IN);
        artist.setAge(UPDATED_AGE);
        artist.setBio(UPDATED_BIO);
        artist.setRole(UPDATED_ROLE);
        artist.setYearsActive(UPDATED_YEARS_ACTIVE);
        artist.setLinks(UPDATED_LINKS);
        artist.setPicture(UPDATED_PICTURE);

        restArtistMockMvc.perform(put("/api/artists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artist)))
                .andExpect(status().isOk());

        // Validate the Artist in the database
        List<Artist> artists = artistRepository.findAll();
        assertThat(artists).hasSize(databaseSizeBeforeUpdate);
        Artist testArtist = artists.get(artists.size() - 1);
        assertThat(testArtist.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testArtist.getRealName()).isEqualTo(UPDATED_REAL_NAME);
        assertThat(testArtist.getBornIn()).isEqualTo(UPDATED_BORN_IN);
        assertThat(testArtist.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testArtist.getBio()).isEqualTo(UPDATED_BIO);
        assertThat(testArtist.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testArtist.getYearsActive()).isEqualTo(UPDATED_YEARS_ACTIVE);
        assertThat(testArtist.getLinks()).isEqualTo(UPDATED_LINKS);
        assertThat(testArtist.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    public void deleteArtist() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

		int databaseSizeBeforeDelete = artistRepository.findAll().size();

        // Get the artist
        restArtistMockMvc.perform(delete("/api/artists/{id}", artist.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Artist> artists = artistRepository.findAll();
        assertThat(artists).hasSize(databaseSizeBeforeDelete - 1);
    }
}
