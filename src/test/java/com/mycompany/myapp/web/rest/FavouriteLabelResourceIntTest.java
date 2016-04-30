package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.FavouriteLabel;
import com.mycompany.myapp.repository.FavouriteLabelRepository;
import com.mycompany.myapp.repository.search.FavouriteLabelSearchRepository;

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
 * Test class for the FavouriteLabelResource REST controller.
 *
 * @see FavouriteLabelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FavouriteLabelResourceIntTest {


    private static final Boolean DEFAULT_LIKED = false;
    private static final Boolean UPDATED_LIKED = true;

    @Inject
    private FavouriteLabelRepository favouriteLabelRepository;

    @Inject
    private FavouriteLabelSearchRepository favouriteLabelSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFavouriteLabelMockMvc;

    private FavouriteLabel favouriteLabel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FavouriteLabelResource favouriteLabelResource = new FavouriteLabelResource();
        ReflectionTestUtils.setField(favouriteLabelResource, "favouriteLabelSearchRepository", favouriteLabelSearchRepository);
        ReflectionTestUtils.setField(favouriteLabelResource, "favouriteLabelRepository", favouriteLabelRepository);
        this.restFavouriteLabelMockMvc = MockMvcBuilders.standaloneSetup(favouriteLabelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        favouriteLabel = new FavouriteLabel();
        favouriteLabel.setLiked(DEFAULT_LIKED);
    }

    @Test
    @Transactional
    public void createFavouriteLabel() throws Exception {
        int databaseSizeBeforeCreate = favouriteLabelRepository.findAll().size();

        // Create the FavouriteLabel

        restFavouriteLabelMockMvc.perform(post("/api/favouriteLabels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(favouriteLabel)))
                .andExpect(status().isCreated());

        // Validate the FavouriteLabel in the database
        List<FavouriteLabel> favouriteLabels = favouriteLabelRepository.findAll();
        assertThat(favouriteLabels).hasSize(databaseSizeBeforeCreate + 1);
        FavouriteLabel testFavouriteLabel = favouriteLabels.get(favouriteLabels.size() - 1);
        assertThat(testFavouriteLabel.getLiked()).isEqualTo(DEFAULT_LIKED);
    }

    @Test
    @Transactional
    public void getAllFavouriteLabels() throws Exception {
        // Initialize the database
        favouriteLabelRepository.saveAndFlush(favouriteLabel);

        // Get all the favouriteLabels
        restFavouriteLabelMockMvc.perform(get("/api/favouriteLabels?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(favouriteLabel.getId().intValue())))
                .andExpect(jsonPath("$.[*].liked").value(hasItem(DEFAULT_LIKED.booleanValue())));
    }

    @Test
    @Transactional
    public void getFavouriteLabel() throws Exception {
        // Initialize the database
        favouriteLabelRepository.saveAndFlush(favouriteLabel);

        // Get the favouriteLabel
        restFavouriteLabelMockMvc.perform(get("/api/favouriteLabels/{id}", favouriteLabel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(favouriteLabel.getId().intValue()))
            .andExpect(jsonPath("$.liked").value(DEFAULT_LIKED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFavouriteLabel() throws Exception {
        // Get the favouriteLabel
        restFavouriteLabelMockMvc.perform(get("/api/favouriteLabels/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFavouriteLabel() throws Exception {
        // Initialize the database
        favouriteLabelRepository.saveAndFlush(favouriteLabel);

		int databaseSizeBeforeUpdate = favouriteLabelRepository.findAll().size();

        // Update the favouriteLabel
        favouriteLabel.setLiked(UPDATED_LIKED);

        restFavouriteLabelMockMvc.perform(put("/api/favouriteLabels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(favouriteLabel)))
                .andExpect(status().isOk());

        // Validate the FavouriteLabel in the database
        List<FavouriteLabel> favouriteLabels = favouriteLabelRepository.findAll();
        assertThat(favouriteLabels).hasSize(databaseSizeBeforeUpdate);
        FavouriteLabel testFavouriteLabel = favouriteLabels.get(favouriteLabels.size() - 1);
        assertThat(testFavouriteLabel.getLiked()).isEqualTo(UPDATED_LIKED);
    }

    @Test
    @Transactional
    public void deleteFavouriteLabel() throws Exception {
        // Initialize the database
        favouriteLabelRepository.saveAndFlush(favouriteLabel);

		int databaseSizeBeforeDelete = favouriteLabelRepository.findAll().size();

        // Get the favouriteLabel
        restFavouriteLabelMockMvc.perform(delete("/api/favouriteLabels/{id}", favouriteLabel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FavouriteLabel> favouriteLabels = favouriteLabelRepository.findAll();
        assertThat(favouriteLabels).hasSize(databaseSizeBeforeDelete - 1);
    }
}
