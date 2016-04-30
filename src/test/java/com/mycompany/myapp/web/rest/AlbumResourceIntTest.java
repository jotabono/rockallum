package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Album;
import com.mycompany.myapp.repository.AlbumRepository;
import com.mycompany.myapp.repository.search.AlbumSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AlbumResource REST controller.
 *
 * @see AlbumResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AlbumResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final LocalDate DEFAULT_RELEASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RELEASE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_CATALOG_ID = "AAAAA";
    private static final String UPDATED_CATALOG_ID = "BBBBB";
    private static final String DEFAULT_NUM_COPIES = "AAAAA";
    private static final String UPDATED_NUM_COPIES = "BBBBB";
    private static final String DEFAULT_FORMAT = "AAAAA";
    private static final String UPDATED_FORMAT = "BBBBB";
    private static final String DEFAULT_ADD_NOTES = "AAAAA";
    private static final String UPDATED_ADD_NOTES = "BBBBB";
    private static final String DEFAULT_REC_INFO = "AAAAA";
    private static final String UPDATED_REC_INFO = "BBBBB";

    private static final Boolean DEFAULT_INDEPENDENT = false;
    private static final Boolean UPDATED_INDEPENDENT = true;
    private static final String DEFAULT_PICTURE = "AAAAA";
    private static final String UPDATED_PICTURE = "BBBBB";

    @Inject
    private AlbumRepository albumRepository;

    @Inject
    private AlbumSearchRepository albumSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAlbumMockMvc;

    private Album album;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AlbumResource albumResource = new AlbumResource();
        ReflectionTestUtils.setField(albumResource, "albumSearchRepository", albumSearchRepository);
        ReflectionTestUtils.setField(albumResource, "albumRepository", albumRepository);
        this.restAlbumMockMvc = MockMvcBuilders.standaloneSetup(albumResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        album = new Album();
        album.setTitle(DEFAULT_TITLE);
        album.setReleaseDate(DEFAULT_RELEASE_DATE);
        album.setCatalogID(DEFAULT_CATALOG_ID);
        album.setNumCopies(DEFAULT_NUM_COPIES);
        album.setFormat(DEFAULT_FORMAT);
        album.setAddNotes(DEFAULT_ADD_NOTES);
        album.setRecInfo(DEFAULT_REC_INFO);
        album.setIndependent(DEFAULT_INDEPENDENT);
        album.setPicture(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    public void createAlbum() throws Exception {
        int databaseSizeBeforeCreate = albumRepository.findAll().size();

        // Create the Album

        restAlbumMockMvc.perform(post("/api/albums")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(album)))
                .andExpect(status().isCreated());

        // Validate the Album in the database
        List<Album> albums = albumRepository.findAll();
        assertThat(albums).hasSize(databaseSizeBeforeCreate + 1);
        Album testAlbum = albums.get(albums.size() - 1);
        assertThat(testAlbum.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAlbum.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testAlbum.getCatalogID()).isEqualTo(DEFAULT_CATALOG_ID);
        assertThat(testAlbum.getNumCopies()).isEqualTo(DEFAULT_NUM_COPIES);
        assertThat(testAlbum.getFormat()).isEqualTo(DEFAULT_FORMAT);
        assertThat(testAlbum.getAddNotes()).isEqualTo(DEFAULT_ADD_NOTES);
        assertThat(testAlbum.getRecInfo()).isEqualTo(DEFAULT_REC_INFO);
        assertThat(testAlbum.getIndependent()).isEqualTo(DEFAULT_INDEPENDENT);
        assertThat(testAlbum.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = albumRepository.findAll().size();
        // set the field null
        album.setTitle(null);

        // Create the Album, which fails.

        restAlbumMockMvc.perform(post("/api/albums")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(album)))
                .andExpect(status().isBadRequest());

        List<Album> albums = albumRepository.findAll();
        assertThat(albums).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReleaseDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = albumRepository.findAll().size();
        // set the field null
        album.setReleaseDate(null);

        // Create the Album, which fails.

        restAlbumMockMvc.perform(post("/api/albums")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(album)))
                .andExpect(status().isBadRequest());

        List<Album> albums = albumRepository.findAll();
        assertThat(albums).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAlbums() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albums
        restAlbumMockMvc.perform(get("/api/albums?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(album.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
                .andExpect(jsonPath("$.[*].catalogID").value(hasItem(DEFAULT_CATALOG_ID.toString())))
                .andExpect(jsonPath("$.[*].numCopies").value(hasItem(DEFAULT_NUM_COPIES.toString())))
                .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())))
                .andExpect(jsonPath("$.[*].addNotes").value(hasItem(DEFAULT_ADD_NOTES.toString())))
                .andExpect(jsonPath("$.[*].recInfo").value(hasItem(DEFAULT_REC_INFO.toString())))
                .andExpect(jsonPath("$.[*].independent").value(hasItem(DEFAULT_INDEPENDENT.booleanValue())))
                .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE.toString())));
    }

    @Test
    @Transactional
    public void getAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get the album
        restAlbumMockMvc.perform(get("/api/albums/{id}", album.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(album.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.catalogID").value(DEFAULT_CATALOG_ID.toString()))
            .andExpect(jsonPath("$.numCopies").value(DEFAULT_NUM_COPIES.toString()))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT.toString()))
            .andExpect(jsonPath("$.addNotes").value(DEFAULT_ADD_NOTES.toString()))
            .andExpect(jsonPath("$.recInfo").value(DEFAULT_REC_INFO.toString()))
            .andExpect(jsonPath("$.independent").value(DEFAULT_INDEPENDENT.booleanValue()))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAlbum() throws Exception {
        // Get the album
        restAlbumMockMvc.perform(get("/api/albums/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

		int databaseSizeBeforeUpdate = albumRepository.findAll().size();

        // Update the album
        album.setTitle(UPDATED_TITLE);
        album.setReleaseDate(UPDATED_RELEASE_DATE);
        album.setCatalogID(UPDATED_CATALOG_ID);
        album.setNumCopies(UPDATED_NUM_COPIES);
        album.setFormat(UPDATED_FORMAT);
        album.setAddNotes(UPDATED_ADD_NOTES);
        album.setRecInfo(UPDATED_REC_INFO);
        album.setIndependent(UPDATED_INDEPENDENT);
        album.setPicture(UPDATED_PICTURE);

        restAlbumMockMvc.perform(put("/api/albums")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(album)))
                .andExpect(status().isOk());

        // Validate the Album in the database
        List<Album> albums = albumRepository.findAll();
        assertThat(albums).hasSize(databaseSizeBeforeUpdate);
        Album testAlbum = albums.get(albums.size() - 1);
        assertThat(testAlbum.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAlbum.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testAlbum.getCatalogID()).isEqualTo(UPDATED_CATALOG_ID);
        assertThat(testAlbum.getNumCopies()).isEqualTo(UPDATED_NUM_COPIES);
        assertThat(testAlbum.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testAlbum.getAddNotes()).isEqualTo(UPDATED_ADD_NOTES);
        assertThat(testAlbum.getRecInfo()).isEqualTo(UPDATED_REC_INFO);
        assertThat(testAlbum.getIndependent()).isEqualTo(UPDATED_INDEPENDENT);
        assertThat(testAlbum.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    public void deleteAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

		int databaseSizeBeforeDelete = albumRepository.findAll().size();

        // Get the album
        restAlbumMockMvc.perform(delete("/api/albums/{id}", album.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Album> albums = albumRepository.findAll();
        assertThat(albums).hasSize(databaseSizeBeforeDelete - 1);
    }
}
