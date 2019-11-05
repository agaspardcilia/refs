package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RefsApp;
import com.mycompany.myapp.domain.SousMotifs;
import com.mycompany.myapp.repository.SousMotifsRepository;
import com.mycompany.myapp.service.SousMotifsService;
import com.mycompany.myapp.service.dto.SousMotifsDTO;
import com.mycompany.myapp.service.mapper.SousMotifsMapper;
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
 * Integration tests for the {@link SousMotifsResource} REST controller.
 */
@SpringBootTest(classes = RefsApp.class)
public class SousMotifsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private SousMotifsRepository sousMotifsRepository;

    @Autowired
    private SousMotifsMapper sousMotifsMapper;

    @Autowired
    private SousMotifsService sousMotifsService;

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

    private MockMvc restSousMotifsMockMvc;

    private SousMotifs sousMotifs;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SousMotifsResource sousMotifsResource = new SousMotifsResource(sousMotifsService);
        this.restSousMotifsMockMvc = MockMvcBuilders.standaloneSetup(sousMotifsResource)
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
    public static SousMotifs createEntity(EntityManager em) {
        SousMotifs sousMotifs = new SousMotifs()
            .name(DEFAULT_NAME);
        return sousMotifs;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousMotifs createUpdatedEntity(EntityManager em) {
        SousMotifs sousMotifs = new SousMotifs()
            .name(UPDATED_NAME);
        return sousMotifs;
    }

    @BeforeEach
    public void initTest() {
        sousMotifs = createEntity(em);
    }

    @Test
    @Transactional
    public void createSousMotifs() throws Exception {
        int databaseSizeBeforeCreate = sousMotifsRepository.findAll().size();

        // Create the SousMotifs
        SousMotifsDTO sousMotifsDTO = sousMotifsMapper.toDto(sousMotifs);
        restSousMotifsMockMvc.perform(post("/api/sous-motifs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sousMotifsDTO)))
            .andExpect(status().isCreated());

        // Validate the SousMotifs in the database
        List<SousMotifs> sousMotifsList = sousMotifsRepository.findAll();
        assertThat(sousMotifsList).hasSize(databaseSizeBeforeCreate + 1);
        SousMotifs testSousMotifs = sousMotifsList.get(sousMotifsList.size() - 1);
        assertThat(testSousMotifs.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSousMotifsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sousMotifsRepository.findAll().size();

        // Create the SousMotifs with an existing ID
        sousMotifs.setId(1L);
        SousMotifsDTO sousMotifsDTO = sousMotifsMapper.toDto(sousMotifs);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSousMotifsMockMvc.perform(post("/api/sous-motifs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sousMotifsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SousMotifs in the database
        List<SousMotifs> sousMotifsList = sousMotifsRepository.findAll();
        assertThat(sousMotifsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSousMotifs() throws Exception {
        // Initialize the database
        sousMotifsRepository.saveAndFlush(sousMotifs);

        // Get all the sousMotifsList
        restSousMotifsMockMvc.perform(get("/api/sous-motifs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousMotifs.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getSousMotifs() throws Exception {
        // Initialize the database
        sousMotifsRepository.saveAndFlush(sousMotifs);

        // Get the sousMotifs
        restSousMotifsMockMvc.perform(get("/api/sous-motifs/{id}", sousMotifs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sousMotifs.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingSousMotifs() throws Exception {
        // Get the sousMotifs
        restSousMotifsMockMvc.perform(get("/api/sous-motifs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSousMotifs() throws Exception {
        // Initialize the database
        sousMotifsRepository.saveAndFlush(sousMotifs);

        int databaseSizeBeforeUpdate = sousMotifsRepository.findAll().size();

        // Update the sousMotifs
        SousMotifs updatedSousMotifs = sousMotifsRepository.findById(sousMotifs.getId()).get();
        // Disconnect from session so that the updates on updatedSousMotifs are not directly saved in db
        em.detach(updatedSousMotifs);
        updatedSousMotifs
            .name(UPDATED_NAME);
        SousMotifsDTO sousMotifsDTO = sousMotifsMapper.toDto(updatedSousMotifs);

        restSousMotifsMockMvc.perform(put("/api/sous-motifs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sousMotifsDTO)))
            .andExpect(status().isOk());

        // Validate the SousMotifs in the database
        List<SousMotifs> sousMotifsList = sousMotifsRepository.findAll();
        assertThat(sousMotifsList).hasSize(databaseSizeBeforeUpdate);
        SousMotifs testSousMotifs = sousMotifsList.get(sousMotifsList.size() - 1);
        assertThat(testSousMotifs.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingSousMotifs() throws Exception {
        int databaseSizeBeforeUpdate = sousMotifsRepository.findAll().size();

        // Create the SousMotifs
        SousMotifsDTO sousMotifsDTO = sousMotifsMapper.toDto(sousMotifs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousMotifsMockMvc.perform(put("/api/sous-motifs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sousMotifsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SousMotifs in the database
        List<SousMotifs> sousMotifsList = sousMotifsRepository.findAll();
        assertThat(sousMotifsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSousMotifs() throws Exception {
        // Initialize the database
        sousMotifsRepository.saveAndFlush(sousMotifs);

        int databaseSizeBeforeDelete = sousMotifsRepository.findAll().size();

        // Delete the sousMotifs
        restSousMotifsMockMvc.perform(delete("/api/sous-motifs/{id}", sousMotifs.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SousMotifs> sousMotifsList = sousMotifsRepository.findAll();
        assertThat(sousMotifsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousMotifs.class);
        SousMotifs sousMotifs1 = new SousMotifs();
        sousMotifs1.setId(1L);
        SousMotifs sousMotifs2 = new SousMotifs();
        sousMotifs2.setId(sousMotifs1.getId());
        assertThat(sousMotifs1).isEqualTo(sousMotifs2);
        sousMotifs2.setId(2L);
        assertThat(sousMotifs1).isNotEqualTo(sousMotifs2);
        sousMotifs1.setId(null);
        assertThat(sousMotifs1).isNotEqualTo(sousMotifs2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousMotifsDTO.class);
        SousMotifsDTO sousMotifsDTO1 = new SousMotifsDTO();
        sousMotifsDTO1.setId(1L);
        SousMotifsDTO sousMotifsDTO2 = new SousMotifsDTO();
        assertThat(sousMotifsDTO1).isNotEqualTo(sousMotifsDTO2);
        sousMotifsDTO2.setId(sousMotifsDTO1.getId());
        assertThat(sousMotifsDTO1).isEqualTo(sousMotifsDTO2);
        sousMotifsDTO2.setId(2L);
        assertThat(sousMotifsDTO1).isNotEqualTo(sousMotifsDTO2);
        sousMotifsDTO1.setId(null);
        assertThat(sousMotifsDTO1).isNotEqualTo(sousMotifsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sousMotifsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sousMotifsMapper.fromId(null)).isNull();
    }
}
