package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.FavouriteArtist;
import com.mycompany.myapp.repository.FavouriteArtistRepository;
import com.mycompany.myapp.repository.search.FavouriteArtistSearchRepository;

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
 * Test class for the FavouriteArtistResource REST controller.
 *
 * @see FavouriteArtistResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FavouriteArtistResourceIntTest {


    private static final Boolean DEFAULT_LIKED = false;
    private static final Boolean UPDATED_LIKED = true;

    @Inject
    private FavouriteArtistRepository favouriteArtistRepository;

    @Inject
    private FavouriteArtistSearchRepository favouriteArtistSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFavouriteArtistMockMvc;

    private FavouriteArtist favouriteArtist;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FavouriteArtistResource favouriteArtistResource = new FavouriteArtistResource();
        ReflectionTestUtils.setField(favouriteArtistResource, "favouriteArtistSearchRepository", favouriteArtistSearchRepository);
        ReflectionTestUtils.setField(favouriteArtistResource, "favouriteArtistRepository", favouriteArtistRepository);
        this.restFavouriteArtistMockMvc = MockMvcBuilders.standaloneSetup(favouriteArtistResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        favouriteArtist = new FavouriteArtist();
        favouriteArtist.setLiked(DEFAULT_LIKED);
    }

    @Test
    @Transactional
    public void createFavouriteArtist() throws Exception {
        int databaseSizeBeforeCreate = favouriteArtistRepository.findAll().size();

        // Create the FavouriteArtist

        restFavouriteArtistMockMvc.perform(post("/api/favouriteArtists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(favouriteArtist)))
                .andExpect(status().isCreated());

        // Validate the FavouriteArtist in the database
        List<FavouriteArtist> favouriteArtists = favouriteArtistRepository.findAll();
        assertThat(favouriteArtists).hasSize(databaseSizeBeforeCreate + 1);
        FavouriteArtist testFavouriteArtist = favouriteArtists.get(favouriteArtists.size() - 1);
        assertThat(testFavouriteArtist.getLiked()).isEqualTo(DEFAULT_LIKED);
    }

    @Test
    @Transactional
    public void getAllFavouriteArtists() throws Exception {
        // Initialize the database
        favouriteArtistRepository.saveAndFlush(favouriteArtist);

        // Get all the favouriteArtists
        restFavouriteArtistMockMvc.perform(get("/api/favouriteArtists?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(favouriteArtist.getId().intValue())))
                .andExpect(jsonPath("$.[*].liked").value(hasItem(DEFAULT_LIKED.booleanValue())));
    }

    @Test
    @Transactional
    public void getFavouriteArtist() throws Exception {
        // Initialize the database
        favouriteArtistRepository.saveAndFlush(favouriteArtist);

        // Get the favouriteArtist
        restFavouriteArtistMockMvc.perform(get("/api/favouriteArtists/{id}", favouriteArtist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(favouriteArtist.getId().intValue()))
            .andExpect(jsonPath("$.liked").value(DEFAULT_LIKED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFavouriteArtist() throws Exception {
        // Get the favouriteArtist
        restFavouriteArtistMockMvc.perform(get("/api/favouriteArtists/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFavouriteArtist() throws Exception {
        // Initialize the database
        favouriteArtistRepository.saveAndFlush(favouriteArtist);

		int databaseSizeBeforeUpdate = favouriteArtistRepository.findAll().size();

        // Update the favouriteArtist
        favouriteArtist.setLiked(UPDATED_LIKED);

        restFavouriteArtistMockMvc.perform(put("/api/favouriteArtists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(favouriteArtist)))
                .andExpect(status().isOk());

        // Validate the FavouriteArtist in the database
        List<FavouriteArtist> favouriteArtists = favouriteArtistRepository.findAll();
        assertThat(favouriteArtists).hasSize(databaseSizeBeforeUpdate);
        FavouriteArtist testFavouriteArtist = favouriteArtists.get(favouriteArtists.size() - 1);
        assertThat(testFavouriteArtist.getLiked()).isEqualTo(UPDATED_LIKED);
    }

    @Test
    @Transactional
    public void deleteFavouriteArtist() throws Exception {
        // Initialize the database
        favouriteArtistRepository.saveAndFlush(favouriteArtist);

		int databaseSizeBeforeDelete = favouriteArtistRepository.findAll().size();

        // Get the favouriteArtist
        restFavouriteArtistMockMvc.perform(delete("/api/favouriteArtists/{id}", favouriteArtist.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FavouriteArtist> favouriteArtists = favouriteArtistRepository.findAll();
        assertThat(favouriteArtists).hasSize(databaseSizeBeforeDelete - 1);
    }
}
