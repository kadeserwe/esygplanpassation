package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.PlanpassationmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.ModePassation;
import sn.ssi.sigmap.repository.ModePassationRepository;

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
 * Integration tests for the {@link ModePassationResource} REST controller.
 */
@SpringBootTest(classes = { PlanpassationmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ModePassationResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ModePassationRepository modePassationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModePassationMockMvc;

    private ModePassation modePassation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModePassation createEntity(EntityManager em) {
        ModePassation modePassation = new ModePassation()
            .libelle(DEFAULT_LIBELLE)
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION);
        return modePassation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModePassation createUpdatedEntity(EntityManager em) {
        ModePassation modePassation = new ModePassation()
            .libelle(UPDATED_LIBELLE)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION);
        return modePassation;
    }

    @BeforeEach
    public void initTest() {
        modePassation = createEntity(em);
    }

    @Test
    @Transactional
    public void createModePassation() throws Exception {
        int databaseSizeBeforeCreate = modePassationRepository.findAll().size();
        // Create the ModePassation
        restModePassationMockMvc.perform(post("/api/mode-passations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modePassation)))
            .andExpect(status().isCreated());

        // Validate the ModePassation in the database
        List<ModePassation> modePassationList = modePassationRepository.findAll();
        assertThat(modePassationList).hasSize(databaseSizeBeforeCreate + 1);
        ModePassation testModePassation = modePassationList.get(modePassationList.size() - 1);
        assertThat(testModePassation.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testModePassation.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testModePassation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createModePassationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = modePassationRepository.findAll().size();

        // Create the ModePassation with an existing ID
        modePassation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModePassationMockMvc.perform(post("/api/mode-passations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modePassation)))
            .andExpect(status().isBadRequest());

        // Validate the ModePassation in the database
        List<ModePassation> modePassationList = modePassationRepository.findAll();
        assertThat(modePassationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = modePassationRepository.findAll().size();
        // set the field null
        modePassation.setLibelle(null);

        // Create the ModePassation, which fails.


        restModePassationMockMvc.perform(post("/api/mode-passations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modePassation)))
            .andExpect(status().isBadRequest());

        List<ModePassation> modePassationList = modePassationRepository.findAll();
        assertThat(modePassationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = modePassationRepository.findAll().size();
        // set the field null
        modePassation.setCode(null);

        // Create the ModePassation, which fails.


        restModePassationMockMvc.perform(post("/api/mode-passations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modePassation)))
            .andExpect(status().isBadRequest());

        List<ModePassation> modePassationList = modePassationRepository.findAll();
        assertThat(modePassationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = modePassationRepository.findAll().size();
        // set the field null
        modePassation.setDescription(null);

        // Create the ModePassation, which fails.


        restModePassationMockMvc.perform(post("/api/mode-passations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modePassation)))
            .andExpect(status().isBadRequest());

        List<ModePassation> modePassationList = modePassationRepository.findAll();
        assertThat(modePassationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllModePassations() throws Exception {
        // Initialize the database
        modePassationRepository.saveAndFlush(modePassation);

        // Get all the modePassationList
        restModePassationMockMvc.perform(get("/api/mode-passations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modePassation.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getModePassation() throws Exception {
        // Initialize the database
        modePassationRepository.saveAndFlush(modePassation);

        // Get the modePassation
        restModePassationMockMvc.perform(get("/api/mode-passations/{id}", modePassation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(modePassation.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingModePassation() throws Exception {
        // Get the modePassation
        restModePassationMockMvc.perform(get("/api/mode-passations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModePassation() throws Exception {
        // Initialize the database
        modePassationRepository.saveAndFlush(modePassation);

        int databaseSizeBeforeUpdate = modePassationRepository.findAll().size();

        // Update the modePassation
        ModePassation updatedModePassation = modePassationRepository.findById(modePassation.getId()).get();
        // Disconnect from session so that the updates on updatedModePassation are not directly saved in db
        em.detach(updatedModePassation);
        updatedModePassation
            .libelle(UPDATED_LIBELLE)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION);

        restModePassationMockMvc.perform(put("/api/mode-passations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedModePassation)))
            .andExpect(status().isOk());

        // Validate the ModePassation in the database
        List<ModePassation> modePassationList = modePassationRepository.findAll();
        assertThat(modePassationList).hasSize(databaseSizeBeforeUpdate);
        ModePassation testModePassation = modePassationList.get(modePassationList.size() - 1);
        assertThat(testModePassation.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testModePassation.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testModePassation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingModePassation() throws Exception {
        int databaseSizeBeforeUpdate = modePassationRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModePassationMockMvc.perform(put("/api/mode-passations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(modePassation)))
            .andExpect(status().isBadRequest());

        // Validate the ModePassation in the database
        List<ModePassation> modePassationList = modePassationRepository.findAll();
        assertThat(modePassationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteModePassation() throws Exception {
        // Initialize the database
        modePassationRepository.saveAndFlush(modePassation);

        int databaseSizeBeforeDelete = modePassationRepository.findAll().size();

        // Delete the modePassation
        restModePassationMockMvc.perform(delete("/api/mode-passations/{id}", modePassation.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ModePassation> modePassationList = modePassationRepository.findAll();
        assertThat(modePassationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
