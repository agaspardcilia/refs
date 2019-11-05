package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RefsApp;
import com.mycompany.myapp.domain.CourtierPartenaireDelegataires;
import com.mycompany.myapp.repository.CourtierPartenaireDelegatairesRepository;
import com.mycompany.myapp.service.CourtierPartenaireDelegatairesService;
import com.mycompany.myapp.service.dto.CourtierPartenaireDelegatairesDTO;
import com.mycompany.myapp.service.mapper.CourtierPartenaireDelegatairesMapper;
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
 * Integration tests for the {@link CourtierPartenaireDelegatairesResource} REST controller.
 */
@SpringBootTest(classes = RefsApp.class)
public class CourtierPartenaireDelegatairesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CourtierPartenaireDelegatairesRepository courtierPartenaireDelegatairesRepository;

    @Autowired
    private CourtierPartenaireDelegatairesMapper courtierPartenaireDelegatairesMapper;

    @Autowired
    private CourtierPartenaireDelegatairesService courtierPartenaireDelegatairesService;

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

    private MockMvc restCourtierPartenaireDelegatairesMockMvc;

    private CourtierPartenaireDelegataires courtierPartenaireDelegataires;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourtierPartenaireDelegatairesResource courtierPartenaireDelegatairesResource = new CourtierPartenaireDelegatairesResource(courtierPartenaireDelegatairesService);
        this.restCourtierPartenaireDelegatairesMockMvc = MockMvcBuilders.standaloneSetup(courtierPartenaireDelegatairesResource)
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
    public static CourtierPartenaireDelegataires createEntity(EntityManager em) {
        CourtierPartenaireDelegataires courtierPartenaireDelegataires = new CourtierPartenaireDelegataires()
            .name(DEFAULT_NAME);
        return courtierPartenaireDelegataires;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourtierPartenaireDelegataires createUpdatedEntity(EntityManager em) {
        CourtierPartenaireDelegataires courtierPartenaireDelegataires = new CourtierPartenaireDelegataires()
            .name(UPDATED_NAME);
        return courtierPartenaireDelegataires;
    }

    @BeforeEach
    public void initTest() {
        courtierPartenaireDelegataires = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourtierPartenaireDelegataires() throws Exception {
        int databaseSizeBeforeCreate = courtierPartenaireDelegatairesRepository.findAll().size();

        // Create the CourtierPartenaireDelegataires
        CourtierPartenaireDelegatairesDTO courtierPartenaireDelegatairesDTO = courtierPartenaireDelegatairesMapper.toDto(courtierPartenaireDelegataires);
        restCourtierPartenaireDelegatairesMockMvc.perform(post("/api/courtier-partenaire-delegataires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courtierPartenaireDelegatairesDTO)))
            .andExpect(status().isCreated());

        // Validate the CourtierPartenaireDelegataires in the database
        List<CourtierPartenaireDelegataires> courtierPartenaireDelegatairesList = courtierPartenaireDelegatairesRepository.findAll();
        assertThat(courtierPartenaireDelegatairesList).hasSize(databaseSizeBeforeCreate + 1);
        CourtierPartenaireDelegataires testCourtierPartenaireDelegataires = courtierPartenaireDelegatairesList.get(courtierPartenaireDelegatairesList.size() - 1);
        assertThat(testCourtierPartenaireDelegataires.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCourtierPartenaireDelegatairesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courtierPartenaireDelegatairesRepository.findAll().size();

        // Create the CourtierPartenaireDelegataires with an existing ID
        courtierPartenaireDelegataires.setId(1L);
        CourtierPartenaireDelegatairesDTO courtierPartenaireDelegatairesDTO = courtierPartenaireDelegatairesMapper.toDto(courtierPartenaireDelegataires);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourtierPartenaireDelegatairesMockMvc.perform(post("/api/courtier-partenaire-delegataires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courtierPartenaireDelegatairesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourtierPartenaireDelegataires in the database
        List<CourtierPartenaireDelegataires> courtierPartenaireDelegatairesList = courtierPartenaireDelegatairesRepository.findAll();
        assertThat(courtierPartenaireDelegatairesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCourtierPartenaireDelegataires() throws Exception {
        // Initialize the database
        courtierPartenaireDelegatairesRepository.saveAndFlush(courtierPartenaireDelegataires);

        // Get all the courtierPartenaireDelegatairesList
        restCourtierPartenaireDelegatairesMockMvc.perform(get("/api/courtier-partenaire-delegataires?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courtierPartenaireDelegataires.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getCourtierPartenaireDelegataires() throws Exception {
        // Initialize the database
        courtierPartenaireDelegatairesRepository.saveAndFlush(courtierPartenaireDelegataires);

        // Get the courtierPartenaireDelegataires
        restCourtierPartenaireDelegatairesMockMvc.perform(get("/api/courtier-partenaire-delegataires/{id}", courtierPartenaireDelegataires.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(courtierPartenaireDelegataires.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingCourtierPartenaireDelegataires() throws Exception {
        // Get the courtierPartenaireDelegataires
        restCourtierPartenaireDelegatairesMockMvc.perform(get("/api/courtier-partenaire-delegataires/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourtierPartenaireDelegataires() throws Exception {
        // Initialize the database
        courtierPartenaireDelegatairesRepository.saveAndFlush(courtierPartenaireDelegataires);

        int databaseSizeBeforeUpdate = courtierPartenaireDelegatairesRepository.findAll().size();

        // Update the courtierPartenaireDelegataires
        CourtierPartenaireDelegataires updatedCourtierPartenaireDelegataires = courtierPartenaireDelegatairesRepository.findById(courtierPartenaireDelegataires.getId()).get();
        // Disconnect from session so that the updates on updatedCourtierPartenaireDelegataires are not directly saved in db
        em.detach(updatedCourtierPartenaireDelegataires);
        updatedCourtierPartenaireDelegataires
            .name(UPDATED_NAME);
        CourtierPartenaireDelegatairesDTO courtierPartenaireDelegatairesDTO = courtierPartenaireDelegatairesMapper.toDto(updatedCourtierPartenaireDelegataires);

        restCourtierPartenaireDelegatairesMockMvc.perform(put("/api/courtier-partenaire-delegataires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courtierPartenaireDelegatairesDTO)))
            .andExpect(status().isOk());

        // Validate the CourtierPartenaireDelegataires in the database
        List<CourtierPartenaireDelegataires> courtierPartenaireDelegatairesList = courtierPartenaireDelegatairesRepository.findAll();
        assertThat(courtierPartenaireDelegatairesList).hasSize(databaseSizeBeforeUpdate);
        CourtierPartenaireDelegataires testCourtierPartenaireDelegataires = courtierPartenaireDelegatairesList.get(courtierPartenaireDelegatairesList.size() - 1);
        assertThat(testCourtierPartenaireDelegataires.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCourtierPartenaireDelegataires() throws Exception {
        int databaseSizeBeforeUpdate = courtierPartenaireDelegatairesRepository.findAll().size();

        // Create the CourtierPartenaireDelegataires
        CourtierPartenaireDelegatairesDTO courtierPartenaireDelegatairesDTO = courtierPartenaireDelegatairesMapper.toDto(courtierPartenaireDelegataires);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourtierPartenaireDelegatairesMockMvc.perform(put("/api/courtier-partenaire-delegataires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courtierPartenaireDelegatairesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourtierPartenaireDelegataires in the database
        List<CourtierPartenaireDelegataires> courtierPartenaireDelegatairesList = courtierPartenaireDelegatairesRepository.findAll();
        assertThat(courtierPartenaireDelegatairesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourtierPartenaireDelegataires() throws Exception {
        // Initialize the database
        courtierPartenaireDelegatairesRepository.saveAndFlush(courtierPartenaireDelegataires);

        int databaseSizeBeforeDelete = courtierPartenaireDelegatairesRepository.findAll().size();

        // Delete the courtierPartenaireDelegataires
        restCourtierPartenaireDelegatairesMockMvc.perform(delete("/api/courtier-partenaire-delegataires/{id}", courtierPartenaireDelegataires.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourtierPartenaireDelegataires> courtierPartenaireDelegatairesList = courtierPartenaireDelegatairesRepository.findAll();
        assertThat(courtierPartenaireDelegatairesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourtierPartenaireDelegataires.class);
        CourtierPartenaireDelegataires courtierPartenaireDelegataires1 = new CourtierPartenaireDelegataires();
        courtierPartenaireDelegataires1.setId(1L);
        CourtierPartenaireDelegataires courtierPartenaireDelegataires2 = new CourtierPartenaireDelegataires();
        courtierPartenaireDelegataires2.setId(courtierPartenaireDelegataires1.getId());
        assertThat(courtierPartenaireDelegataires1).isEqualTo(courtierPartenaireDelegataires2);
        courtierPartenaireDelegataires2.setId(2L);
        assertThat(courtierPartenaireDelegataires1).isNotEqualTo(courtierPartenaireDelegataires2);
        courtierPartenaireDelegataires1.setId(null);
        assertThat(courtierPartenaireDelegataires1).isNotEqualTo(courtierPartenaireDelegataires2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourtierPartenaireDelegatairesDTO.class);
        CourtierPartenaireDelegatairesDTO courtierPartenaireDelegatairesDTO1 = new CourtierPartenaireDelegatairesDTO();
        courtierPartenaireDelegatairesDTO1.setId(1L);
        CourtierPartenaireDelegatairesDTO courtierPartenaireDelegatairesDTO2 = new CourtierPartenaireDelegatairesDTO();
        assertThat(courtierPartenaireDelegatairesDTO1).isNotEqualTo(courtierPartenaireDelegatairesDTO2);
        courtierPartenaireDelegatairesDTO2.setId(courtierPartenaireDelegatairesDTO1.getId());
        assertThat(courtierPartenaireDelegatairesDTO1).isEqualTo(courtierPartenaireDelegatairesDTO2);
        courtierPartenaireDelegatairesDTO2.setId(2L);
        assertThat(courtierPartenaireDelegatairesDTO1).isNotEqualTo(courtierPartenaireDelegatairesDTO2);
        courtierPartenaireDelegatairesDTO1.setId(null);
        assertThat(courtierPartenaireDelegatairesDTO1).isNotEqualTo(courtierPartenaireDelegatairesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(courtierPartenaireDelegatairesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(courtierPartenaireDelegatairesMapper.fromId(null)).isNull();
    }
}
