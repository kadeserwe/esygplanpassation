package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.PlanpassationmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.SygTypeSourceFinancement;
import sn.ssi.sigmap.repository.SygTypeSourceFinancementRepository;
import sn.ssi.sigmap.service.SygTypeSourceFinancementService;
import sn.ssi.sigmap.service.dto.SygTypeSourceFinancementDTO;
import sn.ssi.sigmap.service.mapper.SygTypeSourceFinancementMapper;
import sn.ssi.sigmap.service.dto.SygTypeSourceFinancementCriteria;
import sn.ssi.sigmap.service.SygTypeSourceFinancementQueryService;

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
 * Integration tests for the {@link SygTypeSourceFinancementResource} REST controller.
 */
@SpringBootTest(classes = { PlanpassationmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SygTypeSourceFinancementResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private SygTypeSourceFinancementRepository sygTypeSourceFinancementRepository;

    @Autowired
    private SygTypeSourceFinancementMapper sygTypeSourceFinancementMapper;

    @Autowired
    private SygTypeSourceFinancementService sygTypeSourceFinancementService;

    @Autowired
    private SygTypeSourceFinancementQueryService sygTypeSourceFinancementQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSygTypeSourceFinancementMockMvc;

    private SygTypeSourceFinancement sygTypeSourceFinancement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SygTypeSourceFinancement createEntity(EntityManager em) {
        SygTypeSourceFinancement sygTypeSourceFinancement = new SygTypeSourceFinancement()
            .libelle(DEFAULT_LIBELLE);
        return sygTypeSourceFinancement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SygTypeSourceFinancement createUpdatedEntity(EntityManager em) {
        SygTypeSourceFinancement sygTypeSourceFinancement = new SygTypeSourceFinancement()
            .libelle(UPDATED_LIBELLE);
        return sygTypeSourceFinancement;
    }

    @BeforeEach
    public void initTest() {
        sygTypeSourceFinancement = createEntity(em);
    }

    @Test
    @Transactional
    public void createSygTypeSourceFinancement() throws Exception {
        int databaseSizeBeforeCreate = sygTypeSourceFinancementRepository.findAll().size();
        // Create the SygTypeSourceFinancement
        SygTypeSourceFinancementDTO sygTypeSourceFinancementDTO = sygTypeSourceFinancementMapper.toDto(sygTypeSourceFinancement);
        restSygTypeSourceFinancementMockMvc.perform(post("/api/syg-type-source-financements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygTypeSourceFinancementDTO)))
            .andExpect(status().isCreated());

        // Validate the SygTypeSourceFinancement in the database
        List<SygTypeSourceFinancement> sygTypeSourceFinancementList = sygTypeSourceFinancementRepository.findAll();
        assertThat(sygTypeSourceFinancementList).hasSize(databaseSizeBeforeCreate + 1);
        SygTypeSourceFinancement testSygTypeSourceFinancement = sygTypeSourceFinancementList.get(sygTypeSourceFinancementList.size() - 1);
        assertThat(testSygTypeSourceFinancement.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createSygTypeSourceFinancementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sygTypeSourceFinancementRepository.findAll().size();

        // Create the SygTypeSourceFinancement with an existing ID
        sygTypeSourceFinancement.setId(1L);
        SygTypeSourceFinancementDTO sygTypeSourceFinancementDTO = sygTypeSourceFinancementMapper.toDto(sygTypeSourceFinancement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSygTypeSourceFinancementMockMvc.perform(post("/api/syg-type-source-financements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygTypeSourceFinancementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SygTypeSourceFinancement in the database
        List<SygTypeSourceFinancement> sygTypeSourceFinancementList = sygTypeSourceFinancementRepository.findAll();
        assertThat(sygTypeSourceFinancementList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sygTypeSourceFinancementRepository.findAll().size();
        // set the field null
        sygTypeSourceFinancement.setLibelle(null);

        // Create the SygTypeSourceFinancement, which fails.
        SygTypeSourceFinancementDTO sygTypeSourceFinancementDTO = sygTypeSourceFinancementMapper.toDto(sygTypeSourceFinancement);


        restSygTypeSourceFinancementMockMvc.perform(post("/api/syg-type-source-financements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygTypeSourceFinancementDTO)))
            .andExpect(status().isBadRequest());

        List<SygTypeSourceFinancement> sygTypeSourceFinancementList = sygTypeSourceFinancementRepository.findAll();
        assertThat(sygTypeSourceFinancementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSygTypeSourceFinancements() throws Exception {
        // Initialize the database
        sygTypeSourceFinancementRepository.saveAndFlush(sygTypeSourceFinancement);

        // Get all the sygTypeSourceFinancementList
        restSygTypeSourceFinancementMockMvc.perform(get("/api/syg-type-source-financements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sygTypeSourceFinancement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
    
    @Test
    @Transactional
    public void getSygTypeSourceFinancement() throws Exception {
        // Initialize the database
        sygTypeSourceFinancementRepository.saveAndFlush(sygTypeSourceFinancement);

        // Get the sygTypeSourceFinancement
        restSygTypeSourceFinancementMockMvc.perform(get("/api/syg-type-source-financements/{id}", sygTypeSourceFinancement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sygTypeSourceFinancement.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }


    @Test
    @Transactional
    public void getSygTypeSourceFinancementsByIdFiltering() throws Exception {
        // Initialize the database
        sygTypeSourceFinancementRepository.saveAndFlush(sygTypeSourceFinancement);

        Long id = sygTypeSourceFinancement.getId();

        defaultSygTypeSourceFinancementShouldBeFound("id.equals=" + id);
        defaultSygTypeSourceFinancementShouldNotBeFound("id.notEquals=" + id);

        defaultSygTypeSourceFinancementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSygTypeSourceFinancementShouldNotBeFound("id.greaterThan=" + id);

        defaultSygTypeSourceFinancementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSygTypeSourceFinancementShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSygTypeSourceFinancementsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        sygTypeSourceFinancementRepository.saveAndFlush(sygTypeSourceFinancement);

        // Get all the sygTypeSourceFinancementList where libelle equals to DEFAULT_LIBELLE
        defaultSygTypeSourceFinancementShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the sygTypeSourceFinancementList where libelle equals to UPDATED_LIBELLE
        defaultSygTypeSourceFinancementShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygTypeSourceFinancementsByLibelleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sygTypeSourceFinancementRepository.saveAndFlush(sygTypeSourceFinancement);

        // Get all the sygTypeSourceFinancementList where libelle not equals to DEFAULT_LIBELLE
        defaultSygTypeSourceFinancementShouldNotBeFound("libelle.notEquals=" + DEFAULT_LIBELLE);

        // Get all the sygTypeSourceFinancementList where libelle not equals to UPDATED_LIBELLE
        defaultSygTypeSourceFinancementShouldBeFound("libelle.notEquals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygTypeSourceFinancementsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        sygTypeSourceFinancementRepository.saveAndFlush(sygTypeSourceFinancement);

        // Get all the sygTypeSourceFinancementList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultSygTypeSourceFinancementShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the sygTypeSourceFinancementList where libelle equals to UPDATED_LIBELLE
        defaultSygTypeSourceFinancementShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygTypeSourceFinancementsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        sygTypeSourceFinancementRepository.saveAndFlush(sygTypeSourceFinancement);

        // Get all the sygTypeSourceFinancementList where libelle is not null
        defaultSygTypeSourceFinancementShouldBeFound("libelle.specified=true");

        // Get all the sygTypeSourceFinancementList where libelle is null
        defaultSygTypeSourceFinancementShouldNotBeFound("libelle.specified=false");
    }
                @Test
    @Transactional
    public void getAllSygTypeSourceFinancementsByLibelleContainsSomething() throws Exception {
        // Initialize the database
        sygTypeSourceFinancementRepository.saveAndFlush(sygTypeSourceFinancement);

        // Get all the sygTypeSourceFinancementList where libelle contains DEFAULT_LIBELLE
        defaultSygTypeSourceFinancementShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the sygTypeSourceFinancementList where libelle contains UPDATED_LIBELLE
        defaultSygTypeSourceFinancementShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygTypeSourceFinancementsByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        sygTypeSourceFinancementRepository.saveAndFlush(sygTypeSourceFinancement);

        // Get all the sygTypeSourceFinancementList where libelle does not contain DEFAULT_LIBELLE
        defaultSygTypeSourceFinancementShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the sygTypeSourceFinancementList where libelle does not contain UPDATED_LIBELLE
        defaultSygTypeSourceFinancementShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSygTypeSourceFinancementShouldBeFound(String filter) throws Exception {
        restSygTypeSourceFinancementMockMvc.perform(get("/api/syg-type-source-financements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sygTypeSourceFinancement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));

        // Check, that the count call also returns 1
        restSygTypeSourceFinancementMockMvc.perform(get("/api/syg-type-source-financements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSygTypeSourceFinancementShouldNotBeFound(String filter) throws Exception {
        restSygTypeSourceFinancementMockMvc.perform(get("/api/syg-type-source-financements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSygTypeSourceFinancementMockMvc.perform(get("/api/syg-type-source-financements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSygTypeSourceFinancement() throws Exception {
        // Get the sygTypeSourceFinancement
        restSygTypeSourceFinancementMockMvc.perform(get("/api/syg-type-source-financements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSygTypeSourceFinancement() throws Exception {
        // Initialize the database
        sygTypeSourceFinancementRepository.saveAndFlush(sygTypeSourceFinancement);

        int databaseSizeBeforeUpdate = sygTypeSourceFinancementRepository.findAll().size();

        // Update the sygTypeSourceFinancement
        SygTypeSourceFinancement updatedSygTypeSourceFinancement = sygTypeSourceFinancementRepository.findById(sygTypeSourceFinancement.getId()).get();
        // Disconnect from session so that the updates on updatedSygTypeSourceFinancement are not directly saved in db
        em.detach(updatedSygTypeSourceFinancement);
        updatedSygTypeSourceFinancement
            .libelle(UPDATED_LIBELLE);
        SygTypeSourceFinancementDTO sygTypeSourceFinancementDTO = sygTypeSourceFinancementMapper.toDto(updatedSygTypeSourceFinancement);

        restSygTypeSourceFinancementMockMvc.perform(put("/api/syg-type-source-financements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygTypeSourceFinancementDTO)))
            .andExpect(status().isOk());

        // Validate the SygTypeSourceFinancement in the database
        List<SygTypeSourceFinancement> sygTypeSourceFinancementList = sygTypeSourceFinancementRepository.findAll();
        assertThat(sygTypeSourceFinancementList).hasSize(databaseSizeBeforeUpdate);
        SygTypeSourceFinancement testSygTypeSourceFinancement = sygTypeSourceFinancementList.get(sygTypeSourceFinancementList.size() - 1);
        assertThat(testSygTypeSourceFinancement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void updateNonExistingSygTypeSourceFinancement() throws Exception {
        int databaseSizeBeforeUpdate = sygTypeSourceFinancementRepository.findAll().size();

        // Create the SygTypeSourceFinancement
        SygTypeSourceFinancementDTO sygTypeSourceFinancementDTO = sygTypeSourceFinancementMapper.toDto(sygTypeSourceFinancement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSygTypeSourceFinancementMockMvc.perform(put("/api/syg-type-source-financements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygTypeSourceFinancementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SygTypeSourceFinancement in the database
        List<SygTypeSourceFinancement> sygTypeSourceFinancementList = sygTypeSourceFinancementRepository.findAll();
        assertThat(sygTypeSourceFinancementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSygTypeSourceFinancement() throws Exception {
        // Initialize the database
        sygTypeSourceFinancementRepository.saveAndFlush(sygTypeSourceFinancement);

        int databaseSizeBeforeDelete = sygTypeSourceFinancementRepository.findAll().size();

        // Delete the sygTypeSourceFinancement
        restSygTypeSourceFinancementMockMvc.perform(delete("/api/syg-type-source-financements/{id}", sygTypeSourceFinancement.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SygTypeSourceFinancement> sygTypeSourceFinancementList = sygTypeSourceFinancementRepository.findAll();
        assertThat(sygTypeSourceFinancementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
