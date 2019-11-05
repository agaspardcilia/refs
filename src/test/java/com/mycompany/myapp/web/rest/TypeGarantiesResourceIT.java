package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RefsApp;
import com.mycompany.myapp.domain.TypeGaranties;
import com.mycompany.myapp.repository.TypeGarantiesRepository;
import com.mycompany.myapp.service.TypeGarantiesService;
import com.mycompany.myapp.service.dto.TypeGarantiesDTO;
import com.mycompany.myapp.service.mapper.TypeGarantiesMapper;
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
 * Integration tests for the {@link TypeGarantiesResource} REST controller.
 */
@SpringBootTest(classes = RefsApp.class)
public class TypeGarantiesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private TypeGarantiesRepository typeGarantiesRepository;

    @Autowired
    private TypeGarantiesMapper typeGarantiesMapper;

    @Autowired
    private TypeGarantiesService typeGarantiesService;

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

    private MockMvc restTypeGarantiesMockMvc;

    private TypeGaranties typeGaranties;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TypeGarantiesResource typeGarantiesResource = new TypeGarantiesResource(typeGarantiesService);
        this.restTypeGarantiesMockMvc = MockMvcBuilders.standaloneSetup(typeGarantiesResource)
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
    public static TypeGaranties createEntity(EntityManager em) {
        TypeGaranties typeGaranties = new TypeGaranties()
            .name(DEFAULT_NAME);
        return typeGaranties;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeGaranties createUpdatedEntity(EntityManager em) {
        TypeGaranties typeGaranties = new TypeGaranties()
            .name(UPDATED_NAME);
        return typeGaranties;
    }

    @BeforeEach
    public void initTest() {
        typeGaranties = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeGaranties() throws Exception {
        int databaseSizeBeforeCreate = typeGarantiesRepository.findAll().size();

        // Create the TypeGaranties
        TypeGarantiesDTO typeGarantiesDTO = typeGarantiesMapper.toDto(typeGaranties);
        restTypeGarantiesMockMvc.perform(post("/api/type-garanties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeGarantiesDTO)))
            .andExpect(status().isCreated());

        // Validate the TypeGaranties in the database
        List<TypeGaranties> typeGarantiesList = typeGarantiesRepository.findAll();
        assertThat(typeGarantiesList).hasSize(databaseSizeBeforeCreate + 1);
        TypeGaranties testTypeGaranties = typeGarantiesList.get(typeGarantiesList.size() - 1);
        assertThat(testTypeGaranties.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createTypeGarantiesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeGarantiesRepository.findAll().size();

        // Create the TypeGaranties with an existing ID
        typeGaranties.setId(1L);
        TypeGarantiesDTO typeGarantiesDTO = typeGarantiesMapper.toDto(typeGaranties);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeGarantiesMockMvc.perform(post("/api/type-garanties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeGarantiesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeGaranties in the database
        List<TypeGaranties> typeGarantiesList = typeGarantiesRepository.findAll();
        assertThat(typeGarantiesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTypeGaranties() throws Exception {
        // Initialize the database
        typeGarantiesRepository.saveAndFlush(typeGaranties);

        // Get all the typeGarantiesList
        restTypeGarantiesMockMvc.perform(get("/api/type-garanties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeGaranties.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getTypeGaranties() throws Exception {
        // Initialize the database
        typeGarantiesRepository.saveAndFlush(typeGaranties);

        // Get the typeGaranties
        restTypeGarantiesMockMvc.perform(get("/api/type-garanties/{id}", typeGaranties.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(typeGaranties.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingTypeGaranties() throws Exception {
        // Get the typeGaranties
        restTypeGarantiesMockMvc.perform(get("/api/type-garanties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeGaranties() throws Exception {
        // Initialize the database
        typeGarantiesRepository.saveAndFlush(typeGaranties);

        int databaseSizeBeforeUpdate = typeGarantiesRepository.findAll().size();

        // Update the typeGaranties
        TypeGaranties updatedTypeGaranties = typeGarantiesRepository.findById(typeGaranties.getId()).get();
        // Disconnect from session so that the updates on updatedTypeGaranties are not directly saved in db
        em.detach(updatedTypeGaranties);
        updatedTypeGaranties
            .name(UPDATED_NAME);
        TypeGarantiesDTO typeGarantiesDTO = typeGarantiesMapper.toDto(updatedTypeGaranties);

        restTypeGarantiesMockMvc.perform(put("/api/type-garanties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeGarantiesDTO)))
            .andExpect(status().isOk());

        // Validate the TypeGaranties in the database
        List<TypeGaranties> typeGarantiesList = typeGarantiesRepository.findAll();
        assertThat(typeGarantiesList).hasSize(databaseSizeBeforeUpdate);
        TypeGaranties testTypeGaranties = typeGarantiesList.get(typeGarantiesList.size() - 1);
        assertThat(testTypeGaranties.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeGaranties() throws Exception {
        int databaseSizeBeforeUpdate = typeGarantiesRepository.findAll().size();

        // Create the TypeGaranties
        TypeGarantiesDTO typeGarantiesDTO = typeGarantiesMapper.toDto(typeGaranties);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeGarantiesMockMvc.perform(put("/api/type-garanties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeGarantiesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeGaranties in the database
        List<TypeGaranties> typeGarantiesList = typeGarantiesRepository.findAll();
        assertThat(typeGarantiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTypeGaranties() throws Exception {
        // Initialize the database
        typeGarantiesRepository.saveAndFlush(typeGaranties);

        int databaseSizeBeforeDelete = typeGarantiesRepository.findAll().size();

        // Delete the typeGaranties
        restTypeGarantiesMockMvc.perform(delete("/api/type-garanties/{id}", typeGaranties.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeGaranties> typeGarantiesList = typeGarantiesRepository.findAll();
        assertThat(typeGarantiesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeGaranties.class);
        TypeGaranties typeGaranties1 = new TypeGaranties();
        typeGaranties1.setId(1L);
        TypeGaranties typeGaranties2 = new TypeGaranties();
        typeGaranties2.setId(typeGaranties1.getId());
        assertThat(typeGaranties1).isEqualTo(typeGaranties2);
        typeGaranties2.setId(2L);
        assertThat(typeGaranties1).isNotEqualTo(typeGaranties2);
        typeGaranties1.setId(null);
        assertThat(typeGaranties1).isNotEqualTo(typeGaranties2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeGarantiesDTO.class);
        TypeGarantiesDTO typeGarantiesDTO1 = new TypeGarantiesDTO();
        typeGarantiesDTO1.setId(1L);
        TypeGarantiesDTO typeGarantiesDTO2 = new TypeGarantiesDTO();
        assertThat(typeGarantiesDTO1).isNotEqualTo(typeGarantiesDTO2);
        typeGarantiesDTO2.setId(typeGarantiesDTO1.getId());
        assertThat(typeGarantiesDTO1).isEqualTo(typeGarantiesDTO2);
        typeGarantiesDTO2.setId(2L);
        assertThat(typeGarantiesDTO1).isNotEqualTo(typeGarantiesDTO2);
        typeGarantiesDTO1.setId(null);
        assertThat(typeGarantiesDTO1).isNotEqualTo(typeGarantiesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(typeGarantiesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(typeGarantiesMapper.fromId(null)).isNull();
    }
}
