package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RefsApp;
import com.mycompany.myapp.domain.ReclamantTypes;
import com.mycompany.myapp.repository.ReclamantTypesRepository;
import com.mycompany.myapp.service.ReclamantTypesService;
import com.mycompany.myapp.service.dto.ReclamantTypesDTO;
import com.mycompany.myapp.service.mapper.ReclamantTypesMapper;
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
 * Integration tests for the {@link ReclamantTypesResource} REST controller.
 */
@SpringBootTest(classes = RefsApp.class)
public class ReclamantTypesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ReclamantTypesRepository reclamantTypesRepository;

    @Autowired
    private ReclamantTypesMapper reclamantTypesMapper;

    @Autowired
    private ReclamantTypesService reclamantTypesService;

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

    private MockMvc restReclamantTypesMockMvc;

    private ReclamantTypes reclamantTypes;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReclamantTypesResource reclamantTypesResource = new ReclamantTypesResource(reclamantTypesService);
        this.restReclamantTypesMockMvc = MockMvcBuilders.standaloneSetup(reclamantTypesResource)
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
    public static ReclamantTypes createEntity(EntityManager em) {
        ReclamantTypes reclamantTypes = new ReclamantTypes()
            .name(DEFAULT_NAME);
        return reclamantTypes;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReclamantTypes createUpdatedEntity(EntityManager em) {
        ReclamantTypes reclamantTypes = new ReclamantTypes()
            .name(UPDATED_NAME);
        return reclamantTypes;
    }

    @BeforeEach
    public void initTest() {
        reclamantTypes = createEntity(em);
    }

    @Test
    @Transactional
    public void createReclamantTypes() throws Exception {
        int databaseSizeBeforeCreate = reclamantTypesRepository.findAll().size();

        // Create the ReclamantTypes
        ReclamantTypesDTO reclamantTypesDTO = reclamantTypesMapper.toDto(reclamantTypes);
        restReclamantTypesMockMvc.perform(post("/api/reclamant-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reclamantTypesDTO)))
            .andExpect(status().isCreated());

        // Validate the ReclamantTypes in the database
        List<ReclamantTypes> reclamantTypesList = reclamantTypesRepository.findAll();
        assertThat(reclamantTypesList).hasSize(databaseSizeBeforeCreate + 1);
        ReclamantTypes testReclamantTypes = reclamantTypesList.get(reclamantTypesList.size() - 1);
        assertThat(testReclamantTypes.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createReclamantTypesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reclamantTypesRepository.findAll().size();

        // Create the ReclamantTypes with an existing ID
        reclamantTypes.setId(1L);
        ReclamantTypesDTO reclamantTypesDTO = reclamantTypesMapper.toDto(reclamantTypes);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReclamantTypesMockMvc.perform(post("/api/reclamant-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reclamantTypesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReclamantTypes in the database
        List<ReclamantTypes> reclamantTypesList = reclamantTypesRepository.findAll();
        assertThat(reclamantTypesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllReclamantTypes() throws Exception {
        // Initialize the database
        reclamantTypesRepository.saveAndFlush(reclamantTypes);

        // Get all the reclamantTypesList
        restReclamantTypesMockMvc.perform(get("/api/reclamant-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reclamantTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getReclamantTypes() throws Exception {
        // Initialize the database
        reclamantTypesRepository.saveAndFlush(reclamantTypes);

        // Get the reclamantTypes
        restReclamantTypesMockMvc.perform(get("/api/reclamant-types/{id}", reclamantTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reclamantTypes.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingReclamantTypes() throws Exception {
        // Get the reclamantTypes
        restReclamantTypesMockMvc.perform(get("/api/reclamant-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReclamantTypes() throws Exception {
        // Initialize the database
        reclamantTypesRepository.saveAndFlush(reclamantTypes);

        int databaseSizeBeforeUpdate = reclamantTypesRepository.findAll().size();

        // Update the reclamantTypes
        ReclamantTypes updatedReclamantTypes = reclamantTypesRepository.findById(reclamantTypes.getId()).get();
        // Disconnect from session so that the updates on updatedReclamantTypes are not directly saved in db
        em.detach(updatedReclamantTypes);
        updatedReclamantTypes
            .name(UPDATED_NAME);
        ReclamantTypesDTO reclamantTypesDTO = reclamantTypesMapper.toDto(updatedReclamantTypes);

        restReclamantTypesMockMvc.perform(put("/api/reclamant-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reclamantTypesDTO)))
            .andExpect(status().isOk());

        // Validate the ReclamantTypes in the database
        List<ReclamantTypes> reclamantTypesList = reclamantTypesRepository.findAll();
        assertThat(reclamantTypesList).hasSize(databaseSizeBeforeUpdate);
        ReclamantTypes testReclamantTypes = reclamantTypesList.get(reclamantTypesList.size() - 1);
        assertThat(testReclamantTypes.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingReclamantTypes() throws Exception {
        int databaseSizeBeforeUpdate = reclamantTypesRepository.findAll().size();

        // Create the ReclamantTypes
        ReclamantTypesDTO reclamantTypesDTO = reclamantTypesMapper.toDto(reclamantTypes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReclamantTypesMockMvc.perform(put("/api/reclamant-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reclamantTypesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReclamantTypes in the database
        List<ReclamantTypes> reclamantTypesList = reclamantTypesRepository.findAll();
        assertThat(reclamantTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReclamantTypes() throws Exception {
        // Initialize the database
        reclamantTypesRepository.saveAndFlush(reclamantTypes);

        int databaseSizeBeforeDelete = reclamantTypesRepository.findAll().size();

        // Delete the reclamantTypes
        restReclamantTypesMockMvc.perform(delete("/api/reclamant-types/{id}", reclamantTypes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReclamantTypes> reclamantTypesList = reclamantTypesRepository.findAll();
        assertThat(reclamantTypesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReclamantTypes.class);
        ReclamantTypes reclamantTypes1 = new ReclamantTypes();
        reclamantTypes1.setId(1L);
        ReclamantTypes reclamantTypes2 = new ReclamantTypes();
        reclamantTypes2.setId(reclamantTypes1.getId());
        assertThat(reclamantTypes1).isEqualTo(reclamantTypes2);
        reclamantTypes2.setId(2L);
        assertThat(reclamantTypes1).isNotEqualTo(reclamantTypes2);
        reclamantTypes1.setId(null);
        assertThat(reclamantTypes1).isNotEqualTo(reclamantTypes2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReclamantTypesDTO.class);
        ReclamantTypesDTO reclamantTypesDTO1 = new ReclamantTypesDTO();
        reclamantTypesDTO1.setId(1L);
        ReclamantTypesDTO reclamantTypesDTO2 = new ReclamantTypesDTO();
        assertThat(reclamantTypesDTO1).isNotEqualTo(reclamantTypesDTO2);
        reclamantTypesDTO2.setId(reclamantTypesDTO1.getId());
        assertThat(reclamantTypesDTO1).isEqualTo(reclamantTypesDTO2);
        reclamantTypesDTO2.setId(2L);
        assertThat(reclamantTypesDTO1).isNotEqualTo(reclamantTypesDTO2);
        reclamantTypesDTO1.setId(null);
        assertThat(reclamantTypesDTO1).isNotEqualTo(reclamantTypesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(reclamantTypesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(reclamantTypesMapper.fromId(null)).isNull();
    }
}
