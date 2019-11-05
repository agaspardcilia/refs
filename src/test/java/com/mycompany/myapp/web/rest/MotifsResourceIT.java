package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RefsApp;
import com.mycompany.myapp.domain.Motifs;
import com.mycompany.myapp.repository.MotifsRepository;
import com.mycompany.myapp.service.MotifsService;
import com.mycompany.myapp.service.dto.MotifsDTO;
import com.mycompany.myapp.service.mapper.MotifsMapper;
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
 * Integration tests for the {@link MotifsResource} REST controller.
 */
@SpringBootTest(classes = RefsApp.class)
public class MotifsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private MotifsRepository motifsRepository;

    @Autowired
    private MotifsMapper motifsMapper;

    @Autowired
    private MotifsService motifsService;

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

    private MockMvc restMotifsMockMvc;

    private Motifs motifs;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MotifsResource motifsResource = new MotifsResource(motifsService);
        this.restMotifsMockMvc = MockMvcBuilders.standaloneSetup(motifsResource)
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
    public static Motifs createEntity(EntityManager em) {
        Motifs motifs = new Motifs()
            .name(DEFAULT_NAME);
        return motifs;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Motifs createUpdatedEntity(EntityManager em) {
        Motifs motifs = new Motifs()
            .name(UPDATED_NAME);
        return motifs;
    }

    @BeforeEach
    public void initTest() {
        motifs = createEntity(em);
    }

    @Test
    @Transactional
    public void createMotifs() throws Exception {
        int databaseSizeBeforeCreate = motifsRepository.findAll().size();

        // Create the Motifs
        MotifsDTO motifsDTO = motifsMapper.toDto(motifs);
        restMotifsMockMvc.perform(post("/api/motifs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motifsDTO)))
            .andExpect(status().isCreated());

        // Validate the Motifs in the database
        List<Motifs> motifsList = motifsRepository.findAll();
        assertThat(motifsList).hasSize(databaseSizeBeforeCreate + 1);
        Motifs testMotifs = motifsList.get(motifsList.size() - 1);
        assertThat(testMotifs.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createMotifsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = motifsRepository.findAll().size();

        // Create the Motifs with an existing ID
        motifs.setId(1L);
        MotifsDTO motifsDTO = motifsMapper.toDto(motifs);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMotifsMockMvc.perform(post("/api/motifs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motifsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Motifs in the database
        List<Motifs> motifsList = motifsRepository.findAll();
        assertThat(motifsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMotifs() throws Exception {
        // Initialize the database
        motifsRepository.saveAndFlush(motifs);

        // Get all the motifsList
        restMotifsMockMvc.perform(get("/api/motifs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motifs.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getMotifs() throws Exception {
        // Initialize the database
        motifsRepository.saveAndFlush(motifs);

        // Get the motifs
        restMotifsMockMvc.perform(get("/api/motifs/{id}", motifs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(motifs.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingMotifs() throws Exception {
        // Get the motifs
        restMotifsMockMvc.perform(get("/api/motifs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMotifs() throws Exception {
        // Initialize the database
        motifsRepository.saveAndFlush(motifs);

        int databaseSizeBeforeUpdate = motifsRepository.findAll().size();

        // Update the motifs
        Motifs updatedMotifs = motifsRepository.findById(motifs.getId()).get();
        // Disconnect from session so that the updates on updatedMotifs are not directly saved in db
        em.detach(updatedMotifs);
        updatedMotifs
            .name(UPDATED_NAME);
        MotifsDTO motifsDTO = motifsMapper.toDto(updatedMotifs);

        restMotifsMockMvc.perform(put("/api/motifs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motifsDTO)))
            .andExpect(status().isOk());

        // Validate the Motifs in the database
        List<Motifs> motifsList = motifsRepository.findAll();
        assertThat(motifsList).hasSize(databaseSizeBeforeUpdate);
        Motifs testMotifs = motifsList.get(motifsList.size() - 1);
        assertThat(testMotifs.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingMotifs() throws Exception {
        int databaseSizeBeforeUpdate = motifsRepository.findAll().size();

        // Create the Motifs
        MotifsDTO motifsDTO = motifsMapper.toDto(motifs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMotifsMockMvc.perform(put("/api/motifs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motifsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Motifs in the database
        List<Motifs> motifsList = motifsRepository.findAll();
        assertThat(motifsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMotifs() throws Exception {
        // Initialize the database
        motifsRepository.saveAndFlush(motifs);

        int databaseSizeBeforeDelete = motifsRepository.findAll().size();

        // Delete the motifs
        restMotifsMockMvc.perform(delete("/api/motifs/{id}", motifs.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Motifs> motifsList = motifsRepository.findAll();
        assertThat(motifsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Motifs.class);
        Motifs motifs1 = new Motifs();
        motifs1.setId(1L);
        Motifs motifs2 = new Motifs();
        motifs2.setId(motifs1.getId());
        assertThat(motifs1).isEqualTo(motifs2);
        motifs2.setId(2L);
        assertThat(motifs1).isNotEqualTo(motifs2);
        motifs1.setId(null);
        assertThat(motifs1).isNotEqualTo(motifs2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MotifsDTO.class);
        MotifsDTO motifsDTO1 = new MotifsDTO();
        motifsDTO1.setId(1L);
        MotifsDTO motifsDTO2 = new MotifsDTO();
        assertThat(motifsDTO1).isNotEqualTo(motifsDTO2);
        motifsDTO2.setId(motifsDTO1.getId());
        assertThat(motifsDTO1).isEqualTo(motifsDTO2);
        motifsDTO2.setId(2L);
        assertThat(motifsDTO1).isNotEqualTo(motifsDTO2);
        motifsDTO1.setId(null);
        assertThat(motifsDTO1).isNotEqualTo(motifsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(motifsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(motifsMapper.fromId(null)).isNull();
    }
}
