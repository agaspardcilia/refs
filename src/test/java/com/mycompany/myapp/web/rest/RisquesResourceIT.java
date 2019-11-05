package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RefsApp;
import com.mycompany.myapp.domain.Risques;
import com.mycompany.myapp.repository.RisquesRepository;
import com.mycompany.myapp.service.RisquesService;
import com.mycompany.myapp.service.dto.RisquesDTO;
import com.mycompany.myapp.service.mapper.RisquesMapper;
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
 * Integration tests for the {@link RisquesResource} REST controller.
 */
@SpringBootTest(classes = RefsApp.class)
public class RisquesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private RisquesRepository risquesRepository;

    @Autowired
    private RisquesMapper risquesMapper;

    @Autowired
    private RisquesService risquesService;

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

    private MockMvc restRisquesMockMvc;

    private Risques risques;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RisquesResource risquesResource = new RisquesResource(risquesService);
        this.restRisquesMockMvc = MockMvcBuilders.standaloneSetup(risquesResource)
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
    public static Risques createEntity(EntityManager em) {
        Risques risques = new Risques()
            .name(DEFAULT_NAME);
        return risques;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Risques createUpdatedEntity(EntityManager em) {
        Risques risques = new Risques()
            .name(UPDATED_NAME);
        return risques;
    }

    @BeforeEach
    public void initTest() {
        risques = createEntity(em);
    }

    @Test
    @Transactional
    public void createRisques() throws Exception {
        int databaseSizeBeforeCreate = risquesRepository.findAll().size();

        // Create the Risques
        RisquesDTO risquesDTO = risquesMapper.toDto(risques);
        restRisquesMockMvc.perform(post("/api/risques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(risquesDTO)))
            .andExpect(status().isCreated());

        // Validate the Risques in the database
        List<Risques> risquesList = risquesRepository.findAll();
        assertThat(risquesList).hasSize(databaseSizeBeforeCreate + 1);
        Risques testRisques = risquesList.get(risquesList.size() - 1);
        assertThat(testRisques.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createRisquesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = risquesRepository.findAll().size();

        // Create the Risques with an existing ID
        risques.setId(1L);
        RisquesDTO risquesDTO = risquesMapper.toDto(risques);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRisquesMockMvc.perform(post("/api/risques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(risquesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Risques in the database
        List<Risques> risquesList = risquesRepository.findAll();
        assertThat(risquesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRisques() throws Exception {
        // Initialize the database
        risquesRepository.saveAndFlush(risques);

        // Get all the risquesList
        restRisquesMockMvc.perform(get("/api/risques?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(risques.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getRisques() throws Exception {
        // Initialize the database
        risquesRepository.saveAndFlush(risques);

        // Get the risques
        restRisquesMockMvc.perform(get("/api/risques/{id}", risques.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(risques.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingRisques() throws Exception {
        // Get the risques
        restRisquesMockMvc.perform(get("/api/risques/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRisques() throws Exception {
        // Initialize the database
        risquesRepository.saveAndFlush(risques);

        int databaseSizeBeforeUpdate = risquesRepository.findAll().size();

        // Update the risques
        Risques updatedRisques = risquesRepository.findById(risques.getId()).get();
        // Disconnect from session so that the updates on updatedRisques are not directly saved in db
        em.detach(updatedRisques);
        updatedRisques
            .name(UPDATED_NAME);
        RisquesDTO risquesDTO = risquesMapper.toDto(updatedRisques);

        restRisquesMockMvc.perform(put("/api/risques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(risquesDTO)))
            .andExpect(status().isOk());

        // Validate the Risques in the database
        List<Risques> risquesList = risquesRepository.findAll();
        assertThat(risquesList).hasSize(databaseSizeBeforeUpdate);
        Risques testRisques = risquesList.get(risquesList.size() - 1);
        assertThat(testRisques.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingRisques() throws Exception {
        int databaseSizeBeforeUpdate = risquesRepository.findAll().size();

        // Create the Risques
        RisquesDTO risquesDTO = risquesMapper.toDto(risques);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRisquesMockMvc.perform(put("/api/risques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(risquesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Risques in the database
        List<Risques> risquesList = risquesRepository.findAll();
        assertThat(risquesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRisques() throws Exception {
        // Initialize the database
        risquesRepository.saveAndFlush(risques);

        int databaseSizeBeforeDelete = risquesRepository.findAll().size();

        // Delete the risques
        restRisquesMockMvc.perform(delete("/api/risques/{id}", risques.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Risques> risquesList = risquesRepository.findAll();
        assertThat(risquesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Risques.class);
        Risques risques1 = new Risques();
        risques1.setId(1L);
        Risques risques2 = new Risques();
        risques2.setId(risques1.getId());
        assertThat(risques1).isEqualTo(risques2);
        risques2.setId(2L);
        assertThat(risques1).isNotEqualTo(risques2);
        risques1.setId(null);
        assertThat(risques1).isNotEqualTo(risques2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RisquesDTO.class);
        RisquesDTO risquesDTO1 = new RisquesDTO();
        risquesDTO1.setId(1L);
        RisquesDTO risquesDTO2 = new RisquesDTO();
        assertThat(risquesDTO1).isNotEqualTo(risquesDTO2);
        risquesDTO2.setId(risquesDTO1.getId());
        assertThat(risquesDTO1).isEqualTo(risquesDTO2);
        risquesDTO2.setId(2L);
        assertThat(risquesDTO1).isNotEqualTo(risquesDTO2);
        risquesDTO1.setId(null);
        assertThat(risquesDTO1).isNotEqualTo(risquesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(risquesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(risquesMapper.fromId(null)).isNull();
    }
}
