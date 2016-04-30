package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Sex;
import com.mycompany.myapp.repository.SexRepository;
import com.mycompany.myapp.repository.search.SexSearchRepository;

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
 * Test class for the SexResource REST controller.
 *
 * @see SexResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SexResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private SexRepository sexRepository;

    @Inject
    private SexSearchRepository sexSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSexMockMvc;

    private Sex sex;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SexResource sexResource = new SexResource();
        ReflectionTestUtils.setField(sexResource, "sexSearchRepository", sexSearchRepository);
        ReflectionTestUtils.setField(sexResource, "sexRepository", sexRepository);
        this.restSexMockMvc = MockMvcBuilders.standaloneSetup(sexResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        sex = new Sex();
        sex.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSex() throws Exception {
        int databaseSizeBeforeCreate = sexRepository.findAll().size();

        // Create the Sex

        restSexMockMvc.perform(post("/api/sexs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sex)))
                .andExpect(status().isCreated());

        // Validate the Sex in the database
        List<Sex> sexs = sexRepository.findAll();
        assertThat(sexs).hasSize(databaseSizeBeforeCreate + 1);
        Sex testSex = sexs.get(sexs.size() - 1);
        assertThat(testSex.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllSexs() throws Exception {
        // Initialize the database
        sexRepository.saveAndFlush(sex);

        // Get all the sexs
        restSexMockMvc.perform(get("/api/sexs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sex.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getSex() throws Exception {
        // Initialize the database
        sexRepository.saveAndFlush(sex);

        // Get the sex
        restSexMockMvc.perform(get("/api/sexs/{id}", sex.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sex.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSex() throws Exception {
        // Get the sex
        restSexMockMvc.perform(get("/api/sexs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSex() throws Exception {
        // Initialize the database
        sexRepository.saveAndFlush(sex);

		int databaseSizeBeforeUpdate = sexRepository.findAll().size();

        // Update the sex
        sex.setName(UPDATED_NAME);

        restSexMockMvc.perform(put("/api/sexs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sex)))
                .andExpect(status().isOk());

        // Validate the Sex in the database
        List<Sex> sexs = sexRepository.findAll();
        assertThat(sexs).hasSize(databaseSizeBeforeUpdate);
        Sex testSex = sexs.get(sexs.size() - 1);
        assertThat(testSex.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteSex() throws Exception {
        // Initialize the database
        sexRepository.saveAndFlush(sex);

		int databaseSizeBeforeDelete = sexRepository.findAll().size();

        // Get the sex
        restSexMockMvc.perform(delete("/api/sexs/{id}", sex.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Sex> sexs = sexRepository.findAll();
        assertThat(sexs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
