package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.FavouriteBand;
import com.mycompany.myapp.repository.FavouriteBandRepository;
import com.mycompany.myapp.repository.search.FavouriteBandSearchRepository;

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
 * Test class for the FavouriteBandResource REST controller.
 *
 * @see FavouriteBandResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FavouriteBandResourceIntTest {


    private static final Boolean DEFAULT_LIKED = false;
    private static final Boolean UPDATED_LIKED = true;

    @Inject
    private FavouriteBandRepository favouriteBandRepository;

    @Inject
    private FavouriteBandSearchRepository favouriteBandSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFavouriteBandMockMvc;

    private FavouriteBand favouriteBand;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FavouriteBandResource favouriteBandResource = new FavouriteBandResource();
        ReflectionTestUtils.setField(favouriteBandResource, "favouriteBandSearchRepository", favouriteBandSearchRepository);
        ReflectionTestUtils.setField(favouriteBandResource, "favouriteBandRepository", favouriteBandRepository);
        this.restFavouriteBandMockMvc = MockMvcBuilders.standaloneSetup(favouriteBandResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        favouriteBand = new FavouriteBand();
        favouriteBand.setLiked(DEFAULT_LIKED);
    }

    @Test
    @Transactional
    public void createFavouriteBand() throws Exception {
        int databaseSizeBeforeCreate = favouriteBandRepository.findAll().size();

        // Create the FavouriteBand

        restFavouriteBandMockMvc.perform(post("/api/favouriteBands")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(favouriteBand)))
                .andExpect(status().isCreated());

        // Validate the FavouriteBand in the database
        List<FavouriteBand> favouriteBands = favouriteBandRepository.findAll();
        assertThat(favouriteBands).hasSize(databaseSizeBeforeCreate + 1);
        FavouriteBand testFavouriteBand = favouriteBands.get(favouriteBands.size() - 1);
        assertThat(testFavouriteBand.getLiked()).isEqualTo(DEFAULT_LIKED);
    }

    @Test
    @Transactional
    public void getAllFavouriteBands() throws Exception {
        // Initialize the database
        favouriteBandRepository.saveAndFlush(favouriteBand);

        // Get all the favouriteBands
        restFavouriteBandMockMvc.perform(get("/api/favouriteBands?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(favouriteBand.getId().intValue())))
                .andExpect(jsonPath("$.[*].liked").value(hasItem(DEFAULT_LIKED.booleanValue())));
    }

    @Test
    @Transactional
    public void getFavouriteBand() throws Exception {
        // Initialize the database
        favouriteBandRepository.saveAndFlush(favouriteBand);

        // Get the favouriteBand
        restFavouriteBandMockMvc.perform(get("/api/favouriteBands/{id}", favouriteBand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(favouriteBand.getId().intValue()))
            .andExpect(jsonPath("$.liked").value(DEFAULT_LIKED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFavouriteBand() throws Exception {
        // Get the favouriteBand
        restFavouriteBandMockMvc.perform(get("/api/favouriteBands/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFavouriteBand() throws Exception {
        // Initialize the database
        favouriteBandRepository.saveAndFlush(favouriteBand);

		int databaseSizeBeforeUpdate = favouriteBandRepository.findAll().size();

        // Update the favouriteBand
        favouriteBand.setLiked(UPDATED_LIKED);

        restFavouriteBandMockMvc.perform(put("/api/favouriteBands")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(favouriteBand)))
                .andExpect(status().isOk());

        // Validate the FavouriteBand in the database
        List<FavouriteBand> favouriteBands = favouriteBandRepository.findAll();
        assertThat(favouriteBands).hasSize(databaseSizeBeforeUpdate);
        FavouriteBand testFavouriteBand = favouriteBands.get(favouriteBands.size() - 1);
        assertThat(testFavouriteBand.getLiked()).isEqualTo(UPDATED_LIKED);
    }

    @Test
    @Transactional
    public void deleteFavouriteBand() throws Exception {
        // Initialize the database
        favouriteBandRepository.saveAndFlush(favouriteBand);

		int databaseSizeBeforeDelete = favouriteBandRepository.findAll().size();

        // Get the favouriteBand
        restFavouriteBandMockMvc.perform(delete("/api/favouriteBands/{id}", favouriteBand.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FavouriteBand> favouriteBands = favouriteBandRepository.findAll();
        assertThat(favouriteBands).hasSize(databaseSizeBeforeDelete - 1);
    }
}
