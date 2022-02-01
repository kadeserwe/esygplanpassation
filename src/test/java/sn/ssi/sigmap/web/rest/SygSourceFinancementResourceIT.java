package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.PlanpassationmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.SygSourceFinancement;
import sn.ssi.sigmap.domain.SygTypeSourceFinancement;
import sn.ssi.sigmap.repository.SygSourceFinancementRepository;
import sn.ssi.sigmap.service.SygSourceFinancementService;
import sn.ssi.sigmap.service.dto.SygSourceFinancementDTO;
import sn.ssi.sigmap.service.mapper.SygSourceFinancementMapper;
import sn.ssi.sigmap.service.dto.SygSourceFinancementCriteria;
import sn.ssi.sigmap.service.SygSourceFinancementQueryService;

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
 * Integration tests for the {@link SygSourceFinancementResource} REST controller.
 */
@SpringBootTest(classes = { PlanpassationmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SygSourceFinancementResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private SygSourceFinancementRepository sygSourceFinancementRepository;

    @Autowired
    private SygSourceFinancementMapper sygSourceFinancementMapper;

    @Autowired
    private SygSourceFinancementService sygSourceFinancementService;

    @Autowired
    private SygSourceFinancementQueryService sygSourceFinancementQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSygSourceFinancementMockMvc;

    private SygSourceFinancement sygSourceFinancement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SygSourceFinancement createEntity(EntityManager em) {
        SygSourceFinancement sygSourceFinancement = new SygSourceFinancement()
            .libelle(DEFAULT_LIBELLE);
        // Add required entity
        SygTypeSourceFinancement sygTypeSourceFinancement;
        if (TestUtil.findAll(em, SygTypeSourceFinancement.class).isEmpty()) {
            sygTypeSourceFinancement = SygTypeSourceFinancementResourceIT.createEntity(em);
            em.persist(sygTypeSourceFinancement);
            em.flush();
        } else {
            sygTypeSourceFinancement = TestUtil.findAll(em, SygTypeSourceFinancement.class).get(0);
        }
        sygSourceFinancement.setSygTypeSourceFinancement(sygTypeSourceFinancement);
        return sygSourceFinancement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SygSourceFinancement createUpdatedEntity(EntityManager em) {
        SygSourceFinancement sygSourceFinancement = new SygSourceFinancement()
            .libelle(UPDATED_LIBELLE);
        // Add required entity
        SygTypeSourceFinancement sygTypeSourceFinancement;
        if (TestUtil.findAll(em, SygTypeSourceFinancement.class).isEmpty()) {
            sygTypeSourceFinancement = SygTypeSourceFinancementResourceIT.createUpdatedEntity(em);
            em.persist(sygTypeSourceFinancement);
            em.flush();
        } else {
            sygTypeSourceFinancement = TestUtil.findAll(em, SygTypeSourceFinancement.class).get(0);
        }
        sygSourceFinancement.setSygTypeSourceFinancement(sygTypeSourceFinancement);
        return sygSourceFinancement;
    }

    @BeforeEach
    public void initTest() {
        sygSourceFinancement = createEntity(em);
    }

    @Test
    @Transactional
    public void createSygSourceFinancement() throws Exception {
        int databaseSizeBeforeCreate = sygSourceFinancementRepository.findAll().size();
        // Create the SygSourceFinancement
        SygSourceFinancementDTO sygSourceFinancementDTO = sygSourceFinancementMapper.toDto(sygSourceFinancement);
        restSygSourceFinancementMockMvc.perform(post("/api/syg-source-financements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygSourceFinancementDTO)))
            .andExpect(status().isCreated());

        // Validate the SygSourceFinancement in the database
        List<SygSourceFinancement> sygSourceFinancementList = sygSourceFinancementRepository.findAll();
        assertThat(sygSourceFinancementList).hasSize(databaseSizeBeforeCreate + 1);
        SygSourceFinancement testSygSourceFinancement = sygSourceFinancementList.get(sygSourceFinancementList.size() - 1);
        assertThat(testSygSourceFinancement.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createSygSourceFinancementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sygSourceFinancementRepository.findAll().size();

        // Create the SygSourceFinancement with an existing ID
        sygSourceFinancement.setId(1L);
        SygSourceFinancementDTO sygSourceFinancementDTO = sygSourceFinancementMapper.toDto(sygSourceFinancement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSygSourceFinancementMockMvc.perform(post("/api/syg-source-financements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygSourceFinancementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SygSourceFinancement in the database
        List<SygSourceFinancement> sygSourceFinancementList = sygSourceFinancementRepository.findAll();
        assertThat(sygSourceFinancementList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sygSourceFinancementRepository.findAll().size();
        // set the field null
        sygSourceFinancement.setLibelle(null);

        // Create the SygSourceFinancement, which fails.
        SygSourceFinancementDTO sygSourceFinancementDTO = sygSourceFinancementMapper.toDto(sygSourceFinancement);


        restSygSourceFinancementMockMvc.perform(post("/api/syg-source-financements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygSourceFinancementDTO)))
            .andExpect(status().isBadRequest());

        List<SygSourceFinancement> sygSourceFinancementList = sygSourceFinancementRepository.findAll();
        assertThat(sygSourceFinancementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSygSourceFinancements() throws Exception {
        // Initialize the database
        sygSourceFinancementRepository.saveAndFlush(sygSourceFinancement);

        // Get all the sygSourceFinancementList
        restSygSourceFinancementMockMvc.perform(get("/api/syg-source-financements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sygSourceFinancement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
    
    @Test
    @Transactional
    public void getSygSourceFinancement() throws Exception {
        // Initialize the database
        sygSourceFinancementRepository.saveAndFlush(sygSourceFinancement);

        // Get the sygSourceFinancement
        restSygSourceFinancementMockMvc.perform(get("/api/syg-source-financements/{id}", sygSourceFinancement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sygSourceFinancement.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }


    @Test
    @Transactional
    public void getSygSourceFinancementsByIdFiltering() throws Exception {
        // Initialize the database
        sygSourceFinancementRepository.saveAndFlush(sygSourceFinancement);

        Long id = sygSourceFinancement.getId();

        defaultSygSourceFinancementShouldBeFound("id.equals=" + id);
        defaultSygSourceFinancementShouldNotBeFound("id.notEquals=" + id);

        defaultSygSourceFinancementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSygSourceFinancementShouldNotBeFound("id.greaterThan=" + id);

        defaultSygSourceFinancementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSygSourceFinancementShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSygSourceFinancementsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        sygSourceFinancementRepository.saveAndFlush(sygSourceFinancement);

        // Get all the sygSourceFinancementList where libelle equals to DEFAULT_LIBELLE
        defaultSygSourceFinancementShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the sygSourceFinancementList where libelle equals to UPDATED_LIBELLE
        defaultSygSourceFinancementShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygSourceFinancementsByLibelleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sygSourceFinancementRepository.saveAndFlush(sygSourceFinancement);

        // Get all the sygSourceFinancementList where libelle not equals to DEFAULT_LIBELLE
        defaultSygSourceFinancementShouldNotBeFound("libelle.notEquals=" + DEFAULT_LIBELLE);

        // Get all the sygSourceFinancementList where libelle not equals to UPDATED_LIBELLE
        defaultSygSourceFinancementShouldBeFound("libelle.notEquals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygSourceFinancementsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        sygSourceFinancementRepository.saveAndFlush(sygSourceFinancement);

        // Get all the sygSourceFinancementList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultSygSourceFinancementShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the sygSourceFinancementList where libelle equals to UPDATED_LIBELLE
        defaultSygSourceFinancementShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygSourceFinancementsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        sygSourceFinancementRepository.saveAndFlush(sygSourceFinancement);

        // Get all the sygSourceFinancementList where libelle is not null
        defaultSygSourceFinancementShouldBeFound("libelle.specified=true");

        // Get all the sygSourceFinancementList where libelle is null
        defaultSygSourceFinancementShouldNotBeFound("libelle.specified=false");
    }
                @Test
    @Transactional
    public void getAllSygSourceFinancementsByLibelleContainsSomething() throws Exception {
        // Initialize the database
        sygSourceFinancementRepository.saveAndFlush(sygSourceFinancement);

        // Get all the sygSourceFinancementList where libelle contains DEFAULT_LIBELLE
        defaultSygSourceFinancementShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the sygSourceFinancementList where libelle contains UPDATED_LIBELLE
        defaultSygSourceFinancementShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygSourceFinancementsByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        sygSourceFinancementRepository.saveAndFlush(sygSourceFinancement);

        // Get all the sygSourceFinancementList where libelle does not contain DEFAULT_LIBELLE
        defaultSygSourceFinancementShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the sygSourceFinancementList where libelle does not contain UPDATED_LIBELLE
        defaultSygSourceFinancementShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }


    @Test
    @Transactional
    public void getAllSygSourceFinancementsBySygTypeSourceFinancementIsEqualToSomething() throws Exception {
        // Get already existing entity
        SygTypeSourceFinancement sygTypeSourceFinancement = sygSourceFinancement.getSygTypeSourceFinancement();
        sygSourceFinancementRepository.saveAndFlush(sygSourceFinancement);
        Long sygTypeSourceFinancementId = sygTypeSourceFinancement.getId();

        // Get all the sygSourceFinancementList where sygTypeSourceFinancement equals to sygTypeSourceFinancementId
        defaultSygSourceFinancementShouldBeFound("sygTypeSourceFinancementId.equals=" + sygTypeSourceFinancementId);

        // Get all the sygSourceFinancementList where sygTypeSourceFinancement equals to sygTypeSourceFinancementId + 1
        defaultSygSourceFinancementShouldNotBeFound("sygTypeSourceFinancementId.equals=" + (sygTypeSourceFinancementId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSygSourceFinancementShouldBeFound(String filter) throws Exception {
        restSygSourceFinancementMockMvc.perform(get("/api/syg-source-financements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sygSourceFinancement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));

        // Check, that the count call also returns 1
        restSygSourceFinancementMockMvc.perform(get("/api/syg-source-financements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSygSourceFinancementShouldNotBeFound(String filter) throws Exception {
        restSygSourceFinancementMockMvc.perform(get("/api/syg-source-financements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSygSourceFinancementMockMvc.perform(get("/api/syg-source-financements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSygSourceFinancement() throws Exception {
        // Get the sygSourceFinancement
        restSygSourceFinancementMockMvc.perform(get("/api/syg-source-financements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSygSourceFinancement() throws Exception {
        // Initialize the database
        sygSourceFinancementRepository.saveAndFlush(sygSourceFinancement);

        int databaseSizeBeforeUpdate = sygSourceFinancementRepository.findAll().size();

        // Update the sygSourceFinancement
        SygSourceFinancement updatedSygSourceFinancement = sygSourceFinancementRepository.findById(sygSourceFinancement.getId()).get();
        // Disconnect from session so that the updates on updatedSygSourceFinancement are not directly saved in db
        em.detach(updatedSygSourceFinancement);
        updatedSygSourceFinancement
            .libelle(UPDATED_LIBELLE);
        SygSourceFinancementDTO sygSourceFinancementDTO = sygSourceFinancementMapper.toDto(updatedSygSourceFinancement);

        restSygSourceFinancementMockMvc.perform(put("/api/syg-source-financements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygSourceFinancementDTO)))
            .andExpect(status().isOk());

        // Validate the SygSourceFinancement in the database
        List<SygSourceFinancement> sygSourceFinancementList = sygSourceFinancementRepository.findAll();
        assertThat(sygSourceFinancementList).hasSize(databaseSizeBeforeUpdate);
        SygSourceFinancement testSygSourceFinancement = sygSourceFinancementList.get(sygSourceFinancementList.size() - 1);
        assertThat(testSygSourceFinancement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void updateNonExistingSygSourceFinancement() throws Exception {
        int databaseSizeBeforeUpdate = sygSourceFinancementRepository.findAll().size();

        // Create the SygSourceFinancement
        SygSourceFinancementDTO sygSourceFinancementDTO = sygSourceFinancementMapper.toDto(sygSourceFinancement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSygSourceFinancementMockMvc.perform(put("/api/syg-source-financements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygSourceFinancementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SygSourceFinancement in the database
        List<SygSourceFinancement> sygSourceFinancementList = sygSourceFinancementRepository.findAll();
        assertThat(sygSourceFinancementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSygSourceFinancement() throws Exception {
        // Initialize the database
        sygSourceFinancementRepository.saveAndFlush(sygSourceFinancement);

        int databaseSizeBeforeDelete = sygSourceFinancementRepository.findAll().size();

        // Delete the sygSourceFinancement
        restSygSourceFinancementMockMvc.perform(delete("/api/syg-source-financements/{id}", sygSourceFinancement.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SygSourceFinancement> sygSourceFinancementList = sygSourceFinancementRepository.findAll();
        assertThat(sygSourceFinancementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
