package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RefsApp;
import com.mycompany.myapp.domain.TypeClients;
import com.mycompany.myapp.repository.TypeClientsRepository;
import com.mycompany.myapp.service.TypeClientsService;
import com.mycompany.myapp.service.dto.TypeClientsDTO;
import com.mycompany.myapp.service.mapper.TypeClientsMapper;
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
 * Integration tests for the {@link TypeClientsResource} REST controller.
 */
@SpringBootTest(classes = RefsApp.class)
public class TypeClientsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private TypeClientsRepository typeClientsRepository;

    @Autowired
    private TypeClientsMapper typeClientsMapper;

    @Autowired
    private TypeClientsService typeClientsService;

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

    private MockMvc restTypeClientsMockMvc;

    private TypeClients typeClients;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TypeClientsResource typeClientsResource = new TypeClientsResource(typeClientsService);
        this.restTypeClientsMockMvc = MockMvcBuilders.standaloneSetup(typeClientsResource)
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
    public static TypeClients createEntity(EntityManager em) {
        TypeClients typeClients = new TypeClients()
            .name(DEFAULT_NAME);
        return typeClients;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeClients createUpdatedEntity(EntityManager em) {
        TypeClients typeClients = new TypeClients()
            .name(UPDATED_NAME);
        return typeClients;
    }

    @BeforeEach
    public void initTest() {
        typeClients = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeClients() throws Exception {
        int databaseSizeBeforeCreate = typeClientsRepository.findAll().size();

        // Create the TypeClients
        TypeClientsDTO typeClientsDTO = typeClientsMapper.toDto(typeClients);
        restTypeClientsMockMvc.perform(post("/api/type-clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeClientsDTO)))
            .andExpect(status().isCreated());

        // Validate the TypeClients in the database
        List<TypeClients> typeClientsList = typeClientsRepository.findAll();
        assertThat(typeClientsList).hasSize(databaseSizeBeforeCreate + 1);
        TypeClients testTypeClients = typeClientsList.get(typeClientsList.size() - 1);
        assertThat(testTypeClients.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createTypeClientsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeClientsRepository.findAll().size();

        // Create the TypeClients with an existing ID
        typeClients.setId(1L);
        TypeClientsDTO typeClientsDTO = typeClientsMapper.toDto(typeClients);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeClientsMockMvc.perform(post("/api/type-clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeClientsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeClients in the database
        List<TypeClients> typeClientsList = typeClientsRepository.findAll();
        assertThat(typeClientsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTypeClients() throws Exception {
        // Initialize the database
        typeClientsRepository.saveAndFlush(typeClients);

        // Get all the typeClientsList
        restTypeClientsMockMvc.perform(get("/api/type-clients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeClients.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getTypeClients() throws Exception {
        // Initialize the database
        typeClientsRepository.saveAndFlush(typeClients);

        // Get the typeClients
        restTypeClientsMockMvc.perform(get("/api/type-clients/{id}", typeClients.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(typeClients.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingTypeClients() throws Exception {
        // Get the typeClients
        restTypeClientsMockMvc.perform(get("/api/type-clients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeClients() throws Exception {
        // Initialize the database
        typeClientsRepository.saveAndFlush(typeClients);

        int databaseSizeBeforeUpdate = typeClientsRepository.findAll().size();

        // Update the typeClients
        TypeClients updatedTypeClients = typeClientsRepository.findById(typeClients.getId()).get();
        // Disconnect from session so that the updates on updatedTypeClients are not directly saved in db
        em.detach(updatedTypeClients);
        updatedTypeClients
            .name(UPDATED_NAME);
        TypeClientsDTO typeClientsDTO = typeClientsMapper.toDto(updatedTypeClients);

        restTypeClientsMockMvc.perform(put("/api/type-clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeClientsDTO)))
            .andExpect(status().isOk());

        // Validate the TypeClients in the database
        List<TypeClients> typeClientsList = typeClientsRepository.findAll();
        assertThat(typeClientsList).hasSize(databaseSizeBeforeUpdate);
        TypeClients testTypeClients = typeClientsList.get(typeClientsList.size() - 1);
        assertThat(testTypeClients.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeClients() throws Exception {
        int databaseSizeBeforeUpdate = typeClientsRepository.findAll().size();

        // Create the TypeClients
        TypeClientsDTO typeClientsDTO = typeClientsMapper.toDto(typeClients);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeClientsMockMvc.perform(put("/api/type-clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeClientsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeClients in the database
        List<TypeClients> typeClientsList = typeClientsRepository.findAll();
        assertThat(typeClientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTypeClients() throws Exception {
        // Initialize the database
        typeClientsRepository.saveAndFlush(typeClients);

        int databaseSizeBeforeDelete = typeClientsRepository.findAll().size();

        // Delete the typeClients
        restTypeClientsMockMvc.perform(delete("/api/type-clients/{id}", typeClients.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeClients> typeClientsList = typeClientsRepository.findAll();
        assertThat(typeClientsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeClients.class);
        TypeClients typeClients1 = new TypeClients();
        typeClients1.setId(1L);
        TypeClients typeClients2 = new TypeClients();
        typeClients2.setId(typeClients1.getId());
        assertThat(typeClients1).isEqualTo(typeClients2);
        typeClients2.setId(2L);
        assertThat(typeClients1).isNotEqualTo(typeClients2);
        typeClients1.setId(null);
        assertThat(typeClients1).isNotEqualTo(typeClients2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeClientsDTO.class);
        TypeClientsDTO typeClientsDTO1 = new TypeClientsDTO();
        typeClientsDTO1.setId(1L);
        TypeClientsDTO typeClientsDTO2 = new TypeClientsDTO();
        assertThat(typeClientsDTO1).isNotEqualTo(typeClientsDTO2);
        typeClientsDTO2.setId(typeClientsDTO1.getId());
        assertThat(typeClientsDTO1).isEqualTo(typeClientsDTO2);
        typeClientsDTO2.setId(2L);
        assertThat(typeClientsDTO1).isNotEqualTo(typeClientsDTO2);
        typeClientsDTO1.setId(null);
        assertThat(typeClientsDTO1).isNotEqualTo(typeClientsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(typeClientsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(typeClientsMapper.fromId(null)).isNull();
    }
}
