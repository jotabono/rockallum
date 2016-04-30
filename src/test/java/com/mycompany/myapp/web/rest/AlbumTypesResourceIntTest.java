package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.AlbumTypes;
import com.mycompany.myapp.repository.AlbumTypesRepository;
import com.mycompany.myapp.repository.search.AlbumTypesSearchRepository;

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
 * Test class for the AlbumTypesResource REST controller.
 *
 * @see AlbumTypesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AlbumTypesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private AlbumTypesRepository albumTypesRepository;

    @Inject
    private AlbumTypesSearchRepository albumTypesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAlbumTypesMockMvc;

    private AlbumTypes albumTypes;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AlbumTypesResource albumTypesResource = new AlbumTypesResource();
        ReflectionTestUtils.setField(albumTypesResource, "albumTypesSearchRepository", albumTypesSearchRepository);
        ReflectionTestUtils.setField(albumTypesResource, "albumTypesRepository", albumTypesRepository);
        this.restAlbumTypesMockMvc = MockMvcBuilders.standaloneSetup(albumTypesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        albumTypes = new AlbumTypes();
        albumTypes.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createAlbumTypes() throws Exception {
        int databaseSizeBeforeCreate = albumTypesRepository.findAll().size();

        // Create the AlbumTypes

        restAlbumTypesMockMvc.perform(post("/api/albumTypess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(albumTypes)))
                .andExpect(status().isCreated());

        // Validate the AlbumTypes in the database
        List<AlbumTypes> albumTypess = albumTypesRepository.findAll();
        assertThat(albumTypess).hasSize(databaseSizeBeforeCreate + 1);
        AlbumTypes testAlbumTypes = albumTypess.get(albumTypess.size() - 1);
        assertThat(testAlbumTypes.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = albumTypesRepository.findAll().size();
        // set the field null
        albumTypes.setName(null);

        // Create the AlbumTypes, which fails.

        restAlbumTypesMockMvc.perform(post("/api/albumTypess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(albumTypes)))
                .andExpect(status().isBadRequest());

        List<AlbumTypes> albumTypess = albumTypesRepository.findAll();
        assertThat(albumTypess).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAlbumTypess() throws Exception {
        // Initialize the database
        albumTypesRepository.saveAndFlush(albumTypes);

        // Get all the albumTypess
        restAlbumTypesMockMvc.perform(get("/api/albumTypess?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(albumTypes.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAlbumTypes() throws Exception {
        // Initialize the database
        albumTypesRepository.saveAndFlush(albumTypes);

        // Get the albumTypes
        restAlbumTypesMockMvc.perform(get("/api/albumTypess/{id}", albumTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(albumTypes.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAlbumTypes() throws Exception {
        // Get the albumTypes
        restAlbumTypesMockMvc.perform(get("/api/albumTypess/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlbumTypes() throws Exception {
        // Initialize the database
        albumTypesRepository.saveAndFlush(albumTypes);

		int databaseSizeBeforeUpdate = albumTypesRepository.findAll().size();

        // Update the albumTypes
        albumTypes.setName(UPDATED_NAME);

        restAlbumTypesMockMvc.perform(put("/api/albumTypess")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(albumTypes)))
                .andExpect(status().isOk());

        // Validate the AlbumTypes in the database
        List<AlbumTypes> albumTypess = albumTypesRepository.findAll();
        assertThat(albumTypess).hasSize(databaseSizeBeforeUpdate);
        AlbumTypes testAlbumTypes = albumTypess.get(albumTypess.size() - 1);
        assertThat(testAlbumTypes.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteAlbumTypes() throws Exception {
        // Initialize the database
        albumTypesRepository.saveAndFlush(albumTypes);

		int databaseSizeBeforeDelete = albumTypesRepository.findAll().size();

        // Get the albumTypes
        restAlbumTypesMockMvc.perform(delete("/api/albumTypess/{id}", albumTypes.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AlbumTypes> albumTypess = albumTypesRepository.findAll();
        assertThat(albumTypess).hasSize(databaseSizeBeforeDelete - 1);
    }
}
