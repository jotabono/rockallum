package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Collection;
import com.mycompany.myapp.repository.CollectionRepository;
import com.mycompany.myapp.repository.search.CollectionSearchRepository;

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
 * Test class for the CollectionResource REST controller.
 *
 * @see CollectionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CollectionResourceIntTest {


    private static final Boolean DEFAULT_CATCHED = false;
    private static final Boolean UPDATED_CATCHED = true;

    @Inject
    private CollectionRepository collectionRepository;

    @Inject
    private CollectionSearchRepository collectionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCollectionMockMvc;

    private Collection collection;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CollectionResource collectionResource = new CollectionResource();
        ReflectionTestUtils.setField(collectionResource, "collectionSearchRepository", collectionSearchRepository);
        ReflectionTestUtils.setField(collectionResource, "collectionRepository", collectionRepository);
        this.restCollectionMockMvc = MockMvcBuilders.standaloneSetup(collectionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        collection = new Collection();
        collection.setCatched(DEFAULT_CATCHED);
    }

    @Test
    @Transactional
    public void createCollection() throws Exception {
        int databaseSizeBeforeCreate = collectionRepository.findAll().size();

        // Create the Collection

        restCollectionMockMvc.perform(post("/api/collections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(collection)))
                .andExpect(status().isCreated());

        // Validate the Collection in the database
        List<Collection> collections = collectionRepository.findAll();
        assertThat(collections).hasSize(databaseSizeBeforeCreate + 1);
        Collection testCollection = collections.get(collections.size() - 1);
        assertThat(testCollection.getCatched()).isEqualTo(DEFAULT_CATCHED);
    }

    @Test
    @Transactional
    public void getAllCollections() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collections
        restCollectionMockMvc.perform(get("/api/collections?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(collection.getId().intValue())))
                .andExpect(jsonPath("$.[*].catched").value(hasItem(DEFAULT_CATCHED.booleanValue())));
    }

    @Test
    @Transactional
    public void getCollection() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get the collection
        restCollectionMockMvc.perform(get("/api/collections/{id}", collection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(collection.getId().intValue()))
            .andExpect(jsonPath("$.catched").value(DEFAULT_CATCHED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCollection() throws Exception {
        // Get the collection
        restCollectionMockMvc.perform(get("/api/collections/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCollection() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

		int databaseSizeBeforeUpdate = collectionRepository.findAll().size();

        // Update the collection
        collection.setCatched(UPDATED_CATCHED);

        restCollectionMockMvc.perform(put("/api/collections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(collection)))
                .andExpect(status().isOk());

        // Validate the Collection in the database
        List<Collection> collections = collectionRepository.findAll();
        assertThat(collections).hasSize(databaseSizeBeforeUpdate);
        Collection testCollection = collections.get(collections.size() - 1);
        assertThat(testCollection.getCatched()).isEqualTo(UPDATED_CATCHED);
    }

    @Test
    @Transactional
    public void deleteCollection() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

		int databaseSizeBeforeDelete = collectionRepository.findAll().size();

        // Get the collection
        restCollectionMockMvc.perform(delete("/api/collections/{id}", collection.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Collection> collections = collectionRepository.findAll();
        assertThat(collections).hasSize(databaseSizeBeforeDelete - 1);
    }
}
