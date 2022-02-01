package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.PlanpassationmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.SygTypeService;
import sn.ssi.sigmap.repository.SygTypeServiceRepository;
import sn.ssi.sigmap.service.SygTypeServiceService;
import sn.ssi.sigmap.service.dto.SygTypeServiceDTO;
import sn.ssi.sigmap.service.mapper.SygTypeServiceMapper;
import sn.ssi.sigmap.service.dto.SygTypeServiceCriteria;
import sn.ssi.sigmap.service.SygTypeServiceQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SygTypeServiceResource} REST controller.
 */
@SpringBootTest(classes = { PlanpassationmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SygTypeServiceResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private SygTypeServiceRepository sygTypeServiceRepository;

    @Autowired
    private SygTypeServiceMapper sygTypeServiceMapper;

    @Autowired
    private SygTypeServiceService sygTypeServiceService;

    @Autowired
    private SygTypeServiceQueryService sygTypeServiceQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSygTypeServiceMockMvc;

    private SygTypeService sygTypeService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SygTypeService createEntity(EntityManager em) {
        SygTypeService sygTypeService = new SygTypeService()
            .libelle(DEFAULT_LIBELLE);
        return sygTypeService;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SygTypeService createUpdatedEntity(EntityManager em) {
        SygTypeService sygTypeService = new SygTypeService()
            .libelle(UPDATED_LIBELLE);
        return sygTypeService;
    }

    @BeforeEach
    public void initTest() {
        sygTypeService = createEntity(em);
    }

    @Test
    @Transactional
    public void createSygTypeService() throws Exception {
        int databaseSizeBeforeCreate = sygTypeServiceRepository.findAll().size();
        // Create the SygTypeService
        SygTypeServiceDTO sygTypeServiceDTO = sygTypeServiceMapper.toDto(sygTypeService);
        restSygTypeServiceMockMvc.perform(post("/api/syg-type-services").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygTypeServiceDTO)))
            .andExpect(status().isCreated());

        // Validate the SygTypeService in the database
        List<SygTypeService> sygTypeServiceList = sygTypeServiceRepository.findAll();
        assertThat(sygTypeServiceList).hasSize(databaseSizeBeforeCreate + 1);
        SygTypeService testSygTypeService = sygTypeServiceList.get(sygTypeServiceList.size() - 1);
        assertThat(testSygTypeService.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createSygTypeServiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sygTypeServiceRepository.findAll().size();

        // Create the SygTypeService with an existing ID
        sygTypeService.setId(1L);
        SygTypeServiceDTO sygTypeServiceDTO = sygTypeServiceMapper.toDto(sygTypeService);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSygTypeServiceMockMvc.perform(post("/api/syg-type-services").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygTypeServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SygTypeService in the database
        List<SygTypeService> sygTypeServiceList = sygTypeServiceRepository.findAll();
        assertThat(sygTypeServiceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sygTypeServiceRepository.findAll().size();
        // set the field null
        sygTypeService.setLibelle(null);

        // Create the SygTypeService, which fails.
        SygTypeServiceDTO sygTypeServiceDTO = sygTypeServiceMapper.toDto(sygTypeService);


        restSygTypeServiceMockMvc.perform(post("/api/syg-type-services").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygTypeServiceDTO)))
            .andExpect(status().isBadRequest());

        List<SygTypeService> sygTypeServiceList = sygTypeServiceRepository.findAll();
        assertThat(sygTypeServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSygTypeServices() throws Exception {
        // Initialize the database
        sygTypeServiceRepository.saveAndFlush(sygTypeService);

        // Get all the sygTypeServiceList
        restSygTypeServiceMockMvc.perform(get("/api/syg-type-services?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sygTypeService.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
    
    @Test
    @Transactional
    public void getSygTypeService() throws Exception {
        // Initialize the database
        sygTypeServiceRepository.saveAndFlush(sygTypeService);

        // Get the sygTypeService
        restSygTypeServiceMockMvc.perform(get("/api/syg-type-services/{id}", sygTypeService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sygTypeService.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }


    @Test
    @Transactional
    public void getSygTypeServicesByIdFiltering() throws Exception {
        // Initialize the database
        sygTypeServiceRepository.saveAndFlush(sygTypeService);

        Long id = sygTypeService.getId();

        defaultSygTypeServiceShouldBeFound("id.equals=" + id);
        defaultSygTypeServiceShouldNotBeFound("id.notEquals=" + id);

        defaultSygTypeServiceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSygTypeServiceShouldNotBeFound("id.greaterThan=" + id);

        defaultSygTypeServiceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSygTypeServiceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSygTypeServicesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        sygTypeServiceRepository.saveAndFlush(sygTypeService);

        // Get all the sygTypeServiceList where libelle equals to DEFAULT_LIBELLE
        defaultSygTypeServiceShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the sygTypeServiceList where libelle equals to UPDATED_LIBELLE
        defaultSygTypeServiceShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygTypeServicesByLibelleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sygTypeServiceRepository.saveAndFlush(sygTypeService);

        // Get all the sygTypeServiceList where libelle not equals to DEFAULT_LIBELLE
        defaultSygTypeServiceShouldNotBeFound("libelle.notEquals=" + DEFAULT_LIBELLE);

        // Get all the sygTypeServiceList where libelle not equals to UPDATED_LIBELLE
        defaultSygTypeServiceShouldBeFound("libelle.notEquals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygTypeServicesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        sygTypeServiceRepository.saveAndFlush(sygTypeService);

        // Get all the sygTypeServiceList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultSygTypeServiceShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the sygTypeServiceList where libelle equals to UPDATED_LIBELLE
        defaultSygTypeServiceShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygTypeServicesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        sygTypeServiceRepository.saveAndFlush(sygTypeService);

        // Get all the sygTypeServiceList where libelle is not null
        defaultSygTypeServiceShouldBeFound("libelle.specified=true");

        // Get all the sygTypeServiceList where libelle is null
        defaultSygTypeServiceShouldNotBeFound("libelle.specified=false");
    }
                @Test
    @Transactional
    public void getAllSygTypeServicesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        sygTypeServiceRepository.saveAndFlush(sygTypeService);

        // Get all the sygTypeServiceList where libelle contains DEFAULT_LIBELLE
        defaultSygTypeServiceShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the sygTypeServiceList where libelle contains UPDATED_LIBELLE
        defaultSygTypeServiceShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygTypeServicesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        sygTypeServiceRepository.saveAndFlush(sygTypeService);

        // Get all the sygTypeServiceList where libelle does not contain DEFAULT_LIBELLE
        defaultSygTypeServiceShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the sygTypeServiceList where libelle does not contain UPDATED_LIBELLE
        defaultSygTypeServiceShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSygTypeServiceShouldBeFound(String filter) throws Exception {
        restSygTypeServiceMockMvc.perform(get("/api/syg-type-services?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sygTypeService.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));

        // Check, that the count call also returns 1
        restSygTypeServiceMockMvc.perform(get("/api/syg-type-services/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSygTypeServiceShouldNotBeFound(String filter) throws Exception {
        restSygTypeServiceMockMvc.perform(get("/api/syg-type-services?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSygTypeServiceMockMvc.perform(get("/api/syg-type-services/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSygTypeService() throws Exception {
        // Get the sygTypeService
        restSygTypeServiceMockMvc.perform(get("/api/syg-type-services/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSygTypeService() throws Exception {
        // Initialize the database
        sygTypeServiceRepository.saveAndFlush(sygTypeService);

        int databaseSizeBeforeUpdate = sygTypeServiceRepository.findAll().size();

        // Update the sygTypeService
        SygTypeService updatedSygTypeService = sygTypeServiceRepository.findById(sygTypeService.getId()).get();
        // Disconnect from session so that the updates on updatedSygTypeService are not directly saved in db
        em.detach(updatedSygTypeService);
        updatedSygTypeService
            .libelle(UPDATED_LIBELLE);
        SygTypeServiceDTO sygTypeServiceDTO = sygTypeServiceMapper.toDto(updatedSygTypeService);

        restSygTypeServiceMockMvc.perform(put("/api/syg-type-services").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygTypeServiceDTO)))
            .andExpect(status().isOk());

        // Validate the SygTypeService in the database
        List<SygTypeService> sygTypeServiceList = sygTypeServiceRepository.findAll();
        assertThat(sygTypeServiceList).hasSize(databaseSizeBeforeUpdate);
        SygTypeService testSygTypeService = sygTypeServiceList.get(sygTypeServiceList.size() - 1);
        assertThat(testSygTypeService.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void updateNonExistingSygTypeService() throws Exception {
        int databaseSizeBeforeUpdate = sygTypeServiceRepository.findAll().size();

        // Create the SygTypeService
        SygTypeServiceDTO sygTypeServiceDTO = sygTypeServiceMapper.toDto(sygTypeService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSygTypeServiceMockMvc.perform(put("/api/syg-type-services").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygTypeServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SygTypeService in the database
        List<SygTypeService> sygTypeServiceList = sygTypeServiceRepository.findAll();
        assertThat(sygTypeServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSygTypeService() throws Exception {
        // Initialize the database
        sygTypeServiceRepository.saveAndFlush(sygTypeService);

        int databaseSizeBeforeDelete = sygTypeServiceRepository.findAll().size();

        // Delete the sygTypeService
        restSygTypeServiceMockMvc.perform(delete("/api/syg-type-services/{id}", sygTypeService.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SygTypeService> sygTypeServiceList = sygTypeServiceRepository.findAll();
        assertThat(sygTypeServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
