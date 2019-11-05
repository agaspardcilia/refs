package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RefsApp;
import com.mycompany.myapp.domain.Civilites;
import com.mycompany.myapp.repository.CivilitesRepository;
import com.mycompany.myapp.service.CivilitesService;
import com.mycompany.myapp.service.dto.CivilitesDTO;
import com.mycompany.myapp.service.mapper.CivilitesMapper;
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
 * Integration tests for the {@link CivilitesResource} REST controller.
 */
@SpringBootTest(classes = RefsApp.class)
public class CivilitesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CivilitesRepository civilitesRepository;

    @Autowired
    private CivilitesMapper civilitesMapper;

    @Autowired
    private CivilitesService civilitesService;

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

    private MockMvc restCivilitesMockMvc;

    private Civilites civilites;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CivilitesResource civilitesResource = new CivilitesResource(civilitesService);
        this.restCivilitesMockMvc = MockMvcBuilders.standaloneSetup(civilitesResource)
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
    public static Civilites createEntity(EntityManager em) {
        Civilites civilites = new Civilites()
            .name(DEFAULT_NAME);
        return civilites;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Civilites createUpdatedEntity(EntityManager em) {
        Civilites civilites = new Civilites()
            .name(UPDATED_NAME);
        return civilites;
    }

    @BeforeEach
    public void initTest() {
        civilites = createEntity(em);
    }

    @Test
    @Transactional
    public void createCivilites() throws Exception {
        int databaseSizeBeforeCreate = civilitesRepository.findAll().size();

        // Create the Civilites
        CivilitesDTO civilitesDTO = civilitesMapper.toDto(civilites);
        restCivilitesMockMvc.perform(post("/api/civilites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(civilitesDTO)))
            .andExpect(status().isCreated());

        // Validate the Civilites in the database
        List<Civilites> civilitesList = civilitesRepository.findAll();
        assertThat(civilitesList).hasSize(databaseSizeBeforeCreate + 1);
        Civilites testCivilites = civilitesList.get(civilitesList.size() - 1);
        assertThat(testCivilites.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCivilitesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = civilitesRepository.findAll().size();

        // Create the Civilites with an existing ID
        civilites.setId(1L);
        CivilitesDTO civilitesDTO = civilitesMapper.toDto(civilites);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCivilitesMockMvc.perform(post("/api/civilites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(civilitesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Civilites in the database
        List<Civilites> civilitesList = civilitesRepository.findAll();
        assertThat(civilitesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCivilites() throws Exception {
        // Initialize the database
        civilitesRepository.saveAndFlush(civilites);

        // Get all the civilitesList
        restCivilitesMockMvc.perform(get("/api/civilites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(civilites.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getCivilites() throws Exception {
        // Initialize the database
        civilitesRepository.saveAndFlush(civilites);

        // Get the civilites
        restCivilitesMockMvc.perform(get("/api/civilites/{id}", civilites.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(civilites.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingCivilites() throws Exception {
        // Get the civilites
        restCivilitesMockMvc.perform(get("/api/civilites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCivilites() throws Exception {
        // Initialize the database
        civilitesRepository.saveAndFlush(civilites);

        int databaseSizeBeforeUpdate = civilitesRepository.findAll().size();

        // Update the civilites
        Civilites updatedCivilites = civilitesRepository.findById(civilites.getId()).get();
        // Disconnect from session so that the updates on updatedCivilites are not directly saved in db
        em.detach(updatedCivilites);
        updatedCivilites
            .name(UPDATED_NAME);
        CivilitesDTO civilitesDTO = civilitesMapper.toDto(updatedCivilites);

        restCivilitesMockMvc.perform(put("/api/civilites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(civilitesDTO)))
            .andExpect(status().isOk());

        // Validate the Civilites in the database
        List<Civilites> civilitesList = civilitesRepository.findAll();
        assertThat(civilitesList).hasSize(databaseSizeBeforeUpdate);
        Civilites testCivilites = civilitesList.get(civilitesList.size() - 1);
        assertThat(testCivilites.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCivilites() throws Exception {
        int databaseSizeBeforeUpdate = civilitesRepository.findAll().size();

        // Create the Civilites
        CivilitesDTO civilitesDTO = civilitesMapper.toDto(civilites);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCivilitesMockMvc.perform(put("/api/civilites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(civilitesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Civilites in the database
        List<Civilites> civilitesList = civilitesRepository.findAll();
        assertThat(civilitesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCivilites() throws Exception {
        // Initialize the database
        civilitesRepository.saveAndFlush(civilites);

        int databaseSizeBeforeDelete = civilitesRepository.findAll().size();

        // Delete the civilites
        restCivilitesMockMvc.perform(delete("/api/civilites/{id}", civilites.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Civilites> civilitesList = civilitesRepository.findAll();
        assertThat(civilitesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Civilites.class);
        Civilites civilites1 = new Civilites();
        civilites1.setId(1L);
        Civilites civilites2 = new Civilites();
        civilites2.setId(civilites1.getId());
        assertThat(civilites1).isEqualTo(civilites2);
        civilites2.setId(2L);
        assertThat(civilites1).isNotEqualTo(civilites2);
        civilites1.setId(null);
        assertThat(civilites1).isNotEqualTo(civilites2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CivilitesDTO.class);
        CivilitesDTO civilitesDTO1 = new CivilitesDTO();
        civilitesDTO1.setId(1L);
        CivilitesDTO civilitesDTO2 = new CivilitesDTO();
        assertThat(civilitesDTO1).isNotEqualTo(civilitesDTO2);
        civilitesDTO2.setId(civilitesDTO1.getId());
        assertThat(civilitesDTO1).isEqualTo(civilitesDTO2);
        civilitesDTO2.setId(2L);
        assertThat(civilitesDTO1).isNotEqualTo(civilitesDTO2);
        civilitesDTO1.setId(null);
        assertThat(civilitesDTO1).isNotEqualTo(civilitesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(civilitesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(civilitesMapper.fromId(null)).isNull();
    }
}
