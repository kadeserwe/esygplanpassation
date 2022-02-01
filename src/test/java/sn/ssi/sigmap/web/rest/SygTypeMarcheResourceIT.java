package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.PlanpassationmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.SygTypeMarche;
import sn.ssi.sigmap.repository.SygTypeMarcheRepository;
import sn.ssi.sigmap.service.SygTypeMarcheService;
import sn.ssi.sigmap.service.dto.SygTypeMarcheDTO;
import sn.ssi.sigmap.service.mapper.SygTypeMarcheMapper;
import sn.ssi.sigmap.service.dto.SygTypeMarcheCriteria;
import sn.ssi.sigmap.service.SygTypeMarcheQueryService;

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
 * Integration tests for the {@link SygTypeMarcheResource} REST controller.
 */
@SpringBootTest(classes = { PlanpassationmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SygTypeMarcheResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private SygTypeMarcheRepository sygTypeMarcheRepository;

    @Autowired
    private SygTypeMarcheMapper sygTypeMarcheMapper;

    @Autowired
    private SygTypeMarcheService sygTypeMarcheService;

    @Autowired
    private SygTypeMarcheQueryService sygTypeMarcheQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSygTypeMarcheMockMvc;

    private SygTypeMarche sygTypeMarche;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SygTypeMarche createEntity(EntityManager em) {
        SygTypeMarche sygTypeMarche = new SygTypeMarche()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .description(DEFAULT_DESCRIPTION);
        return sygTypeMarche;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SygTypeMarche createUpdatedEntity(EntityManager em) {
        SygTypeMarche sygTypeMarche = new SygTypeMarche()
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION);
        return sygTypeMarche;
    }

    @BeforeEach
    public void initTest() {
        sygTypeMarche = createEntity(em);
    }

    @Test
    @Transactional
    public void createSygTypeMarche() throws Exception {
        int databaseSizeBeforeCreate = sygTypeMarcheRepository.findAll().size();
        // Create the SygTypeMarche
        SygTypeMarcheDTO sygTypeMarcheDTO = sygTypeMarcheMapper.toDto(sygTypeMarche);
        restSygTypeMarcheMockMvc.perform(post("/api/syg-type-marches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygTypeMarcheDTO)))
            .andExpect(status().isCreated());

        // Validate the SygTypeMarche in the database
        List<SygTypeMarche> sygTypeMarcheList = sygTypeMarcheRepository.findAll();
        assertThat(sygTypeMarcheList).hasSize(databaseSizeBeforeCreate + 1);
        SygTypeMarche testSygTypeMarche = sygTypeMarcheList.get(sygTypeMarcheList.size() - 1);
        assertThat(testSygTypeMarche.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSygTypeMarche.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testSygTypeMarche.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createSygTypeMarcheWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sygTypeMarcheRepository.findAll().size();

        // Create the SygTypeMarche with an existing ID
        sygTypeMarche.setId(1L);
        SygTypeMarcheDTO sygTypeMarcheDTO = sygTypeMarcheMapper.toDto(sygTypeMarche);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSygTypeMarcheMockMvc.perform(post("/api/syg-type-marches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygTypeMarcheDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SygTypeMarche in the database
        List<SygTypeMarche> sygTypeMarcheList = sygTypeMarcheRepository.findAll();
        assertThat(sygTypeMarcheList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sygTypeMarcheRepository.findAll().size();
        // set the field null
        sygTypeMarche.setCode(null);

        // Create the SygTypeMarche, which fails.
        SygTypeMarcheDTO sygTypeMarcheDTO = sygTypeMarcheMapper.toDto(sygTypeMarche);


        restSygTypeMarcheMockMvc.perform(post("/api/syg-type-marches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygTypeMarcheDTO)))
            .andExpect(status().isBadRequest());

        List<SygTypeMarche> sygTypeMarcheList = sygTypeMarcheRepository.findAll();
        assertThat(sygTypeMarcheList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sygTypeMarcheRepository.findAll().size();
        // set the field null
        sygTypeMarche.setLibelle(null);

        // Create the SygTypeMarche, which fails.
        SygTypeMarcheDTO sygTypeMarcheDTO = sygTypeMarcheMapper.toDto(sygTypeMarche);


        restSygTypeMarcheMockMvc.perform(post("/api/syg-type-marches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygTypeMarcheDTO)))
            .andExpect(status().isBadRequest());

        List<SygTypeMarche> sygTypeMarcheList = sygTypeMarcheRepository.findAll();
        assertThat(sygTypeMarcheList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSygTypeMarches() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList
        restSygTypeMarcheMockMvc.perform(get("/api/syg-type-marches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sygTypeMarche.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getSygTypeMarche() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get the sygTypeMarche
        restSygTypeMarcheMockMvc.perform(get("/api/syg-type-marches/{id}", sygTypeMarche.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sygTypeMarche.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getSygTypeMarchesByIdFiltering() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        Long id = sygTypeMarche.getId();

        defaultSygTypeMarcheShouldBeFound("id.equals=" + id);
        defaultSygTypeMarcheShouldNotBeFound("id.notEquals=" + id);

        defaultSygTypeMarcheShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSygTypeMarcheShouldNotBeFound("id.greaterThan=" + id);

        defaultSygTypeMarcheShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSygTypeMarcheShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSygTypeMarchesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where code equals to DEFAULT_CODE
        defaultSygTypeMarcheShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the sygTypeMarcheList where code equals to UPDATED_CODE
        defaultSygTypeMarcheShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllSygTypeMarchesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where code not equals to DEFAULT_CODE
        defaultSygTypeMarcheShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the sygTypeMarcheList where code not equals to UPDATED_CODE
        defaultSygTypeMarcheShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllSygTypeMarchesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where code in DEFAULT_CODE or UPDATED_CODE
        defaultSygTypeMarcheShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the sygTypeMarcheList where code equals to UPDATED_CODE
        defaultSygTypeMarcheShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllSygTypeMarchesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where code is not null
        defaultSygTypeMarcheShouldBeFound("code.specified=true");

        // Get all the sygTypeMarcheList where code is null
        defaultSygTypeMarcheShouldNotBeFound("code.specified=false");
    }
                @Test
    @Transactional
    public void getAllSygTypeMarchesByCodeContainsSomething() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where code contains DEFAULT_CODE
        defaultSygTypeMarcheShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the sygTypeMarcheList where code contains UPDATED_CODE
        defaultSygTypeMarcheShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllSygTypeMarchesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where code does not contain DEFAULT_CODE
        defaultSygTypeMarcheShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the sygTypeMarcheList where code does not contain UPDATED_CODE
        defaultSygTypeMarcheShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllSygTypeMarchesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where libelle equals to DEFAULT_LIBELLE
        defaultSygTypeMarcheShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the sygTypeMarcheList where libelle equals to UPDATED_LIBELLE
        defaultSygTypeMarcheShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygTypeMarchesByLibelleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where libelle not equals to DEFAULT_LIBELLE
        defaultSygTypeMarcheShouldNotBeFound("libelle.notEquals=" + DEFAULT_LIBELLE);

        // Get all the sygTypeMarcheList where libelle not equals to UPDATED_LIBELLE
        defaultSygTypeMarcheShouldBeFound("libelle.notEquals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygTypeMarchesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultSygTypeMarcheShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the sygTypeMarcheList where libelle equals to UPDATED_LIBELLE
        defaultSygTypeMarcheShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygTypeMarchesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where libelle is not null
        defaultSygTypeMarcheShouldBeFound("libelle.specified=true");

        // Get all the sygTypeMarcheList where libelle is null
        defaultSygTypeMarcheShouldNotBeFound("libelle.specified=false");
    }
                @Test
    @Transactional
    public void getAllSygTypeMarchesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where libelle contains DEFAULT_LIBELLE
        defaultSygTypeMarcheShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the sygTypeMarcheList where libelle contains UPDATED_LIBELLE
        defaultSygTypeMarcheShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSygTypeMarchesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where libelle does not contain DEFAULT_LIBELLE
        defaultSygTypeMarcheShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the sygTypeMarcheList where libelle does not contain UPDATED_LIBELLE
        defaultSygTypeMarcheShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }


    @Test
    @Transactional
    public void getAllSygTypeMarchesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where description equals to DEFAULT_DESCRIPTION
        defaultSygTypeMarcheShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the sygTypeMarcheList where description equals to UPDATED_DESCRIPTION
        defaultSygTypeMarcheShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSygTypeMarchesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where description not equals to DEFAULT_DESCRIPTION
        defaultSygTypeMarcheShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the sygTypeMarcheList where description not equals to UPDATED_DESCRIPTION
        defaultSygTypeMarcheShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSygTypeMarchesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSygTypeMarcheShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the sygTypeMarcheList where description equals to UPDATED_DESCRIPTION
        defaultSygTypeMarcheShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSygTypeMarchesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where description is not null
        defaultSygTypeMarcheShouldBeFound("description.specified=true");

        // Get all the sygTypeMarcheList where description is null
        defaultSygTypeMarcheShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllSygTypeMarchesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where description contains DEFAULT_DESCRIPTION
        defaultSygTypeMarcheShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the sygTypeMarcheList where description contains UPDATED_DESCRIPTION
        defaultSygTypeMarcheShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSygTypeMarchesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        // Get all the sygTypeMarcheList where description does not contain DEFAULT_DESCRIPTION
        defaultSygTypeMarcheShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the sygTypeMarcheList where description does not contain UPDATED_DESCRIPTION
        defaultSygTypeMarcheShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSygTypeMarcheShouldBeFound(String filter) throws Exception {
        restSygTypeMarcheMockMvc.perform(get("/api/syg-type-marches?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sygTypeMarche.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restSygTypeMarcheMockMvc.perform(get("/api/syg-type-marches/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSygTypeMarcheShouldNotBeFound(String filter) throws Exception {
        restSygTypeMarcheMockMvc.perform(get("/api/syg-type-marches?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSygTypeMarcheMockMvc.perform(get("/api/syg-type-marches/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSygTypeMarche() throws Exception {
        // Get the sygTypeMarche
        restSygTypeMarcheMockMvc.perform(get("/api/syg-type-marches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSygTypeMarche() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        int databaseSizeBeforeUpdate = sygTypeMarcheRepository.findAll().size();

        // Update the sygTypeMarche
        SygTypeMarche updatedSygTypeMarche = sygTypeMarcheRepository.findById(sygTypeMarche.getId()).get();
        // Disconnect from session so that the updates on updatedSygTypeMarche are not directly saved in db
        em.detach(updatedSygTypeMarche);
        updatedSygTypeMarche
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION);
        SygTypeMarcheDTO sygTypeMarcheDTO = sygTypeMarcheMapper.toDto(updatedSygTypeMarche);

        restSygTypeMarcheMockMvc.perform(put("/api/syg-type-marches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygTypeMarcheDTO)))
            .andExpect(status().isOk());

        // Validate the SygTypeMarche in the database
        List<SygTypeMarche> sygTypeMarcheList = sygTypeMarcheRepository.findAll();
        assertThat(sygTypeMarcheList).hasSize(databaseSizeBeforeUpdate);
        SygTypeMarche testSygTypeMarche = sygTypeMarcheList.get(sygTypeMarcheList.size() - 1);
        assertThat(testSygTypeMarche.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSygTypeMarche.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSygTypeMarche.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingSygTypeMarche() throws Exception {
        int databaseSizeBeforeUpdate = sygTypeMarcheRepository.findAll().size();

        // Create the SygTypeMarche
        SygTypeMarcheDTO sygTypeMarcheDTO = sygTypeMarcheMapper.toDto(sygTypeMarche);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSygTypeMarcheMockMvc.perform(put("/api/syg-type-marches").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygTypeMarcheDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SygTypeMarche in the database
        List<SygTypeMarche> sygTypeMarcheList = sygTypeMarcheRepository.findAll();
        assertThat(sygTypeMarcheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSygTypeMarche() throws Exception {
        // Initialize the database
        sygTypeMarcheRepository.saveAndFlush(sygTypeMarche);

        int databaseSizeBeforeDelete = sygTypeMarcheRepository.findAll().size();

        // Delete the sygTypeMarche
        restSygTypeMarcheMockMvc.perform(delete("/api/syg-type-marches/{id}", sygTypeMarche.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SygTypeMarche> sygTypeMarcheList = sygTypeMarcheRepository.findAll();
        assertThat(sygTypeMarcheList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
