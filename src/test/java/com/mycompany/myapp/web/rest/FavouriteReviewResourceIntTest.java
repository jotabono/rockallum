package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.FavouriteReview;
import com.mycompany.myapp.repository.FavouriteReviewRepository;
import com.mycompany.myapp.repository.search.FavouriteReviewSearchRepository;

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
 * Test class for the FavouriteReviewResource REST controller.
 *
 * @see FavouriteReviewResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FavouriteReviewResourceIntTest {


    private static final Boolean DEFAULT_LIKED = false;
    private static final Boolean UPDATED_LIKED = true;

    @Inject
    private FavouriteReviewRepository favouriteReviewRepository;

    @Inject
    private FavouriteReviewSearchRepository favouriteReviewSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFavouriteReviewMockMvc;

    private FavouriteReview favouriteReview;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FavouriteReviewResource favouriteReviewResource = new FavouriteReviewResource();
        ReflectionTestUtils.setField(favouriteReviewResource, "favouriteReviewSearchRepository", favouriteReviewSearchRepository);
        ReflectionTestUtils.setField(favouriteReviewResource, "favouriteReviewRepository", favouriteReviewRepository);
        this.restFavouriteReviewMockMvc = MockMvcBuilders.standaloneSetup(favouriteReviewResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        favouriteReview = new FavouriteReview();
        favouriteReview.setLiked(DEFAULT_LIKED);
    }

    @Test
    @Transactional
    public void createFavouriteReview() throws Exception {
        int databaseSizeBeforeCreate = favouriteReviewRepository.findAll().size();

        // Create the FavouriteReview

        restFavouriteReviewMockMvc.perform(post("/api/favouriteReviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(favouriteReview)))
                .andExpect(status().isCreated());

        // Validate the FavouriteReview in the database
        List<FavouriteReview> favouriteReviews = favouriteReviewRepository.findAll();
        assertThat(favouriteReviews).hasSize(databaseSizeBeforeCreate + 1);
        FavouriteReview testFavouriteReview = favouriteReviews.get(favouriteReviews.size() - 1);
        assertThat(testFavouriteReview.getLiked()).isEqualTo(DEFAULT_LIKED);
    }

    @Test
    @Transactional
    public void getAllFavouriteReviews() throws Exception {
        // Initialize the database
        favouriteReviewRepository.saveAndFlush(favouriteReview);

        // Get all the favouriteReviews
        restFavouriteReviewMockMvc.perform(get("/api/favouriteReviews?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(favouriteReview.getId().intValue())))
                .andExpect(jsonPath("$.[*].liked").value(hasItem(DEFAULT_LIKED.booleanValue())));
    }

    @Test
    @Transactional
    public void getFavouriteReview() throws Exception {
        // Initialize the database
        favouriteReviewRepository.saveAndFlush(favouriteReview);

        // Get the favouriteReview
        restFavouriteReviewMockMvc.perform(get("/api/favouriteReviews/{id}", favouriteReview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(favouriteReview.getId().intValue()))
            .andExpect(jsonPath("$.liked").value(DEFAULT_LIKED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFavouriteReview() throws Exception {
        // Get the favouriteReview
        restFavouriteReviewMockMvc.perform(get("/api/favouriteReviews/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFavouriteReview() throws Exception {
        // Initialize the database
        favouriteReviewRepository.saveAndFlush(favouriteReview);

		int databaseSizeBeforeUpdate = favouriteReviewRepository.findAll().size();

        // Update the favouriteReview
        favouriteReview.setLiked(UPDATED_LIKED);

        restFavouriteReviewMockMvc.perform(put("/api/favouriteReviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(favouriteReview)))
                .andExpect(status().isOk());

        // Validate the FavouriteReview in the database
        List<FavouriteReview> favouriteReviews = favouriteReviewRepository.findAll();
        assertThat(favouriteReviews).hasSize(databaseSizeBeforeUpdate);
        FavouriteReview testFavouriteReview = favouriteReviews.get(favouriteReviews.size() - 1);
        assertThat(testFavouriteReview.getLiked()).isEqualTo(UPDATED_LIKED);
    }

    @Test
    @Transactional
    public void deleteFavouriteReview() throws Exception {
        // Initialize the database
        favouriteReviewRepository.saveAndFlush(favouriteReview);

		int databaseSizeBeforeDelete = favouriteReviewRepository.findAll().size();

        // Get the favouriteReview
        restFavouriteReviewMockMvc.perform(delete("/api/favouriteReviews/{id}", favouriteReview.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FavouriteReview> favouriteReviews = favouriteReviewRepository.findAll();
        assertThat(favouriteReviews).hasSize(databaseSizeBeforeDelete - 1);
    }
}
