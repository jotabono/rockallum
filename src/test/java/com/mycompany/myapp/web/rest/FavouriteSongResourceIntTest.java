package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.FavouriteSong;
import com.mycompany.myapp.repository.FavouriteSongRepository;
import com.mycompany.myapp.repository.search.FavouriteSongSearchRepository;

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
 * Test class for the FavouriteSongResource REST controller.
 *
 * @see FavouriteSongResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FavouriteSongResourceIntTest {


    private static final Boolean DEFAULT_LIKED = false;
    private static final Boolean UPDATED_LIKED = true;

    @Inject
    private FavouriteSongRepository favouriteSongRepository;

    @Inject
    private FavouriteSongSearchRepository favouriteSongSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFavouriteSongMockMvc;

    private FavouriteSong favouriteSong;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FavouriteSongResource favouriteSongResource = new FavouriteSongResource();
        ReflectionTestUtils.setField(favouriteSongResource, "favouriteSongSearchRepository", favouriteSongSearchRepository);
        ReflectionTestUtils.setField(favouriteSongResource, "favouriteSongRepository", favouriteSongRepository);
        this.restFavouriteSongMockMvc = MockMvcBuilders.standaloneSetup(favouriteSongResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        favouriteSong = new FavouriteSong();
        favouriteSong.setLiked(DEFAULT_LIKED);
    }

    @Test
    @Transactional
    public void createFavouriteSong() throws Exception {
        int databaseSizeBeforeCreate = favouriteSongRepository.findAll().size();

        // Create the FavouriteSong

        restFavouriteSongMockMvc.perform(post("/api/favouriteSongs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(favouriteSong)))
                .andExpect(status().isCreated());

        // Validate the FavouriteSong in the database
        List<FavouriteSong> favouriteSongs = favouriteSongRepository.findAll();
        assertThat(favouriteSongs).hasSize(databaseSizeBeforeCreate + 1);
        FavouriteSong testFavouriteSong = favouriteSongs.get(favouriteSongs.size() - 1);
        assertThat(testFavouriteSong.getLiked()).isEqualTo(DEFAULT_LIKED);
    }

    @Test
    @Transactional
    public void getAllFavouriteSongs() throws Exception {
        // Initialize the database
        favouriteSongRepository.saveAndFlush(favouriteSong);

        // Get all the favouriteSongs
        restFavouriteSongMockMvc.perform(get("/api/favouriteSongs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(favouriteSong.getId().intValue())))
                .andExpect(jsonPath("$.[*].liked").value(hasItem(DEFAULT_LIKED.booleanValue())));
    }

    @Test
    @Transactional
    public void getFavouriteSong() throws Exception {
        // Initialize the database
        favouriteSongRepository.saveAndFlush(favouriteSong);

        // Get the favouriteSong
        restFavouriteSongMockMvc.perform(get("/api/favouriteSongs/{id}", favouriteSong.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(favouriteSong.getId().intValue()))
            .andExpect(jsonPath("$.liked").value(DEFAULT_LIKED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFavouriteSong() throws Exception {
        // Get the favouriteSong
        restFavouriteSongMockMvc.perform(get("/api/favouriteSongs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFavouriteSong() throws Exception {
        // Initialize the database
        favouriteSongRepository.saveAndFlush(favouriteSong);

		int databaseSizeBeforeUpdate = favouriteSongRepository.findAll().size();

        // Update the favouriteSong
        favouriteSong.setLiked(UPDATED_LIKED);

        restFavouriteSongMockMvc.perform(put("/api/favouriteSongs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(favouriteSong)))
                .andExpect(status().isOk());

        // Validate the FavouriteSong in the database
        List<FavouriteSong> favouriteSongs = favouriteSongRepository.findAll();
        assertThat(favouriteSongs).hasSize(databaseSizeBeforeUpdate);
        FavouriteSong testFavouriteSong = favouriteSongs.get(favouriteSongs.size() - 1);
        assertThat(testFavouriteSong.getLiked()).isEqualTo(UPDATED_LIKED);
    }

    @Test
    @Transactional
    public void deleteFavouriteSong() throws Exception {
        // Initialize the database
        favouriteSongRepository.saveAndFlush(favouriteSong);

		int databaseSizeBeforeDelete = favouriteSongRepository.findAll().size();

        // Get the favouriteSong
        restFavouriteSongMockMvc.perform(delete("/api/favouriteSongs/{id}", favouriteSong.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FavouriteSong> favouriteSongs = favouriteSongRepository.findAll();
        assertThat(favouriteSongs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
