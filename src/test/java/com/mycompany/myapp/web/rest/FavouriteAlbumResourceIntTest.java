package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.FavouriteAlbum;
import com.mycompany.myapp.repository.FavouriteAlbumRepository;
import com.mycompany.myapp.repository.search.FavouriteAlbumSearchRepository;

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
 * Test class for the FavouriteAlbumResource REST controller.
 *
 * @see FavouriteAlbumResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FavouriteAlbumResourceIntTest {


    private static final Boolean DEFAULT_LIKED = false;
    private static final Boolean UPDATED_LIKED = true;

    @Inject
    private FavouriteAlbumRepository favouriteAlbumRepository;

    @Inject
    private FavouriteAlbumSearchRepository favouriteAlbumSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFavouriteAlbumMockMvc;

    private FavouriteAlbum favouriteAlbum;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FavouriteAlbumResource favouriteAlbumResource = new FavouriteAlbumResource();
        ReflectionTestUtils.setField(favouriteAlbumResource, "favouriteAlbumSearchRepository", favouriteAlbumSearchRepository);
        ReflectionTestUtils.setField(favouriteAlbumResource, "favouriteAlbumRepository", favouriteAlbumRepository);
        this.restFavouriteAlbumMockMvc = MockMvcBuilders.standaloneSetup(favouriteAlbumResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        favouriteAlbum = new FavouriteAlbum();
        favouriteAlbum.setLiked(DEFAULT_LIKED);
    }

    @Test
    @Transactional
    public void createFavouriteAlbum() throws Exception {
        int databaseSizeBeforeCreate = favouriteAlbumRepository.findAll().size();

        // Create the FavouriteAlbum

        restFavouriteAlbumMockMvc.perform(post("/api/favouriteAlbums")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(favouriteAlbum)))
                .andExpect(status().isCreated());

        // Validate the FavouriteAlbum in the database
        List<FavouriteAlbum> favouriteAlbums = favouriteAlbumRepository.findAll();
        assertThat(favouriteAlbums).hasSize(databaseSizeBeforeCreate + 1);
        FavouriteAlbum testFavouriteAlbum = favouriteAlbums.get(favouriteAlbums.size() - 1);
        assertThat(testFavouriteAlbum.getLiked()).isEqualTo(DEFAULT_LIKED);
    }

    @Test
    @Transactional
    public void getAllFavouriteAlbums() throws Exception {
        // Initialize the database
        favouriteAlbumRepository.saveAndFlush(favouriteAlbum);

        // Get all the favouriteAlbums
        restFavouriteAlbumMockMvc.perform(get("/api/favouriteAlbums?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(favouriteAlbum.getId().intValue())))
                .andExpect(jsonPath("$.[*].liked").value(hasItem(DEFAULT_LIKED.booleanValue())));
    }

    @Test
    @Transactional
    public void getFavouriteAlbum() throws Exception {
        // Initialize the database
        favouriteAlbumRepository.saveAndFlush(favouriteAlbum);

        // Get the favouriteAlbum
        restFavouriteAlbumMockMvc.perform(get("/api/favouriteAlbums/{id}", favouriteAlbum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(favouriteAlbum.getId().intValue()))
            .andExpect(jsonPath("$.liked").value(DEFAULT_LIKED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFavouriteAlbum() throws Exception {
        // Get the favouriteAlbum
        restFavouriteAlbumMockMvc.perform(get("/api/favouriteAlbums/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFavouriteAlbum() throws Exception {
        // Initialize the database
        favouriteAlbumRepository.saveAndFlush(favouriteAlbum);

		int databaseSizeBeforeUpdate = favouriteAlbumRepository.findAll().size();

        // Update the favouriteAlbum
        favouriteAlbum.setLiked(UPDATED_LIKED);

        restFavouriteAlbumMockMvc.perform(put("/api/favouriteAlbums")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(favouriteAlbum)))
                .andExpect(status().isOk());

        // Validate the FavouriteAlbum in the database
        List<FavouriteAlbum> favouriteAlbums = favouriteAlbumRepository.findAll();
        assertThat(favouriteAlbums).hasSize(databaseSizeBeforeUpdate);
        FavouriteAlbum testFavouriteAlbum = favouriteAlbums.get(favouriteAlbums.size() - 1);
        assertThat(testFavouriteAlbum.getLiked()).isEqualTo(UPDATED_LIKED);
    }

    @Test
    @Transactional
    public void deleteFavouriteAlbum() throws Exception {
        // Initialize the database
        favouriteAlbumRepository.saveAndFlush(favouriteAlbum);

		int databaseSizeBeforeDelete = favouriteAlbumRepository.findAll().size();

        // Get the favouriteAlbum
        restFavouriteAlbumMockMvc.perform(delete("/api/favouriteAlbums/{id}", favouriteAlbum.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FavouriteAlbum> favouriteAlbums = favouriteAlbumRepository.findAll();
        assertThat(favouriteAlbums).hasSize(databaseSizeBeforeDelete - 1);
    }
}
