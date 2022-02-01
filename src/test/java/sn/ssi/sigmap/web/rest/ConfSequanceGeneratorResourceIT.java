package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.PlanpassationmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.ConfSequanceGenerator;
import sn.ssi.sigmap.repository.ConfSequanceGeneratorRepository;
import sn.ssi.sigmap.service.ConfSequanceGeneratorService;

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
 * Integration tests for the {@link ConfSequanceGeneratorResource} REST controller.
 */
@SpringBootTest(classes = { PlanpassationmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ConfSequanceGeneratorResourceIT {

    private static final String DEFAULT_NOM_TABLE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_TABLE = "BBBBBBBBBB";

    private static final Long DEFAULT_CURRENT_VALUE = 1L;
    private static final Long UPDATED_CURRENT_VALUE = 2L;

    @Autowired
    private ConfSequanceGeneratorRepository confSequanceGeneratorRepository;

    @Autowired
    private ConfSequanceGeneratorService confSequanceGeneratorService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfSequanceGeneratorMockMvc;

    private ConfSequanceGenerator confSequanceGenerator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfSequanceGenerator createEntity(EntityManager em) {
        ConfSequanceGenerator confSequanceGenerator = new ConfSequanceGenerator()
            .nomTable(DEFAULT_NOM_TABLE)
            .currentValue(DEFAULT_CURRENT_VALUE);
        return confSequanceGenerator;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfSequanceGenerator createUpdatedEntity(EntityManager em) {
        ConfSequanceGenerator confSequanceGenerator = new ConfSequanceGenerator()
            .nomTable(UPDATED_NOM_TABLE)
            .currentValue(UPDATED_CURRENT_VALUE);
        return confSequanceGenerator;
    }

    @BeforeEach
    public void initTest() {
        confSequanceGenerator = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfSequanceGenerator() throws Exception {
        int databaseSizeBeforeCreate = confSequanceGeneratorRepository.findAll().size();
        // Create the ConfSequanceGenerator
        restConfSequanceGeneratorMockMvc.perform(post("/api/conf-sequance-generators").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(confSequanceGenerator)))
            .andExpect(status().isCreated());

        // Validate the ConfSequanceGenerator in the database
        List<ConfSequanceGenerator> confSequanceGeneratorList = confSequanceGeneratorRepository.findAll();
        assertThat(confSequanceGeneratorList).hasSize(databaseSizeBeforeCreate + 1);
        ConfSequanceGenerator testConfSequanceGenerator = confSequanceGeneratorList.get(confSequanceGeneratorList.size() - 1);
        assertThat(testConfSequanceGenerator.getNomTable()).isEqualTo(DEFAULT_NOM_TABLE);
        assertThat(testConfSequanceGenerator.getCurrentValue()).isEqualTo(DEFAULT_CURRENT_VALUE);
    }

    @Test
    @Transactional
    public void createConfSequanceGeneratorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = confSequanceGeneratorRepository.findAll().size();

        // Create the ConfSequanceGenerator with an existing ID
        confSequanceGenerator.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfSequanceGeneratorMockMvc.perform(post("/api/conf-sequance-generators").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(confSequanceGenerator)))
            .andExpect(status().isBadRequest());

        // Validate the ConfSequanceGenerator in the database
        List<ConfSequanceGenerator> confSequanceGeneratorList = confSequanceGeneratorRepository.findAll();
        assertThat(confSequanceGeneratorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfSequanceGenerators() throws Exception {
        // Initialize the database
        confSequanceGeneratorRepository.saveAndFlush(confSequanceGenerator);

        // Get all the confSequanceGeneratorList
        restConfSequanceGeneratorMockMvc.perform(get("/api/conf-sequance-generators?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(confSequanceGenerator.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomTable").value(hasItem(DEFAULT_NOM_TABLE)))
            .andExpect(jsonPath("$.[*].currentValue").value(hasItem(DEFAULT_CURRENT_VALUE.intValue())));
    }
    
    @Test
    @Transactional
    public void getConfSequanceGenerator() throws Exception {
        // Initialize the database
        confSequanceGeneratorRepository.saveAndFlush(confSequanceGenerator);

        // Get the confSequanceGenerator
        restConfSequanceGeneratorMockMvc.perform(get("/api/conf-sequance-generators/{id}", confSequanceGenerator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(confSequanceGenerator.getId().intValue()))
            .andExpect(jsonPath("$.nomTable").value(DEFAULT_NOM_TABLE))
            .andExpect(jsonPath("$.currentValue").value(DEFAULT_CURRENT_VALUE.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingConfSequanceGenerator() throws Exception {
        // Get the confSequanceGenerator
        restConfSequanceGeneratorMockMvc.perform(get("/api/conf-sequance-generators/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfSequanceGenerator() throws Exception {
        // Initialize the database
        confSequanceGeneratorService.save(confSequanceGenerator);

        int databaseSizeBeforeUpdate = confSequanceGeneratorRepository.findAll().size();

        // Update the confSequanceGenerator
        ConfSequanceGenerator updatedConfSequanceGenerator = confSequanceGeneratorRepository.findById(confSequanceGenerator.getId()).get();
        // Disconnect from session so that the updates on updatedConfSequanceGenerator are not directly saved in db
        em.detach(updatedConfSequanceGenerator);
        updatedConfSequanceGenerator
            .nomTable(UPDATED_NOM_TABLE)
            .currentValue(UPDATED_CURRENT_VALUE);

        restConfSequanceGeneratorMockMvc.perform(put("/api/conf-sequance-generators").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfSequanceGenerator)))
            .andExpect(status().isOk());

        // Validate the ConfSequanceGenerator in the database
        List<ConfSequanceGenerator> confSequanceGeneratorList = confSequanceGeneratorRepository.findAll();
        assertThat(confSequanceGeneratorList).hasSize(databaseSizeBeforeUpdate);
        ConfSequanceGenerator testConfSequanceGenerator = confSequanceGeneratorList.get(confSequanceGeneratorList.size() - 1);
        assertThat(testConfSequanceGenerator.getNomTable()).isEqualTo(UPDATED_NOM_TABLE);
        assertThat(testConfSequanceGenerator.getCurrentValue()).isEqualTo(UPDATED_CURRENT_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingConfSequanceGenerator() throws Exception {
        int databaseSizeBeforeUpdate = confSequanceGeneratorRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfSequanceGeneratorMockMvc.perform(put("/api/conf-sequance-generators").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(confSequanceGenerator)))
            .andExpect(status().isBadRequest());

        // Validate the ConfSequanceGenerator in the database
        List<ConfSequanceGenerator> confSequanceGeneratorList = confSequanceGeneratorRepository.findAll();
        assertThat(confSequanceGeneratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfSequanceGenerator() throws Exception {
        // Initialize the database
        confSequanceGeneratorService.save(confSequanceGenerator);

        int databaseSizeBeforeDelete = confSequanceGeneratorRepository.findAll().size();

        // Delete the confSequanceGenerator
        restConfSequanceGeneratorMockMvc.perform(delete("/api/conf-sequance-generators/{id}", confSequanceGenerator.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfSequanceGenerator> confSequanceGeneratorList = confSequanceGeneratorRepository.findAll();
        assertThat(confSequanceGeneratorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
