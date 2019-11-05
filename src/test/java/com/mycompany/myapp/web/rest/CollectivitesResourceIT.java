package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RefsApp;
import com.mycompany.myapp.domain.Collectivites;
import com.mycompany.myapp.repository.CollectivitesRepository;
import com.mycompany.myapp.service.CollectivitesService;
import com.mycompany.myapp.service.dto.CollectivitesDTO;
import com.mycompany.myapp.service.mapper.CollectivitesMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CollectivitesResource} REST controller.
 */
@SpringBootTest(classes = RefsApp.class)
public class CollectivitesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CollectivitesRepository collectivitesRepository;

    @Autowired
    private CollectivitesMapper collectivitesMapper;

    @Autowired
    private CollectivitesService collectivitesService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCollectivitesMockMvc;

    private Collectivites collectivites;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CollectivitesResource collectivitesResource = new CollectivitesResource(collectivitesService);
        this.restCollectivitesMockMvc = MockMvcBuilders.standaloneSetup(collectivitesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Collectivites createEntity(EntityManager em) {
        Collectivites collectivites = new Collectivites()
            .name(DEFAULT_NAME);
        return collectivites;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Collectivites createUpdatedEntity(EntityManager em) {
        Collectivites collectivites = new Collectivites()
            .name(UPDATED_NAME);
        return collectivites;
    }

    @BeforeEach
    public void initTest() {
        collectivites = createEntity(em);
    }

    @Test
    @Transactional
    public void createCollectivites() throws Exception {
        int databaseSizeBeforeCreate = collectivitesRepository.findAll().size();

        // Create the Collectivites
        CollectivitesDTO collectivitesDTO = collectivitesMapper.toDto(collectivites);
        restCollectivitesMockMvc.perform(post("/api/collectivites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectivitesDTO)))
            .andExpect(status().isCreated());

        // Validate the Collectivites in the database
        List<Collectivites> collectivitesList = collectivitesRepository.findAll();
        assertThat(collectivitesList).hasSize(databaseSizeBeforeCreate + 1);
        Collectivites testCollectivites = collectivitesList.get(collectivitesList.size() - 1);
        assertThat(testCollectivites.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCollectivitesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = collectivitesRepository.findAll().size();

        // Create the Collectivites with an existing ID
        collectivites.setId(1L);
        CollectivitesDTO collectivitesDTO = collectivitesMapper.toDto(collectivites);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollectivitesMockMvc.perform(post("/api/collectivites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectivitesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Collectivites in the database
        List<Collectivites> collectivitesList = collectivitesRepository.findAll();
        assertThat(collectivitesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCollectivites() throws Exception {
        // Initialize the database
        collectivitesRepository.saveAndFlush(collectivites);

        // Get all the collectivitesList
        restCollectivitesMockMvc.perform(get("/api/collectivites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collectivites.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getCollectivites() throws Exception {
        // Initialize the database
        collectivitesRepository.saveAndFlush(collectivites);

        // Get the collectivites
        restCollectivitesMockMvc.perform(get("/api/collectivites/{id}", collectivites.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(collectivites.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingCollectivites() throws Exception {
        // Get the collectivites
        restCollectivitesMockMvc.perform(get("/api/collectivites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCollectivites() throws Exception {
        // Initialize the database
        collectivitesRepository.saveAndFlush(collectivites);

        int databaseSizeBeforeUpdate = collectivitesRepository.findAll().size();

        // Update the collectivites
        Collectivites updatedCollectivites = collectivitesRepository.findById(collectivites.getId()).get();
        // Disconnect from session so that the updates on updatedCollectivites are not directly saved in db
        em.detach(updatedCollectivites);
        updatedCollectivites
            .name(UPDATED_NAME);
        CollectivitesDTO collectivitesDTO = collectivitesMapper.toDto(updatedCollectivites);

        restCollectivitesMockMvc.perform(put("/api/collectivites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectivitesDTO)))
            .andExpect(status().isOk());

        // Validate the Collectivites in the database
        List<Collectivites> collectivitesList = collectivitesRepository.findAll();
        assertThat(collectivitesList).hasSize(databaseSizeBeforeUpdate);
        Collectivites testCollectivites = collectivitesList.get(collectivitesList.size() - 1);
        assertThat(testCollectivites.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCollectivites() throws Exception {
        int databaseSizeBeforeUpdate = collectivitesRepository.findAll().size();

        // Create the Collectivites
        CollectivitesDTO collectivitesDTO = collectivitesMapper.toDto(collectivites);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollectivitesMockMvc.perform(put("/api/collectivites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectivitesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Collectivites in the database
        List<Collectivites> collectivitesList = collectivitesRepository.findAll();
        assertThat(collectivitesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCollectivites() throws Exception {
        // Initialize the database
        collectivitesRepository.saveAndFlush(collectivites);

        int databaseSizeBeforeDelete = collectivitesRepository.findAll().size();

        // Delete the collectivites
        restCollectivitesMockMvc.perform(delete("/api/collectivites/{id}", collectivites.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Collectivites> collectivitesList = collectivitesRepository.findAll();
        assertThat(collectivitesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Collectivites.class);
        Collectivites collectivites1 = new Collectivites();
        collectivites1.setId(1L);
        Collectivites collectivites2 = new Collectivites();
        collectivites2.setId(collectivites1.getId());
        assertThat(collectivites1).isEqualTo(collectivites2);
        collectivites2.setId(2L);
        assertThat(collectivites1).isNotEqualTo(collectivites2);
        collectivites1.setId(null);
        assertThat(collectivites1).isNotEqualTo(collectivites2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollectivitesDTO.class);
        CollectivitesDTO collectivitesDTO1 = new CollectivitesDTO();
        collectivitesDTO1.setId(1L);
        CollectivitesDTO collectivitesDTO2 = new CollectivitesDTO();
        assertThat(collectivitesDTO1).isNotEqualTo(collectivitesDTO2);
        collectivitesDTO2.setId(collectivitesDTO1.getId());
        assertThat(collectivitesDTO1).isEqualTo(collectivitesDTO2);
        collectivitesDTO2.setId(2L);
        assertThat(collectivitesDTO1).isNotEqualTo(collectivitesDTO2);
        collectivitesDTO1.setId(null);
        assertThat(collectivitesDTO1).isNotEqualTo(collectivitesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(collectivitesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(collectivitesMapper.fromId(null)).isNull();
    }
}
