package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Label;
import com.mycompany.myapp.repository.LabelRepository;
import com.mycompany.myapp.repository.search.LabelSearchRepository;

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
 * Test class for the LabelResource REST controller.
 *
 * @see LabelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LabelResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_FOUNDING_DATE = "AAAAA";
    private static final String UPDATED_FOUNDING_DATE = "BBBBB";
    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";
    private static final String DEFAULT_STATUS = "AAAAA";
    private static final String UPDATED_STATUS = "BBBBB";
    private static final String DEFAULT_GENRES = "AAAAA";
    private static final String UPDATED_GENRES = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_ADD_NOTES = "AAAAA";
    private static final String UPDATED_ADD_NOTES = "BBBBB";
    private static final String DEFAULT_LINKS = "AAAAA";
    private static final String UPDATED_LINKS = "BBBBB";
    private static final String DEFAULT_PHONE = "AAAAA";
    private static final String UPDATED_PHONE = "BBBBB";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";
    private static final String DEFAULT_ONLINESHOP = "AAAAA";
    private static final String UPDATED_ONLINESHOP = "BBBBB";
    private static final String DEFAULT_PICTURE = "AAAAA";
    private static final String UPDATED_PICTURE = "BBBBB";

    @Inject
    private LabelRepository labelRepository;

    @Inject
    private LabelSearchRepository labelSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLabelMockMvc;

    private Label label;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LabelResource labelResource = new LabelResource();
        ReflectionTestUtils.setField(labelResource, "labelSearchRepository", labelSearchRepository);
        ReflectionTestUtils.setField(labelResource, "labelRepository", labelRepository);
        this.restLabelMockMvc = MockMvcBuilders.standaloneSetup(labelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        label = new Label();
        label.setName(DEFAULT_NAME);
        label.setFoundingDate(DEFAULT_FOUNDING_DATE);
        label.setLocation(DEFAULT_LOCATION);
        label.setStatus(DEFAULT_STATUS);
        label.setGenres(DEFAULT_GENRES);
        label.setDescription(DEFAULT_DESCRIPTION);
        label.setAddNotes(DEFAULT_ADD_NOTES);
        label.setLinks(DEFAULT_LINKS);
        label.setPhone(DEFAULT_PHONE);
        label.setAddress(DEFAULT_ADDRESS);
        label.setOnlineshop(DEFAULT_ONLINESHOP);
        label.setPicture(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    public void createLabel() throws Exception {
        int databaseSizeBeforeCreate = labelRepository.findAll().size();

        // Create the Label

        restLabelMockMvc.perform(post("/api/labels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(label)))
                .andExpect(status().isCreated());

        // Validate the Label in the database
        List<Label> labels = labelRepository.findAll();
        assertThat(labels).hasSize(databaseSizeBeforeCreate + 1);
        Label testLabel = labels.get(labels.size() - 1);
        assertThat(testLabel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLabel.getFoundingDate()).isEqualTo(DEFAULT_FOUNDING_DATE);
        assertThat(testLabel.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testLabel.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLabel.getGenres()).isEqualTo(DEFAULT_GENRES);
        assertThat(testLabel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLabel.getAddNotes()).isEqualTo(DEFAULT_ADD_NOTES);
        assertThat(testLabel.getLinks()).isEqualTo(DEFAULT_LINKS);
        assertThat(testLabel.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testLabel.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testLabel.getOnlineshop()).isEqualTo(DEFAULT_ONLINESHOP);
        assertThat(testLabel.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = labelRepository.findAll().size();
        // set the field null
        label.setName(null);

        // Create the Label, which fails.

        restLabelMockMvc.perform(post("/api/labels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(label)))
                .andExpect(status().isBadRequest());

        List<Label> labels = labelRepository.findAll();
        assertThat(labels).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLabels() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        // Get all the labels
        restLabelMockMvc.perform(get("/api/labels?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(label.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].foundingDate").value(hasItem(DEFAULT_FOUNDING_DATE.toString())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].genres").value(hasItem(DEFAULT_GENRES.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].addNotes").value(hasItem(DEFAULT_ADD_NOTES.toString())))
                .andExpect(jsonPath("$.[*].links").value(hasItem(DEFAULT_LINKS.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].onlineshop").value(hasItem(DEFAULT_ONLINESHOP.toString())))
                .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE.toString())));
    }

    @Test
    @Transactional
    public void getLabel() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

        // Get the label
        restLabelMockMvc.perform(get("/api/labels/{id}", label.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(label.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.foundingDate").value(DEFAULT_FOUNDING_DATE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.genres").value(DEFAULT_GENRES.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.addNotes").value(DEFAULT_ADD_NOTES.toString()))
            .andExpect(jsonPath("$.links").value(DEFAULT_LINKS.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.onlineshop").value(DEFAULT_ONLINESHOP.toString()))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLabel() throws Exception {
        // Get the label
        restLabelMockMvc.perform(get("/api/labels/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLabel() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

		int databaseSizeBeforeUpdate = labelRepository.findAll().size();

        // Update the label
        label.setName(UPDATED_NAME);
        label.setFoundingDate(UPDATED_FOUNDING_DATE);
        label.setLocation(UPDATED_LOCATION);
        label.setStatus(UPDATED_STATUS);
        label.setGenres(UPDATED_GENRES);
        label.setDescription(UPDATED_DESCRIPTION);
        label.setAddNotes(UPDATED_ADD_NOTES);
        label.setLinks(UPDATED_LINKS);
        label.setPhone(UPDATED_PHONE);
        label.setAddress(UPDATED_ADDRESS);
        label.setOnlineshop(UPDATED_ONLINESHOP);
        label.setPicture(UPDATED_PICTURE);

        restLabelMockMvc.perform(put("/api/labels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(label)))
                .andExpect(status().isOk());

        // Validate the Label in the database
        List<Label> labels = labelRepository.findAll();
        assertThat(labels).hasSize(databaseSizeBeforeUpdate);
        Label testLabel = labels.get(labels.size() - 1);
        assertThat(testLabel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLabel.getFoundingDate()).isEqualTo(UPDATED_FOUNDING_DATE);
        assertThat(testLabel.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testLabel.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLabel.getGenres()).isEqualTo(UPDATED_GENRES);
        assertThat(testLabel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLabel.getAddNotes()).isEqualTo(UPDATED_ADD_NOTES);
        assertThat(testLabel.getLinks()).isEqualTo(UPDATED_LINKS);
        assertThat(testLabel.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testLabel.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testLabel.getOnlineshop()).isEqualTo(UPDATED_ONLINESHOP);
        assertThat(testLabel.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    public void deleteLabel() throws Exception {
        // Initialize the database
        labelRepository.saveAndFlush(label);

		int databaseSizeBeforeDelete = labelRepository.findAll().size();

        // Get the label
        restLabelMockMvc.perform(delete("/api/labels/{id}", label.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Label> labels = labelRepository.findAll();
        assertThat(labels).hasSize(databaseSizeBeforeDelete - 1);
    }
}
