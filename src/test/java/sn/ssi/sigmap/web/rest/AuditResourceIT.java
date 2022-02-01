package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.PlanpassationmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.Audit;
import sn.ssi.sigmap.repository.AuditRepository;
import sn.ssi.sigmap.service.AuditService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AuditResource} REST controller.
 */
@SpringBootTest(classes = { PlanpassationmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class AuditResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TYPE_CONNEXION = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_CONNEXION = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuditMockMvc;

    private Audit audit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Audit createEntity(EntityManager em) {
        Audit audit = new Audit()
            .date(DEFAULT_DATE)
            .typeConnexion(DEFAULT_TYPE_CONNEXION)
            .entite(DEFAULT_ENTITE)
            .message(DEFAULT_MESSAGE);
        return audit;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Audit createUpdatedEntity(EntityManager em) {
        Audit audit = new Audit()
            .date(UPDATED_DATE)
            .typeConnexion(UPDATED_TYPE_CONNEXION)
            .entite(UPDATED_ENTITE)
            .message(UPDATED_MESSAGE);
        return audit;
    }

    @BeforeEach
    public void initTest() {
        audit = createEntity(em);
    }

    @Test
    @Transactional
    public void createAudit() throws Exception {
        int databaseSizeBeforeCreate = auditRepository.findAll().size();
        // Create the Audit
        restAuditMockMvc.perform(post("/api/audits").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(audit)))
            .andExpect(status().isCreated());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeCreate + 1);
        Audit testAudit = auditList.get(auditList.size() - 1);
        assertThat(testAudit.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAudit.getTypeConnexion()).isEqualTo(DEFAULT_TYPE_CONNEXION);
        assertThat(testAudit.getEntite()).isEqualTo(DEFAULT_ENTITE);
        assertThat(testAudit.getMessage()).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    public void createAuditWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = auditRepository.findAll().size();

        // Create the Audit with an existing ID
        audit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuditMockMvc.perform(post("/api/audits").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(audit)))
            .andExpect(status().isBadRequest());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAudits() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList
        restAuditMockMvc.perform(get("/api/audits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(audit.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].typeConnexion").value(hasItem(DEFAULT_TYPE_CONNEXION)))
            .andExpect(jsonPath("$.[*].entite").value(hasItem(DEFAULT_ENTITE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)));
    }
    
    @Test
    @Transactional
    public void getAudit() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get the audit
        restAuditMockMvc.perform(get("/api/audits/{id}", audit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(audit.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.typeConnexion").value(DEFAULT_TYPE_CONNEXION))
            .andExpect(jsonPath("$.entite").value(DEFAULT_ENTITE))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE));
    }
    @Test
    @Transactional
    public void getNonExistingAudit() throws Exception {
        // Get the audit
        restAuditMockMvc.perform(get("/api/audits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAudit() throws Exception {
        // Initialize the database
        auditService.save(audit);

        int databaseSizeBeforeUpdate = auditRepository.findAll().size();

        // Update the audit
        Audit updatedAudit = auditRepository.findById(audit.getId()).get();
        // Disconnect from session so that the updates on updatedAudit are not directly saved in db
        em.detach(updatedAudit);
        updatedAudit
            .date(UPDATED_DATE)
            .typeConnexion(UPDATED_TYPE_CONNEXION)
            .entite(UPDATED_ENTITE)
            .message(UPDATED_MESSAGE);

        restAuditMockMvc.perform(put("/api/audits").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAudit)))
            .andExpect(status().isOk());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeUpdate);
        Audit testAudit = auditList.get(auditList.size() - 1);
        assertThat(testAudit.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAudit.getTypeConnexion()).isEqualTo(UPDATED_TYPE_CONNEXION);
        assertThat(testAudit.getEntite()).isEqualTo(UPDATED_ENTITE);
        assertThat(testAudit.getMessage()).isEqualTo(UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingAudit() throws Exception {
        int databaseSizeBeforeUpdate = auditRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditMockMvc.perform(put("/api/audits").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(audit)))
            .andExpect(status().isBadRequest());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAudit() throws Exception {
        // Initialize the database
        auditService.save(audit);

        int databaseSizeBeforeDelete = auditRepository.findAll().size();

        // Delete the audit
        restAuditMockMvc.perform(delete("/api/audits/{id}", audit.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
