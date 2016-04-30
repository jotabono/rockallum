package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Review;
import com.mycompany.myapp.repository.ReviewRepository;
import com.mycompany.myapp.repository.search.ReviewSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ReviewResource REST controller.
 *
 * @see ReviewResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ReviewResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final Integer DEFAULT_MARK = 1;
    private static final Integer UPDATED_MARK = 2;

    private static final ZonedDateTime DEFAULT_REVIEW_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_REVIEW_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_REVIEW_DATE_STR = dateTimeFormatter.format(DEFAULT_REVIEW_DATE);
    private static final String DEFAULT_REVIEW_TEXT = "AAAAA";
    private static final String UPDATED_REVIEW_TEXT = "BBBBB";

    @Inject
    private ReviewRepository reviewRepository;

    @Inject
    private ReviewSearchRepository reviewSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restReviewMockMvc;

    private Review review;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReviewResource reviewResource = new ReviewResource();
        ReflectionTestUtils.setField(reviewResource, "reviewSearchRepository", reviewSearchRepository);
        ReflectionTestUtils.setField(reviewResource, "reviewRepository", reviewRepository);
        this.restReviewMockMvc = MockMvcBuilders.standaloneSetup(reviewResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        review = new Review();
        review.setTitle(DEFAULT_TITLE);
        review.setMark(DEFAULT_MARK);
        review.setReviewDate(DEFAULT_REVIEW_DATE);
        review.setReviewText(DEFAULT_REVIEW_TEXT);
    }

    @Test
    @Transactional
    public void createReview() throws Exception {
        int databaseSizeBeforeCreate = reviewRepository.findAll().size();

        // Create the Review

        restReviewMockMvc.perform(post("/api/reviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(review)))
                .andExpect(status().isCreated());

        // Validate the Review in the database
        List<Review> reviews = reviewRepository.findAll();
        assertThat(reviews).hasSize(databaseSizeBeforeCreate + 1);
        Review testReview = reviews.get(reviews.size() - 1);
        assertThat(testReview.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testReview.getMark()).isEqualTo(DEFAULT_MARK);
        assertThat(testReview.getReviewDate()).isEqualTo(DEFAULT_REVIEW_DATE);
        assertThat(testReview.getReviewText()).isEqualTo(DEFAULT_REVIEW_TEXT);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewRepository.findAll().size();
        // set the field null
        review.setTitle(null);

        // Create the Review, which fails.

        restReviewMockMvc.perform(post("/api/reviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(review)))
                .andExpect(status().isBadRequest());

        List<Review> reviews = reviewRepository.findAll();
        assertThat(reviews).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMarkIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewRepository.findAll().size();
        // set the field null
        review.setMark(null);

        // Create the Review, which fails.

        restReviewMockMvc.perform(post("/api/reviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(review)))
                .andExpect(status().isBadRequest());

        List<Review> reviews = reviewRepository.findAll();
        assertThat(reviews).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReviewDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewRepository.findAll().size();
        // set the field null
        review.setReviewDate(null);

        // Create the Review, which fails.

        restReviewMockMvc.perform(post("/api/reviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(review)))
                .andExpect(status().isBadRequest());

        List<Review> reviews = reviewRepository.findAll();
        assertThat(reviews).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReviewTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewRepository.findAll().size();
        // set the field null
        review.setReviewText(null);

        // Create the Review, which fails.

        restReviewMockMvc.perform(post("/api/reviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(review)))
                .andExpect(status().isBadRequest());

        List<Review> reviews = reviewRepository.findAll();
        assertThat(reviews).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReviews() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviews
        restReviewMockMvc.perform(get("/api/reviews?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].mark").value(hasItem(DEFAULT_MARK)))
                .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE_STR)))
                .andExpect(jsonPath("$.[*].reviewText").value(hasItem(DEFAULT_REVIEW_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get the review
        restReviewMockMvc.perform(get("/api/reviews/{id}", review.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(review.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.mark").value(DEFAULT_MARK))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE_STR))
            .andExpect(jsonPath("$.reviewText").value(DEFAULT_REVIEW_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReview() throws Exception {
        // Get the review
        restReviewMockMvc.perform(get("/api/reviews/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

		int databaseSizeBeforeUpdate = reviewRepository.findAll().size();

        // Update the review
        review.setTitle(UPDATED_TITLE);
        review.setMark(UPDATED_MARK);
        review.setReviewDate(UPDATED_REVIEW_DATE);
        review.setReviewText(UPDATED_REVIEW_TEXT);

        restReviewMockMvc.perform(put("/api/reviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(review)))
                .andExpect(status().isOk());

        // Validate the Review in the database
        List<Review> reviews = reviewRepository.findAll();
        assertThat(reviews).hasSize(databaseSizeBeforeUpdate);
        Review testReview = reviews.get(reviews.size() - 1);
        assertThat(testReview.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testReview.getMark()).isEqualTo(UPDATED_MARK);
        assertThat(testReview.getReviewDate()).isEqualTo(UPDATED_REVIEW_DATE);
        assertThat(testReview.getReviewText()).isEqualTo(UPDATED_REVIEW_TEXT);
    }

    @Test
    @Transactional
    public void deleteReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

		int databaseSizeBeforeDelete = reviewRepository.findAll().size();

        // Get the review
        restReviewMockMvc.perform(delete("/api/reviews/{id}", review.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Review> reviews = reviewRepository.findAll();
        assertThat(reviews).hasSize(databaseSizeBeforeDelete - 1);
    }
}
