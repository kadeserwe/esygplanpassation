package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.PlanpassationmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.SygService;
import sn.ssi.sigmap.domain.SygTypeService;
import sn.ssi.sigmap.repository.SygServiceRepository;
import sn.ssi.sigmap.service.SygServiceService;
import sn.ssi.sigmap.service.dto.SygServiceDTO;
import sn.ssi.sigmap.service.mapper.SygServiceMapper;
import sn.ssi.sigmap.service.dto.SygServiceCriteria;
import sn.ssi.sigmap.service.SygServiceQueryService;

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
 * Integration tests for the {@link SygServiceResource} REST controller.
 */
@SpringBootTest(classes = { PlanpassationmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SygServiceResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private SygServiceRepository sygServiceRepository;

    @Autowired
    private SygServiceMapper sygServiceMapper;

    @Autowired
    private SygServiceService sygServiceService;

    @Autowired
    private SygServiceQueryService sygServiceQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSygServiceMockMvc;

    private SygService sygService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SygService createEntity(EntityManager em) {
        SygService sygService = new SygService()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        SygTypeService sygTypeService;
        if (TestUtil.findAll(em, SygTypeService.class).isEmpty()) {
            sygTypeService = SygTypeServiceResourceIT.createEntity(em);
            em.persist(sygTypeService);
            em.flush();
        } else {
            sygTypeService = TestUtil.findAll(em, SygTypeService.class).get(0);
        }
        sygService.setSygTypeService(sygTypeService);
        return sygService;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SygService createUpdatedEntity(EntityManager em) {
        SygService sygService = new SygService()
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        SygTypeService sygTypeService;
        if (TestUtil.findAll(em, SygTypeService.class).isEmpty()) {
            sygTypeService = SygTypeServiceResourceIT.createUpdatedEntity(em);
            em.persist(sygTypeService);
            em.flush();
        } else {
            sygTypeService = TestUtil.findAll(em, SygTypeService.class).get(0);
        }
        sygService.setSygTypeService(sygTypeService);
        return sygService;
    }

    @BeforeEach
    public void initTest() {
        sygService = createEntity(em);
    }

    @Test
    @Transactional
    public void createSygService() throws Exception {
        int databaseSizeBeforeCreate = sygServiceRepository.findAll().size();
        // Create the SygService
        SygServiceDTO sygServiceDTO = sygServiceMapper.toDto(sygService);
        restSygServiceMockMvc.perform(post("/api/syg-services").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygServiceDTO)))
            .andExpect(status().isCreated());

        // Validate the SygService in the database
        List<SygService> sygServiceList = sygServiceRepository.findAll();
        assertThat(sygServiceList).hasSize(databaseSizeBeforeCreate + 1);
        SygService testSygService = sygServiceList.get(sygServiceList.size() - 1);
        assertThat(testSygService.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSygService.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testSygService.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createSygServiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sygServiceRepository.findAll().size();

        // Create the SygService with an existing ID
        sygService.setId(1L);
        SygServiceDTO sygServiceDTO = sygServiceMapper.toDto(sygService);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSygServiceMockMvc.perform(post("/api/syg-services").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SygService in the database
        List<SygService> sygServiceList = sygServiceRepository.findAll();
        assertThat(sygServiceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sygServiceRepository.findAll().size();
        // set the field null
        sygService.setCode(null);

        // Create the SygService, which fails.
        SygServiceDTO sygServiceDTO = sygServiceMapper.toDto(sygService);


        restSygServiceMockMvc.perform(post("/api/syg-services").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygServiceDTO)))
            .andExpect(status().isBadRequest());

        List<SygService> sygServiceList = sygServiceRepository.findAll();
        assertThat(sygServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sygServiceRepository.findAll().size();
        // set the field null
        sygService.setLibelle(null);

        // Create the SygService, which fails.
        SygServiceDTO sygServiceDTO = sygServiceMapper.toDto(sygService);


        restSygServiceMockMvc.perform(post("/api/syg-services").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygServiceDTO)))
            .andExpect(status().isBadRequest());

        List<SygService> sygServiceList = sygServiceRepository.findAll();
        assertThat(sygServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSygServices() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList
        restSygServiceMockMvc.perform(get("/api/syg-services?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sygService.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getSygService() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get the sygService
        restSygServiceMockMvc.perform(get("/api/syg-services/{id}", sygService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sygService.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getSygServicesByIdFiltering() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        Long id = sygService.getId();

        defaultSygServiceShouldBeFound("id.equals=" + id);
        defaultSygServiceShouldNotBeFound("id.notEquals=" + id);

        defaultSygServiceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSygServiceShouldNotBeFound("id.greaterThan=" + id);

        defaultSygServiceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSygServiceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSygServicesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where code equals to DEFAULT_CODE
        defaultSygServiceShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the sygServiceList where code equals to UPDATED_CODE
        defaultSygServiceShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllSygServicesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where code not equals to DEFAULT_CODE
        defaultSygServiceShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the sygServiceList where code not equals to UPDATED_CODE
        defaultSygServiceShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllSygServicesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where code in DEFAULT_CODE or UPDATED_CODE
        defaultSygServiceShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the sygServiceList where code equals to UPDATED_CODE
        defaultSygServiceShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllSygServicesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where code is not null
        defaultSygServiceShouldBeFound("code.specified=true");

        // Get all the sygServiceList where code is null
        defaultSygServiceShouldNotBeFound("code.specified=false");
    }
                @Test
    @Transactional
    public void getAllSygServicesByCodeContainsSomething() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where code contains DEFAULT_CODE
        defaultSygServiceShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the sygServiceList where code contains UPDATED_CODE
        defaultSygServiceShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllSygServicesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where code does not contain DEFAULT_CODE
        defaultSygServiceShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the sygServiceList where code does not contain UPDATED_CODE
        defaultSygServiceShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllSygServicesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where libelle equals to DEFAULT_LIBELLE
        defaultSygServiceShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the sygServiceList where libelle equals to UPDATED_LIBELLE
        defaultSygServiceShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygServicesByLibelleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where libelle not equals to DEFAULT_LIBELLE
        defaultSygServiceShouldNotBeFound("libelle.notEquals=" + DEFAULT_LIBELLE);

        // Get all the sygServiceList where libelle not equals to UPDATED_LIBELLE
        defaultSygServiceShouldBeFound("libelle.notEquals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygServicesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultSygServiceShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the sygServiceList where libelle equals to UPDATED_LIBELLE
        defaultSygServiceShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygServicesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where libelle is not null
        defaultSygServiceShouldBeFound("libelle.specified=true");

        // Get all the sygServiceList where libelle is null
        defaultSygServiceShouldNotBeFound("libelle.specified=false");
    }
                @Test
    @Transactional
    public void getAllSygServicesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where libelle contains DEFAULT_LIBELLE
        defaultSygServiceShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the sygServiceList where libelle contains UPDATED_LIBELLE
        defaultSygServiceShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygServicesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where libelle does not contain DEFAULT_LIBELLE
        defaultSygServiceShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the sygServiceList where libelle does not contain UPDATED_LIBELLE
        defaultSygServiceShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }


    @Test
    @Transactional
    public void getAllSygServicesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where description equals to DEFAULT_DESCRIPTION
        defaultSygServiceShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the sygServiceList where description equals to UPDATED_DESCRIPTION
        defaultSygServiceShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSygServicesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where description not equals to DEFAULT_DESCRIPTION
        defaultSygServiceShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the sygServiceList where description not equals to UPDATED_DESCRIPTION
        defaultSygServiceShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSygServicesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSygServiceShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the sygServiceList where description equals to UPDATED_DESCRIPTION
        defaultSygServiceShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSygServicesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where description is not null
        defaultSygServiceShouldBeFound("description.specified=true");

        // Get all the sygServiceList where description is null
        defaultSygServiceShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllSygServicesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where description contains DEFAULT_DESCRIPTION
        defaultSygServiceShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the sygServiceList where description contains UPDATED_DESCRIPTION
        defaultSygServiceShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSygServicesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        // Get all the sygServiceList where description does not contain DEFAULT_DESCRIPTION
        defaultSygServiceShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the sygServiceList where description does not contain UPDATED_DESCRIPTION
        defaultSygServiceShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllSygServicesBySygTypeServiceIsEqualToSomething() throws Exception {
        // Get already existing entity
        SygTypeService sygTypeService = sygService.getSygTypeService();
        sygServiceRepository.saveAndFlush(sygService);
        Long sygTypeServiceId = sygTypeService.getId();

        // Get all the sygServiceList where sygTypeService equals to sygTypeServiceId
        defaultSygServiceShouldBeFound("sygTypeServiceId.equals=" + sygTypeServiceId);

        // Get all the sygServiceList where sygTypeService equals to sygTypeServiceId + 1
        defaultSygServiceShouldNotBeFound("sygTypeServiceId.equals=" + (sygTypeServiceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSygServiceShouldBeFound(String filter) throws Exception {
        restSygServiceMockMvc.perform(get("/api/syg-services?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sygService.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restSygServiceMockMvc.perform(get("/api/syg-services/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSygServiceShouldNotBeFound(String filter) throws Exception {
        restSygServiceMockMvc.perform(get("/api/syg-services?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSygServiceMockMvc.perform(get("/api/syg-services/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSygService() throws Exception {
        // Get the sygService
        restSygServiceMockMvc.perform(get("/api/syg-services/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSygService() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        int databaseSizeBeforeUpdate = sygServiceRepository.findAll().size();

        // Update the sygService
        SygService updatedSygService = sygServiceRepository.findById(sygService.getId()).get();
        // Disconnect from session so that the updates on updatedSygService are not directly saved in db
        em.detach(updatedSygService);
        updatedSygService
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION);
        SygServiceDTO sygServiceDTO = sygServiceMapper.toDto(updatedSygService);

        restSygServiceMockMvc.perform(put("/api/syg-services").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygServiceDTO)))
            .andExpect(status().isOk());

        // Validate the SygService in the database
        List<SygService> sygServiceList = sygServiceRepository.findAll();
        assertThat(sygServiceList).hasSize(databaseSizeBeforeUpdate);
        SygService testSygService = sygServiceList.get(sygServiceList.size() - 1);
        assertThat(testSygService.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSygService.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSygService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingSygService() throws Exception {
        int databaseSizeBeforeUpdate = sygServiceRepository.findAll().size();

        // Create the SygService
        SygServiceDTO sygServiceDTO = sygServiceMapper.toDto(sygService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSygServiceMockMvc.perform(put("/api/syg-services").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SygService in the database
        List<SygService> sygServiceList = sygServiceRepository.findAll();
        assertThat(sygServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSygService() throws Exception {
        // Initialize the database
        sygServiceRepository.saveAndFlush(sygService);

        int databaseSizeBeforeDelete = sygServiceRepository.findAll().size();

        // Delete the sygService
        restSygServiceMockMvc.perform(delete("/api/syg-services/{id}", sygService.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SygService> sygServiceList = sygServiceRepository.findAll();
        assertThat(sygServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
