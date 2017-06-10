package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Social;
import com.mycompany.myapp.repository.SocialRepository;
import com.mycompany.myapp.repository.search.SocialSearchRepository;

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
 * Test class for the SocialResource REST controller.
 *
 * @see SocialResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SocialResourceIntTest {

    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    private static final Boolean DEFAULT_OFFICIAL = false;
    private static final Boolean UPDATED_OFFICIAL = true;

    private static final Boolean DEFAULT_MERCHANDISING = false;
    private static final Boolean UPDATED_MERCHANDISING = true;

    private static final Boolean DEFAULT_TABS = false;
    private static final Boolean UPDATED_TABS = true;

    @Inject
    private SocialRepository socialRepository;

    @Inject
    private SocialSearchRepository socialSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSocialMockMvc;

    private Social social;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SocialResource socialResource = new SocialResource();
        ReflectionTestUtils.setField(socialResource, "socialSearchRepository", socialSearchRepository);
        ReflectionTestUtils.setField(socialResource, "socialRepository", socialRepository);
        this.restSocialMockMvc = MockMvcBuilders.standaloneSetup(socialResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        social = new Social();
        social.setUrl(DEFAULT_URL);
        social.setOfficial(DEFAULT_OFFICIAL);
        social.setMerchandising(DEFAULT_MERCHANDISING);
        social.setTabs(DEFAULT_TABS);
    }

    @Test
    @Transactional
    public void createSocial() throws Exception {
        int databaseSizeBeforeCreate = socialRepository.findAll().size();

        // Create the Social

        restSocialMockMvc.perform(post("/api/socials")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(social)))
                .andExpect(status().isCreated());

        // Validate the Social in the database
        List<Social> socials = socialRepository.findAll();
        assertThat(socials).hasSize(databaseSizeBeforeCreate + 1);
        Social testSocial = socials.get(socials.size() - 1);
        assertThat(testSocial.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testSocial.getOfficial()).isEqualTo(DEFAULT_OFFICIAL);
        assertThat(testSocial.getMerchandising()).isEqualTo(DEFAULT_MERCHANDISING);
        assertThat(testSocial.getTabs()).isEqualTo(DEFAULT_TABS);
    }

    @Test
    @Transactional
    public void getAllSocials() throws Exception {
        // Initialize the database
        socialRepository.saveAndFlush(social);

        // Get all the socials
        restSocialMockMvc.perform(get("/api/socials?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(social.getId().intValue())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].official").value(hasItem(DEFAULT_OFFICIAL.booleanValue())))
                .andExpect(jsonPath("$.[*].merchandising").value(hasItem(DEFAULT_MERCHANDISING.booleanValue())))
                .andExpect(jsonPath("$.[*].tabs").value(hasItem(DEFAULT_TABS.booleanValue())));
    }

    @Test
    @Transactional
    public void getSocial() throws Exception {
        // Initialize the database
        socialRepository.saveAndFlush(social);

        // Get the social
        restSocialMockMvc.perform(get("/api/socials/{id}", social.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(social.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.official").value(DEFAULT_OFFICIAL.booleanValue()))
            .andExpect(jsonPath("$.merchandising").value(DEFAULT_MERCHANDISING.booleanValue()))
            .andExpect(jsonPath("$.tabs").value(DEFAULT_TABS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSocial() throws Exception {
        // Get the social
        restSocialMockMvc.perform(get("/api/socials/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSocial() throws Exception {
        // Initialize the database
        socialRepository.saveAndFlush(social);

		int databaseSizeBeforeUpdate = socialRepository.findAll().size();

        // Update the social
        social.setUrl(UPDATED_URL);
        social.setOfficial(UPDATED_OFFICIAL);
        social.setMerchandising(UPDATED_MERCHANDISING);
        social.setTabs(UPDATED_TABS);

        restSocialMockMvc.perform(put("/api/socials")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(social)))
                .andExpect(status().isOk());

        // Validate the Social in the database
        List<Social> socials = socialRepository.findAll();
        assertThat(socials).hasSize(databaseSizeBeforeUpdate);
        Social testSocial = socials.get(socials.size() - 1);
        assertThat(testSocial.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testSocial.getOfficial()).isEqualTo(UPDATED_OFFICIAL);
        assertThat(testSocial.getMerchandising()).isEqualTo(UPDATED_MERCHANDISING);
        assertThat(testSocial.getTabs()).isEqualTo(UPDATED_TABS);
    }

    @Test
    @Transactional
    public void deleteSocial() throws Exception {
        // Initialize the database
        socialRepository.saveAndFlush(social);

		int databaseSizeBeforeDelete = socialRepository.findAll().size();

        // Get the social
        restSocialMockMvc.perform(delete("/api/socials/{id}", social.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Social> socials = socialRepository.findAll();
        assertThat(socials).hasSize(databaseSizeBeforeDelete - 1);
    }
}
