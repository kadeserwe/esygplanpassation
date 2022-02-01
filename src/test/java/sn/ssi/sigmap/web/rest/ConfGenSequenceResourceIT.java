package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.PlanpassationmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.ConfGenSequence;
import sn.ssi.sigmap.repository.ConfGenSequenceRepository;
import sn.ssi.sigmap.service.ConfGenSequenceService;

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
 * Integration tests for the {@link ConfGenSequenceResource} REST controller.
 */
@SpringBootTest(classes = { PlanpassationmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ConfGenSequenceResourceIT {

    private static final String DEFAULT_TABLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TABLE_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_CURRENT_VALUE = 1L;
    private static final Long UPDATED_CURRENT_VALUE = 2L;

    @Autowired
    private ConfGenSequenceRepository confGenSequenceRepository;

    @Autowired
    private ConfGenSequenceService confGenSequenceService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfGenSequenceMockMvc;

    private ConfGenSequence confGenSequence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfGenSequence createEntity(EntityManager em) {
        ConfGenSequence confGenSequence = new ConfGenSequence()
            .tableName(DEFAULT_TABLE_NAME)
            .currentValue(DEFAULT_CURRENT_VALUE);
        return confGenSequence;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfGenSequence createUpdatedEntity(EntityManager em) {
        ConfGenSequence confGenSequence = new ConfGenSequence()
            .tableName(UPDATED_TABLE_NAME)
            .currentValue(UPDATED_CURRENT_VALUE);
        return confGenSequence;
    }

    @BeforeEach
    public void initTest() {
        confGenSequence = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfGenSequence() throws Exception {
        int databaseSizeBeforeCreate = confGenSequenceRepository.findAll().size();
        // Create the ConfGenSequence
        restConfGenSequenceMockMvc.perform(post("/api/conf-gen-sequences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(confGenSequence)))
            .andExpect(status().isCreated());

        // Validate the ConfGenSequence in the database
        List<ConfGenSequence> confGenSequenceList = confGenSequenceRepository.findAll();
        assertThat(confGenSequenceList).hasSize(databaseSizeBeforeCreate + 1);
        ConfGenSequence testConfGenSequence = confGenSequenceList.get(confGenSequenceList.size() - 1);
        assertThat(testConfGenSequence.getTableName()).isEqualTo(DEFAULT_TABLE_NAME);
        assertThat(testConfGenSequence.getCurrentValue()).isEqualTo(DEFAULT_CURRENT_VALUE);
    }

    @Test
    @Transactional
    public void createConfGenSequenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = confGenSequenceRepository.findAll().size();

        // Create the ConfGenSequence with an existing ID
        confGenSequence.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfGenSequenceMockMvc.perform(post("/api/conf-gen-sequences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(confGenSequence)))
            .andExpect(status().isBadRequest());

        // Validate the ConfGenSequence in the database
        List<ConfGenSequence> confGenSequenceList = confGenSequenceRepository.findAll();
        assertThat(confGenSequenceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfGenSequences() throws Exception {
        // Initialize the database
        confGenSequenceRepository.saveAndFlush(confGenSequence);

        // Get all the confGenSequenceList
        restConfGenSequenceMockMvc.perform(get("/api/conf-gen-sequences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(confGenSequence.getId().intValue())))
            .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME)))
            .andExpect(jsonPath("$.[*].currentValue").value(hasItem(DEFAULT_CURRENT_VALUE.intValue())));
    }
    
    @Test
    @Transactional
    public void getConfGenSequence() throws Exception {
        // Initialize the database
        confGenSequenceRepository.saveAndFlush(confGenSequence);

        // Get the confGenSequence
        restConfGenSequenceMockMvc.perform(get("/api/conf-gen-sequences/{id}", confGenSequence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(confGenSequence.getId().intValue()))
            .andExpect(jsonPath("$.tableName").value(DEFAULT_TABLE_NAME))
            .andExpect(jsonPath("$.currentValue").value(DEFAULT_CURRENT_VALUE.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingConfGenSequence() throws Exception {
        // Get the confGenSequence
        restConfGenSequenceMockMvc.perform(get("/api/conf-gen-sequences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfGenSequence() throws Exception {
        // Initialize the database
        confGenSequenceService.save(confGenSequence);

        int databaseSizeBeforeUpdate = confGenSequenceRepository.findAll().size();

        // Update the confGenSequence
        ConfGenSequence updatedConfGenSequence = confGenSequenceRepository.findById(confGenSequence.getId()).get();
        // Disconnect from session so that the updates on updatedConfGenSequence are not directly saved in db
        em.detach(updatedConfGenSequence);
        updatedConfGenSequence
            .tableName(UPDATED_TABLE_NAME)
            .currentValue(UPDATED_CURRENT_VALUE);

        restConfGenSequenceMockMvc.perform(put("/api/conf-gen-sequences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfGenSequence)))
            .andExpect(status().isOk());

        // Validate the ConfGenSequence in the database
        List<ConfGenSequence> confGenSequenceList = confGenSequenceRepository.findAll();
        assertThat(confGenSequenceList).hasSize(databaseSizeBeforeUpdate);
        ConfGenSequence testConfGenSequence = confGenSequenceList.get(confGenSequenceList.size() - 1);
        assertThat(testConfGenSequence.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
        assertThat(testConfGenSequence.getCurrentValue()).isEqualTo(UPDATED_CURRENT_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingConfGenSequence() throws Exception {
        int databaseSizeBeforeUpdate = confGenSequenceRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfGenSequenceMockMvc.perform(put("/api/conf-gen-sequences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(confGenSequence)))
            .andExpect(status().isBadRequest());

        // Validate the ConfGenSequence in the database
        List<ConfGenSequence> confGenSequenceList = confGenSequenceRepository.findAll();
        assertThat(confGenSequenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfGenSequence() throws Exception {
        // Initialize the database
        confGenSequenceService.save(confGenSequence);

        int databaseSizeBeforeDelete = confGenSequenceRepository.findAll().size();

        // Delete the confGenSequence
        restConfGenSequenceMockMvc.perform(delete("/api/conf-gen-sequences/{id}", confGenSequence.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfGenSequence> confGenSequenceList = confGenSequenceRepository.findAll();
        assertThat(confGenSequenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
