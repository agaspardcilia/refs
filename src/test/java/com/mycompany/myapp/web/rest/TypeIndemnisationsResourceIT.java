package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RefsApp;
import com.mycompany.myapp.domain.TypeIndemnisations;
import com.mycompany.myapp.repository.TypeIndemnisationsRepository;
import com.mycompany.myapp.service.TypeIndemnisationsService;
import com.mycompany.myapp.service.dto.TypeIndemnisationsDTO;
import com.mycompany.myapp.service.mapper.TypeIndemnisationsMapper;
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
 * Integration tests for the {@link TypeIndemnisationsResource} REST controller.
 */
@SpringBootTest(classes = RefsApp.class)
public class TypeIndemnisationsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private TypeIndemnisationsRepository typeIndemnisationsRepository;

    @Autowired
    private TypeIndemnisationsMapper typeIndemnisationsMapper;

    @Autowired
    private TypeIndemnisationsService typeIndemnisationsService;

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

    private MockMvc restTypeIndemnisationsMockMvc;

    private TypeIndemnisations typeIndemnisations;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TypeIndemnisationsResource typeIndemnisationsResource = new TypeIndemnisationsResource(typeIndemnisationsService);
        this.restTypeIndemnisationsMockMvc = MockMvcBuilders.standaloneSetup(typeIndemnisationsResource)
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
    public static TypeIndemnisations createEntity(EntityManager em) {
        TypeIndemnisations typeIndemnisations = new TypeIndemnisations()
            .name(DEFAULT_NAME);
        return typeIndemnisations;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeIndemnisations createUpdatedEntity(EntityManager em) {
        TypeIndemnisations typeIndemnisations = new TypeIndemnisations()
            .name(UPDATED_NAME);
        return typeIndemnisations;
    }

    @BeforeEach
    public void initTest() {
        typeIndemnisations = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeIndemnisations() throws Exception {
        int databaseSizeBeforeCreate = typeIndemnisationsRepository.findAll().size();

        // Create the TypeIndemnisations
        TypeIndemnisationsDTO typeIndemnisationsDTO = typeIndemnisationsMapper.toDto(typeIndemnisations);
        restTypeIndemnisationsMockMvc.perform(post("/api/type-indemnisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeIndemnisationsDTO)))
            .andExpect(status().isCreated());

        // Validate the TypeIndemnisations in the database
        List<TypeIndemnisations> typeIndemnisationsList = typeIndemnisationsRepository.findAll();
        assertThat(typeIndemnisationsList).hasSize(databaseSizeBeforeCreate + 1);
        TypeIndemnisations testTypeIndemnisations = typeIndemnisationsList.get(typeIndemnisationsList.size() - 1);
        assertThat(testTypeIndemnisations.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createTypeIndemnisationsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeIndemnisationsRepository.findAll().size();

        // Create the TypeIndemnisations with an existing ID
        typeIndemnisations.setId(1L);
        TypeIndemnisationsDTO typeIndemnisationsDTO = typeIndemnisationsMapper.toDto(typeIndemnisations);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeIndemnisationsMockMvc.perform(post("/api/type-indemnisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeIndemnisationsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeIndemnisations in the database
        List<TypeIndemnisations> typeIndemnisationsList = typeIndemnisationsRepository.findAll();
        assertThat(typeIndemnisationsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTypeIndemnisations() throws Exception {
        // Initialize the database
        typeIndemnisationsRepository.saveAndFlush(typeIndemnisations);

        // Get all the typeIndemnisationsList
        restTypeIndemnisationsMockMvc.perform(get("/api/type-indemnisations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeIndemnisations.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getTypeIndemnisations() throws Exception {
        // Initialize the database
        typeIndemnisationsRepository.saveAndFlush(typeIndemnisations);

        // Get the typeIndemnisations
        restTypeIndemnisationsMockMvc.perform(get("/api/type-indemnisations/{id}", typeIndemnisations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(typeIndemnisations.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingTypeIndemnisations() throws Exception {
        // Get the typeIndemnisations
        restTypeIndemnisationsMockMvc.perform(get("/api/type-indemnisations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeIndemnisations() throws Exception {
        // Initialize the database
        typeIndemnisationsRepository.saveAndFlush(typeIndemnisations);

        int databaseSizeBeforeUpdate = typeIndemnisationsRepository.findAll().size();

        // Update the typeIndemnisations
        TypeIndemnisations updatedTypeIndemnisations = typeIndemnisationsRepository.findById(typeIndemnisations.getId()).get();
        // Disconnect from session so that the updates on updatedTypeIndemnisations are not directly saved in db
        em.detach(updatedTypeIndemnisations);
        updatedTypeIndemnisations
            .name(UPDATED_NAME);
        TypeIndemnisationsDTO typeIndemnisationsDTO = typeIndemnisationsMapper.toDto(updatedTypeIndemnisations);

        restTypeIndemnisationsMockMvc.perform(put("/api/type-indemnisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeIndemnisationsDTO)))
            .andExpect(status().isOk());

        // Validate the TypeIndemnisations in the database
        List<TypeIndemnisations> typeIndemnisationsList = typeIndemnisationsRepository.findAll();
        assertThat(typeIndemnisationsList).hasSize(databaseSizeBeforeUpdate);
        TypeIndemnisations testTypeIndemnisations = typeIndemnisationsList.get(typeIndemnisationsList.size() - 1);
        assertThat(testTypeIndemnisations.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeIndemnisations() throws Exception {
        int databaseSizeBeforeUpdate = typeIndemnisationsRepository.findAll().size();

        // Create the TypeIndemnisations
        TypeIndemnisationsDTO typeIndemnisationsDTO = typeIndemnisationsMapper.toDto(typeIndemnisations);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeIndemnisationsMockMvc.perform(put("/api/type-indemnisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeIndemnisationsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeIndemnisations in the database
        List<TypeIndemnisations> typeIndemnisationsList = typeIndemnisationsRepository.findAll();
        assertThat(typeIndemnisationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTypeIndemnisations() throws Exception {
        // Initialize the database
        typeIndemnisationsRepository.saveAndFlush(typeIndemnisations);

        int databaseSizeBeforeDelete = typeIndemnisationsRepository.findAll().size();

        // Delete the typeIndemnisations
        restTypeIndemnisationsMockMvc.perform(delete("/api/type-indemnisations/{id}", typeIndemnisations.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeIndemnisations> typeIndemnisationsList = typeIndemnisationsRepository.findAll();
        assertThat(typeIndemnisationsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeIndemnisations.class);
        TypeIndemnisations typeIndemnisations1 = new TypeIndemnisations();
        typeIndemnisations1.setId(1L);
        TypeIndemnisations typeIndemnisations2 = new TypeIndemnisations();
        typeIndemnisations2.setId(typeIndemnisations1.getId());
        assertThat(typeIndemnisations1).isEqualTo(typeIndemnisations2);
        typeIndemnisations2.setId(2L);
        assertThat(typeIndemnisations1).isNotEqualTo(typeIndemnisations2);
        typeIndemnisations1.setId(null);
        assertThat(typeIndemnisations1).isNotEqualTo(typeIndemnisations2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeIndemnisationsDTO.class);
        TypeIndemnisationsDTO typeIndemnisationsDTO1 = new TypeIndemnisationsDTO();
        typeIndemnisationsDTO1.setId(1L);
        TypeIndemnisationsDTO typeIndemnisationsDTO2 = new TypeIndemnisationsDTO();
        assertThat(typeIndemnisationsDTO1).isNotEqualTo(typeIndemnisationsDTO2);
        typeIndemnisationsDTO2.setId(typeIndemnisationsDTO1.getId());
        assertThat(typeIndemnisationsDTO1).isEqualTo(typeIndemnisationsDTO2);
        typeIndemnisationsDTO2.setId(2L);
        assertThat(typeIndemnisationsDTO1).isNotEqualTo(typeIndemnisationsDTO2);
        typeIndemnisationsDTO1.setId(null);
        assertThat(typeIndemnisationsDTO1).isNotEqualTo(typeIndemnisationsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(typeIndemnisationsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(typeIndemnisationsMapper.fromId(null)).isNull();
    }
}
