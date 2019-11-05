package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.RefsApp;
import com.mycompany.myapp.domain.Support;
import com.mycompany.myapp.repository.SupportRepository;
import com.mycompany.myapp.service.SupportService;
import com.mycompany.myapp.service.dto.SupportDTO;
import com.mycompany.myapp.service.mapper.SupportMapper;
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
 * Integration tests for the {@link SupportResource} REST controller.
 */
@SpringBootTest(classes = RefsApp.class)
public class SupportResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private SupportRepository supportRepository;

    @Autowired
    private SupportMapper supportMapper;

    @Autowired
    private SupportService supportService;

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

    private MockMvc restSupportMockMvc;

    private Support support;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SupportResource supportResource = new SupportResource(supportService);
        this.restSupportMockMvc = MockMvcBuilders.standaloneSetup(supportResource)
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
    public static Support createEntity(EntityManager em) {
        Support support = new Support()
            .name(DEFAULT_NAME);
        return support;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Support createUpdatedEntity(EntityManager em) {
        Support support = new Support()
            .name(UPDATED_NAME);
        return support;
    }

    @BeforeEach
    public void initTest() {
        support = createEntity(em);
    }

    @Test
    @Transactional
    public void createSupport() throws Exception {
        int databaseSizeBeforeCreate = supportRepository.findAll().size();

        // Create the Support
        SupportDTO supportDTO = supportMapper.toDto(support);
        restSupportMockMvc.perform(post("/api/supports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportDTO)))
            .andExpect(status().isCreated());

        // Validate the Support in the database
        List<Support> supportList = supportRepository.findAll();
        assertThat(supportList).hasSize(databaseSizeBeforeCreate + 1);
        Support testSupport = supportList.get(supportList.size() - 1);
        assertThat(testSupport.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSupportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = supportRepository.findAll().size();

        // Create the Support with an existing ID
        support.setId(1L);
        SupportDTO supportDTO = supportMapper.toDto(support);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupportMockMvc.perform(post("/api/supports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Support in the database
        List<Support> supportList = supportRepository.findAll();
        assertThat(supportList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSupports() throws Exception {
        // Initialize the database
        supportRepository.saveAndFlush(support);

        // Get all the supportList
        restSupportMockMvc.perform(get("/api/supports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(support.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getSupport() throws Exception {
        // Initialize the database
        supportRepository.saveAndFlush(support);

        // Get the support
        restSupportMockMvc.perform(get("/api/supports/{id}", support.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(support.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingSupport() throws Exception {
        // Get the support
        restSupportMockMvc.perform(get("/api/supports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSupport() throws Exception {
        // Initialize the database
        supportRepository.saveAndFlush(support);

        int databaseSizeBeforeUpdate = supportRepository.findAll().size();

        // Update the support
        Support updatedSupport = supportRepository.findById(support.getId()).get();
        // Disconnect from session so that the updates on updatedSupport are not directly saved in db
        em.detach(updatedSupport);
        updatedSupport
            .name(UPDATED_NAME);
        SupportDTO supportDTO = supportMapper.toDto(updatedSupport);

        restSupportMockMvc.perform(put("/api/supports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportDTO)))
            .andExpect(status().isOk());

        // Validate the Support in the database
        List<Support> supportList = supportRepository.findAll();
        assertThat(supportList).hasSize(databaseSizeBeforeUpdate);
        Support testSupport = supportList.get(supportList.size() - 1);
        assertThat(testSupport.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingSupport() throws Exception {
        int databaseSizeBeforeUpdate = supportRepository.findAll().size();

        // Create the Support
        SupportDTO supportDTO = supportMapper.toDto(support);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupportMockMvc.perform(put("/api/supports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Support in the database
        List<Support> supportList = supportRepository.findAll();
        assertThat(supportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSupport() throws Exception {
        // Initialize the database
        supportRepository.saveAndFlush(support);

        int databaseSizeBeforeDelete = supportRepository.findAll().size();

        // Delete the support
        restSupportMockMvc.perform(delete("/api/supports/{id}", support.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Support> supportList = supportRepository.findAll();
        assertThat(supportList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Support.class);
        Support support1 = new Support();
        support1.setId(1L);
        Support support2 = new Support();
        support2.setId(support1.getId());
        assertThat(support1).isEqualTo(support2);
        support2.setId(2L);
        assertThat(support1).isNotEqualTo(support2);
        support1.setId(null);
        assertThat(support1).isNotEqualTo(support2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupportDTO.class);
        SupportDTO supportDTO1 = new SupportDTO();
        supportDTO1.setId(1L);
        SupportDTO supportDTO2 = new SupportDTO();
        assertThat(supportDTO1).isNotEqualTo(supportDTO2);
        supportDTO2.setId(supportDTO1.getId());
        assertThat(supportDTO1).isEqualTo(supportDTO2);
        supportDTO2.setId(2L);
        assertThat(supportDTO1).isNotEqualTo(supportDTO2);
        supportDTO1.setId(null);
        assertThat(supportDTO1).isNotEqualTo(supportDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(supportMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(supportMapper.fromId(null)).isNull();
    }
}
