package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RefsApp;
import com.mycompany.myapp.domain.SourceTransmissions;
import com.mycompany.myapp.repository.SourceTransmissionsRepository;
import com.mycompany.myapp.service.SourceTransmissionsService;
import com.mycompany.myapp.service.dto.SourceTransmissionsDTO;
import com.mycompany.myapp.service.mapper.SourceTransmissionsMapper;
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
 * Integration tests for the {@link SourceTransmissionsResource} REST controller.
 */
@SpringBootTest(classes = RefsApp.class)
public class SourceTransmissionsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private SourceTransmissionsRepository sourceTransmissionsRepository;

    @Autowired
    private SourceTransmissionsMapper sourceTransmissionsMapper;

    @Autowired
    private SourceTransmissionsService sourceTransmissionsService;

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

    private MockMvc restSourceTransmissionsMockMvc;

    private SourceTransmissions sourceTransmissions;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SourceTransmissionsResource sourceTransmissionsResource = new SourceTransmissionsResource(sourceTransmissionsService);
        this.restSourceTransmissionsMockMvc = MockMvcBuilders.standaloneSetup(sourceTransmissionsResource)
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
    public static SourceTransmissions createEntity(EntityManager em) {
        SourceTransmissions sourceTransmissions = new SourceTransmissions()
            .name(DEFAULT_NAME);
        return sourceTransmissions;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourceTransmissions createUpdatedEntity(EntityManager em) {
        SourceTransmissions sourceTransmissions = new SourceTransmissions()
            .name(UPDATED_NAME);
        return sourceTransmissions;
    }

    @BeforeEach
    public void initTest() {
        sourceTransmissions = createEntity(em);
    }

    @Test
    @Transactional
    public void createSourceTransmissions() throws Exception {
        int databaseSizeBeforeCreate = sourceTransmissionsRepository.findAll().size();

        // Create the SourceTransmissions
        SourceTransmissionsDTO sourceTransmissionsDTO = sourceTransmissionsMapper.toDto(sourceTransmissions);
        restSourceTransmissionsMockMvc.perform(post("/api/source-transmissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceTransmissionsDTO)))
            .andExpect(status().isCreated());

        // Validate the SourceTransmissions in the database
        List<SourceTransmissions> sourceTransmissionsList = sourceTransmissionsRepository.findAll();
        assertThat(sourceTransmissionsList).hasSize(databaseSizeBeforeCreate + 1);
        SourceTransmissions testSourceTransmissions = sourceTransmissionsList.get(sourceTransmissionsList.size() - 1);
        assertThat(testSourceTransmissions.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSourceTransmissionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sourceTransmissionsRepository.findAll().size();

        // Create the SourceTransmissions with an existing ID
        sourceTransmissions.setId(1L);
        SourceTransmissionsDTO sourceTransmissionsDTO = sourceTransmissionsMapper.toDto(sourceTransmissions);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceTransmissionsMockMvc.perform(post("/api/source-transmissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceTransmissionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SourceTransmissions in the database
        List<SourceTransmissions> sourceTransmissionsList = sourceTransmissionsRepository.findAll();
        assertThat(sourceTransmissionsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSourceTransmissions() throws Exception {
        // Initialize the database
        sourceTransmissionsRepository.saveAndFlush(sourceTransmissions);

        // Get all the sourceTransmissionsList
        restSourceTransmissionsMockMvc.perform(get("/api/source-transmissions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceTransmissions.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getSourceTransmissions() throws Exception {
        // Initialize the database
        sourceTransmissionsRepository.saveAndFlush(sourceTransmissions);

        // Get the sourceTransmissions
        restSourceTransmissionsMockMvc.perform(get("/api/source-transmissions/{id}", sourceTransmissions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sourceTransmissions.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingSourceTransmissions() throws Exception {
        // Get the sourceTransmissions
        restSourceTransmissionsMockMvc.perform(get("/api/source-transmissions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSourceTransmissions() throws Exception {
        // Initialize the database
        sourceTransmissionsRepository.saveAndFlush(sourceTransmissions);

        int databaseSizeBeforeUpdate = sourceTransmissionsRepository.findAll().size();

        // Update the sourceTransmissions
        SourceTransmissions updatedSourceTransmissions = sourceTransmissionsRepository.findById(sourceTransmissions.getId()).get();
        // Disconnect from session so that the updates on updatedSourceTransmissions are not directly saved in db
        em.detach(updatedSourceTransmissions);
        updatedSourceTransmissions
            .name(UPDATED_NAME);
        SourceTransmissionsDTO sourceTransmissionsDTO = sourceTransmissionsMapper.toDto(updatedSourceTransmissions);

        restSourceTransmissionsMockMvc.perform(put("/api/source-transmissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceTransmissionsDTO)))
            .andExpect(status().isOk());

        // Validate the SourceTransmissions in the database
        List<SourceTransmissions> sourceTransmissionsList = sourceTransmissionsRepository.findAll();
        assertThat(sourceTransmissionsList).hasSize(databaseSizeBeforeUpdate);
        SourceTransmissions testSourceTransmissions = sourceTransmissionsList.get(sourceTransmissionsList.size() - 1);
        assertThat(testSourceTransmissions.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingSourceTransmissions() throws Exception {
        int databaseSizeBeforeUpdate = sourceTransmissionsRepository.findAll().size();

        // Create the SourceTransmissions
        SourceTransmissionsDTO sourceTransmissionsDTO = sourceTransmissionsMapper.toDto(sourceTransmissions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceTransmissionsMockMvc.perform(put("/api/source-transmissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sourceTransmissionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SourceTransmissions in the database
        List<SourceTransmissions> sourceTransmissionsList = sourceTransmissionsRepository.findAll();
        assertThat(sourceTransmissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSourceTransmissions() throws Exception {
        // Initialize the database
        sourceTransmissionsRepository.saveAndFlush(sourceTransmissions);

        int databaseSizeBeforeDelete = sourceTransmissionsRepository.findAll().size();

        // Delete the sourceTransmissions
        restSourceTransmissionsMockMvc.perform(delete("/api/source-transmissions/{id}", sourceTransmissions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SourceTransmissions> sourceTransmissionsList = sourceTransmissionsRepository.findAll();
        assertThat(sourceTransmissionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceTransmissions.class);
        SourceTransmissions sourceTransmissions1 = new SourceTransmissions();
        sourceTransmissions1.setId(1L);
        SourceTransmissions sourceTransmissions2 = new SourceTransmissions();
        sourceTransmissions2.setId(sourceTransmissions1.getId());
        assertThat(sourceTransmissions1).isEqualTo(sourceTransmissions2);
        sourceTransmissions2.setId(2L);
        assertThat(sourceTransmissions1).isNotEqualTo(sourceTransmissions2);
        sourceTransmissions1.setId(null);
        assertThat(sourceTransmissions1).isNotEqualTo(sourceTransmissions2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceTransmissionsDTO.class);
        SourceTransmissionsDTO sourceTransmissionsDTO1 = new SourceTransmissionsDTO();
        sourceTransmissionsDTO1.setId(1L);
        SourceTransmissionsDTO sourceTransmissionsDTO2 = new SourceTransmissionsDTO();
        assertThat(sourceTransmissionsDTO1).isNotEqualTo(sourceTransmissionsDTO2);
        sourceTransmissionsDTO2.setId(sourceTransmissionsDTO1.getId());
        assertThat(sourceTransmissionsDTO1).isEqualTo(sourceTransmissionsDTO2);
        sourceTransmissionsDTO2.setId(2L);
        assertThat(sourceTransmissionsDTO1).isNotEqualTo(sourceTransmissionsDTO2);
        sourceTransmissionsDTO1.setId(null);
        assertThat(sourceTransmissionsDTO1).isNotEqualTo(sourceTransmissionsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sourceTransmissionsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sourceTransmissionsMapper.fromId(null)).isNull();
    }
}
