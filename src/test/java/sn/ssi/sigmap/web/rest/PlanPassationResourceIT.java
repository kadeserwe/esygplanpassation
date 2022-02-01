package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.PlanpassationmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.PlanPassation;
import sn.ssi.sigmap.repository.PlanPassationRepository;
import sn.ssi.sigmap.service.PlanPassationService;
import sn.ssi.sigmap.service.dto.PlanPassationDTO;
import sn.ssi.sigmap.service.mapper.PlanPassationMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
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
 * Integration tests for the {@link PlanPassationResource} REST controller.
 */
@SpringBootTest(classes = { PlanpassationmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class PlanPassationResourceIT {

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANNEE = 1;
    private static final Integer UPDATED_ANNEE = 2;

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MISE_VALIDATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MISE_VALIDATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_VALIDATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_VALIDATION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_STATUT = "AAAAAAAAAA";
    private static final String UPDATED_STATUT = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE_MISE_EN_VALIDATION_AC = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE_MISE_EN_VALIDATION_AC = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE_MISE_VALIDATION_AC = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_MISE_VALIDATION_AC = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FICHIER_MISE_VALIDATION_AC = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER_MISE_VALIDATION_AC = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FICHIER_MISE_VALIDATION_AC_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_MISE_VALIDATION_AC_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_DATE_MISE_EN_VALIDATION_CCMP = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MISE_EN_VALIDATION_CCMP = LocalDate.now(ZoneId.systemDefault());

    private static final byte[] DEFAULT_FICHIER_MISE_VALIDATION_CCMP = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER_MISE_VALIDATION_CCMP = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FICHIER_MISE_VALIDATION_CCMP_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_MISE_VALIDATION_CCMP_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_REFERENCE_MISE_VALIDATION_CCMP = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_MISE_VALIDATION_CCMP = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_VALIDATION_1 = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_VALIDATION_1 = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_COMMENTAIRE_VALIDATION = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE_VALIDATION = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE_VALIDATION = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_VALIDATION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FICHIER_VALIDATION = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER_VALIDATION = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FICHIER_VALIDATION_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_VALIDATION_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_DATE_VALIDATION_2 = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_VALIDATION_2 = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_REJET = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_REJET = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_PUBLICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PUBLICATION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_COMMENTAIRE_PUBLICATION = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE_PUBLICATION = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_PLAN = "AAAAAAAAAA";
    private static final String UPDATED_NUM_PLAN = "BBBBBBBBBB";

    @Autowired
    private PlanPassationRepository planPassationRepository;

    @Autowired
    private PlanPassationMapper planPassationMapper;

    @Autowired
    private PlanPassationService planPassationService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanPassationMockMvc;

    private PlanPassation planPassation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanPassation createEntity(EntityManager em) {
        PlanPassation planPassation = new PlanPassation()
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .commentaire(DEFAULT_COMMENTAIRE)
            .annee(DEFAULT_ANNEE)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateMiseValidation(DEFAULT_DATE_MISE_VALIDATION)
            .dateValidation(DEFAULT_DATE_VALIDATION)
            .statut(DEFAULT_STATUT)
            .commentaireMiseEnValidationAC(DEFAULT_COMMENTAIRE_MISE_EN_VALIDATION_AC)
            .referenceMiseValidationAC(DEFAULT_REFERENCE_MISE_VALIDATION_AC)
            .fichierMiseValidationAC(DEFAULT_FICHIER_MISE_VALIDATION_AC)
            .fichierMiseValidationACContentType(DEFAULT_FICHIER_MISE_VALIDATION_AC_CONTENT_TYPE)
            .dateMiseEnValidationCcmp(DEFAULT_DATE_MISE_EN_VALIDATION_CCMP)
            .fichierMiseValidationCcmp(DEFAULT_FICHIER_MISE_VALIDATION_CCMP)
            .fichierMiseValidationCcmpContentType(DEFAULT_FICHIER_MISE_VALIDATION_CCMP_CONTENT_TYPE)
            .referenceMiseValidationCcmp(DEFAULT_REFERENCE_MISE_VALIDATION_CCMP)
            .dateValidation1(DEFAULT_DATE_VALIDATION_1)
            .commentaireValidation(DEFAULT_COMMENTAIRE_VALIDATION)
            .referenceValidation(DEFAULT_REFERENCE_VALIDATION)
            .fichierValidation(DEFAULT_FICHIER_VALIDATION)
            .fichierValidationContentType(DEFAULT_FICHIER_VALIDATION_CONTENT_TYPE)
            .dateValidation2(DEFAULT_DATE_VALIDATION_2)
            .dateRejet(DEFAULT_DATE_REJET)
            .datePublication(DEFAULT_DATE_PUBLICATION)
            .commentairePublication(DEFAULT_COMMENTAIRE_PUBLICATION)
            .numPlan(DEFAULT_NUM_PLAN);
        return planPassation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanPassation createUpdatedEntity(EntityManager em) {
        PlanPassation planPassation = new PlanPassation()
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .commentaire(UPDATED_COMMENTAIRE)
            .annee(UPDATED_ANNEE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateMiseValidation(UPDATED_DATE_MISE_VALIDATION)
            .dateValidation(UPDATED_DATE_VALIDATION)
            .statut(UPDATED_STATUT)
            .commentaireMiseEnValidationAC(UPDATED_COMMENTAIRE_MISE_EN_VALIDATION_AC)
            .referenceMiseValidationAC(UPDATED_REFERENCE_MISE_VALIDATION_AC)
            .fichierMiseValidationAC(UPDATED_FICHIER_MISE_VALIDATION_AC)
            .fichierMiseValidationACContentType(UPDATED_FICHIER_MISE_VALIDATION_AC_CONTENT_TYPE)
            .dateMiseEnValidationCcmp(UPDATED_DATE_MISE_EN_VALIDATION_CCMP)
            .fichierMiseValidationCcmp(UPDATED_FICHIER_MISE_VALIDATION_CCMP)
            .fichierMiseValidationCcmpContentType(UPDATED_FICHIER_MISE_VALIDATION_CCMP_CONTENT_TYPE)
            .referenceMiseValidationCcmp(UPDATED_REFERENCE_MISE_VALIDATION_CCMP)
            .dateValidation1(UPDATED_DATE_VALIDATION_1)
            .commentaireValidation(UPDATED_COMMENTAIRE_VALIDATION)
            .referenceValidation(UPDATED_REFERENCE_VALIDATION)
            .fichierValidation(UPDATED_FICHIER_VALIDATION)
            .fichierValidationContentType(UPDATED_FICHIER_VALIDATION_CONTENT_TYPE)
            .dateValidation2(UPDATED_DATE_VALIDATION_2)
            .dateRejet(UPDATED_DATE_REJET)
            .datePublication(UPDATED_DATE_PUBLICATION)
            .commentairePublication(UPDATED_COMMENTAIRE_PUBLICATION)
            .numPlan(UPDATED_NUM_PLAN);
        return planPassation;
    }

    @BeforeEach
    public void initTest() {
        planPassation = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanPassation() throws Exception {
        int databaseSizeBeforeCreate = planPassationRepository.findAll().size();
        // Create the PlanPassation
        PlanPassationDTO planPassationDTO = planPassationMapper.toDto(planPassation);
        restPlanPassationMockMvc.perform(post("/api/plan-passations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planPassationDTO)))
            .andExpect(status().isCreated());

        // Validate the PlanPassation in the database
        List<PlanPassation> planPassationList = planPassationRepository.findAll();
        assertThat(planPassationList).hasSize(databaseSizeBeforeCreate + 1);
        PlanPassation testPlanPassation = planPassationList.get(planPassationList.size() - 1);
        assertThat(testPlanPassation.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testPlanPassation.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testPlanPassation.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testPlanPassation.getAnnee()).isEqualTo(DEFAULT_ANNEE);
        assertThat(testPlanPassation.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testPlanPassation.getDateMiseValidation()).isEqualTo(DEFAULT_DATE_MISE_VALIDATION);
        assertThat(testPlanPassation.getDateValidation()).isEqualTo(DEFAULT_DATE_VALIDATION);
        assertThat(testPlanPassation.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testPlanPassation.getCommentaireMiseEnValidationAC()).isEqualTo(DEFAULT_COMMENTAIRE_MISE_EN_VALIDATION_AC);
        assertThat(testPlanPassation.getReferenceMiseValidationAC()).isEqualTo(DEFAULT_REFERENCE_MISE_VALIDATION_AC);
        assertThat(testPlanPassation.getFichierMiseValidationAC()).isEqualTo(DEFAULT_FICHIER_MISE_VALIDATION_AC);
        assertThat(testPlanPassation.getFichierMiseValidationACContentType()).isEqualTo(DEFAULT_FICHIER_MISE_VALIDATION_AC_CONTENT_TYPE);
        assertThat(testPlanPassation.getDateMiseEnValidationCcmp()).isEqualTo(DEFAULT_DATE_MISE_EN_VALIDATION_CCMP);
        assertThat(testPlanPassation.getFichierMiseValidationCcmp()).isEqualTo(DEFAULT_FICHIER_MISE_VALIDATION_CCMP);
        assertThat(testPlanPassation.getFichierMiseValidationCcmpContentType()).isEqualTo(DEFAULT_FICHIER_MISE_VALIDATION_CCMP_CONTENT_TYPE);
        assertThat(testPlanPassation.getReferenceMiseValidationCcmp()).isEqualTo(DEFAULT_REFERENCE_MISE_VALIDATION_CCMP);
        assertThat(testPlanPassation.getDateValidation1()).isEqualTo(DEFAULT_DATE_VALIDATION_1);
        assertThat(testPlanPassation.getCommentaireValidation()).isEqualTo(DEFAULT_COMMENTAIRE_VALIDATION);
        assertThat(testPlanPassation.getReferenceValidation()).isEqualTo(DEFAULT_REFERENCE_VALIDATION);
        assertThat(testPlanPassation.getFichierValidation()).isEqualTo(DEFAULT_FICHIER_VALIDATION);
        assertThat(testPlanPassation.getFichierValidationContentType()).isEqualTo(DEFAULT_FICHIER_VALIDATION_CONTENT_TYPE);
        assertThat(testPlanPassation.getDateValidation2()).isEqualTo(DEFAULT_DATE_VALIDATION_2);
        assertThat(testPlanPassation.getDateRejet()).isEqualTo(DEFAULT_DATE_REJET);
        assertThat(testPlanPassation.getDatePublication()).isEqualTo(DEFAULT_DATE_PUBLICATION);
        assertThat(testPlanPassation.getCommentairePublication()).isEqualTo(DEFAULT_COMMENTAIRE_PUBLICATION);
        assertThat(testPlanPassation.getNumPlan()).isEqualTo(DEFAULT_NUM_PLAN);
    }

    @Test
    @Transactional
    public void createPlanPassationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planPassationRepository.findAll().size();

        // Create the PlanPassation with an existing ID
        planPassation.setId(1L);
        PlanPassationDTO planPassationDTO = planPassationMapper.toDto(planPassation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanPassationMockMvc.perform(post("/api/plan-passations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planPassationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlanPassation in the database
        List<PlanPassation> planPassationList = planPassationRepository.findAll();
        assertThat(planPassationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkAnneeIsRequired() throws Exception {
        int databaseSizeBeforeTest = planPassationRepository.findAll().size();
        // set the field null
        planPassation.setAnnee(null);

        // Create the PlanPassation, which fails.
        PlanPassationDTO planPassationDTO = planPassationMapper.toDto(planPassation);


        restPlanPassationMockMvc.perform(post("/api/plan-passations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planPassationDTO)))
            .andExpect(status().isBadRequest());

        List<PlanPassation> planPassationList = planPassationRepository.findAll();
        assertThat(planPassationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateCreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = planPassationRepository.findAll().size();
        // set the field null
        planPassation.setDateCreation(null);

        // Create the PlanPassation, which fails.
        PlanPassationDTO planPassationDTO = planPassationMapper.toDto(planPassation);


        restPlanPassationMockMvc.perform(post("/api/plan-passations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planPassationDTO)))
            .andExpect(status().isBadRequest());

        List<PlanPassation> planPassationList = planPassationRepository.findAll();
        assertThat(planPassationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlanPassations() throws Exception {
        // Initialize the database
        planPassationRepository.saveAndFlush(planPassation);

        // Get all the planPassationList
        restPlanPassationMockMvc.perform(get("/api/plan-passations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planPassation.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateMiseValidation").value(hasItem(DEFAULT_DATE_MISE_VALIDATION.toString())))
            .andExpect(jsonPath("$.[*].dateValidation").value(hasItem(DEFAULT_DATE_VALIDATION.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)))
            .andExpect(jsonPath("$.[*].commentaireMiseEnValidationAC").value(hasItem(DEFAULT_COMMENTAIRE_MISE_EN_VALIDATION_AC)))
            .andExpect(jsonPath("$.[*].referenceMiseValidationAC").value(hasItem(DEFAULT_REFERENCE_MISE_VALIDATION_AC)))
            .andExpect(jsonPath("$.[*].fichierMiseValidationACContentType").value(hasItem(DEFAULT_FICHIER_MISE_VALIDATION_AC_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichierMiseValidationAC").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER_MISE_VALIDATION_AC))))
            .andExpect(jsonPath("$.[*].dateMiseEnValidationCcmp").value(hasItem(DEFAULT_DATE_MISE_EN_VALIDATION_CCMP.toString())))
            .andExpect(jsonPath("$.[*].fichierMiseValidationCcmpContentType").value(hasItem(DEFAULT_FICHIER_MISE_VALIDATION_CCMP_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichierMiseValidationCcmp").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER_MISE_VALIDATION_CCMP))))
            .andExpect(jsonPath("$.[*].referenceMiseValidationCcmp").value(hasItem(DEFAULT_REFERENCE_MISE_VALIDATION_CCMP)))
            .andExpect(jsonPath("$.[*].dateValidation1").value(hasItem(DEFAULT_DATE_VALIDATION_1.toString())))
            .andExpect(jsonPath("$.[*].commentaireValidation").value(hasItem(DEFAULT_COMMENTAIRE_VALIDATION)))
            .andExpect(jsonPath("$.[*].referenceValidation").value(hasItem(DEFAULT_REFERENCE_VALIDATION)))
            .andExpect(jsonPath("$.[*].fichierValidationContentType").value(hasItem(DEFAULT_FICHIER_VALIDATION_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichierValidation").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER_VALIDATION))))
            .andExpect(jsonPath("$.[*].dateValidation2").value(hasItem(DEFAULT_DATE_VALIDATION_2.toString())))
            .andExpect(jsonPath("$.[*].dateRejet").value(hasItem(DEFAULT_DATE_REJET.toString())))
            .andExpect(jsonPath("$.[*].datePublication").value(hasItem(DEFAULT_DATE_PUBLICATION.toString())))
            .andExpect(jsonPath("$.[*].commentairePublication").value(hasItem(DEFAULT_COMMENTAIRE_PUBLICATION)))
            .andExpect(jsonPath("$.[*].numPlan").value(hasItem(DEFAULT_NUM_PLAN)));
    }
    
    @Test
    @Transactional
    public void getPlanPassation() throws Exception {
        // Initialize the database
        planPassationRepository.saveAndFlush(planPassation);

        // Get the planPassation
        restPlanPassationMockMvc.perform(get("/api/plan-passations/{id}", planPassation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(planPassation.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateMiseValidation").value(DEFAULT_DATE_MISE_VALIDATION.toString()))
            .andExpect(jsonPath("$.dateValidation").value(DEFAULT_DATE_VALIDATION.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT))
            .andExpect(jsonPath("$.commentaireMiseEnValidationAC").value(DEFAULT_COMMENTAIRE_MISE_EN_VALIDATION_AC))
            .andExpect(jsonPath("$.referenceMiseValidationAC").value(DEFAULT_REFERENCE_MISE_VALIDATION_AC))
            .andExpect(jsonPath("$.fichierMiseValidationACContentType").value(DEFAULT_FICHIER_MISE_VALIDATION_AC_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichierMiseValidationAC").value(Base64Utils.encodeToString(DEFAULT_FICHIER_MISE_VALIDATION_AC)))
            .andExpect(jsonPath("$.dateMiseEnValidationCcmp").value(DEFAULT_DATE_MISE_EN_VALIDATION_CCMP.toString()))
            .andExpect(jsonPath("$.fichierMiseValidationCcmpContentType").value(DEFAULT_FICHIER_MISE_VALIDATION_CCMP_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichierMiseValidationCcmp").value(Base64Utils.encodeToString(DEFAULT_FICHIER_MISE_VALIDATION_CCMP)))
            .andExpect(jsonPath("$.referenceMiseValidationCcmp").value(DEFAULT_REFERENCE_MISE_VALIDATION_CCMP))
            .andExpect(jsonPath("$.dateValidation1").value(DEFAULT_DATE_VALIDATION_1.toString()))
            .andExpect(jsonPath("$.commentaireValidation").value(DEFAULT_COMMENTAIRE_VALIDATION))
            .andExpect(jsonPath("$.referenceValidation").value(DEFAULT_REFERENCE_VALIDATION))
            .andExpect(jsonPath("$.fichierValidationContentType").value(DEFAULT_FICHIER_VALIDATION_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichierValidation").value(Base64Utils.encodeToString(DEFAULT_FICHIER_VALIDATION)))
            .andExpect(jsonPath("$.dateValidation2").value(DEFAULT_DATE_VALIDATION_2.toString()))
            .andExpect(jsonPath("$.dateRejet").value(DEFAULT_DATE_REJET.toString()))
            .andExpect(jsonPath("$.datePublication").value(DEFAULT_DATE_PUBLICATION.toString()))
            .andExpect(jsonPath("$.commentairePublication").value(DEFAULT_COMMENTAIRE_PUBLICATION))
            .andExpect(jsonPath("$.numPlan").value(DEFAULT_NUM_PLAN));
    }
    @Test
    @Transactional
    public void getNonExistingPlanPassation() throws Exception {
        // Get the planPassation
        restPlanPassationMockMvc.perform(get("/api/plan-passations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanPassation() throws Exception {
        // Initialize the database
        planPassationRepository.saveAndFlush(planPassation);

        int databaseSizeBeforeUpdate = planPassationRepository.findAll().size();

        // Update the planPassation
        PlanPassation updatedPlanPassation = planPassationRepository.findById(planPassation.getId()).get();
        // Disconnect from session so that the updates on updatedPlanPassation are not directly saved in db
        em.detach(updatedPlanPassation);
        updatedPlanPassation
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .commentaire(UPDATED_COMMENTAIRE)
            .annee(UPDATED_ANNEE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateMiseValidation(UPDATED_DATE_MISE_VALIDATION)
            .dateValidation(UPDATED_DATE_VALIDATION)
            .statut(UPDATED_STATUT)
            .commentaireMiseEnValidationAC(UPDATED_COMMENTAIRE_MISE_EN_VALIDATION_AC)
            .referenceMiseValidationAC(UPDATED_REFERENCE_MISE_VALIDATION_AC)
            .fichierMiseValidationAC(UPDATED_FICHIER_MISE_VALIDATION_AC)
            .fichierMiseValidationACContentType(UPDATED_FICHIER_MISE_VALIDATION_AC_CONTENT_TYPE)
            .dateMiseEnValidationCcmp(UPDATED_DATE_MISE_EN_VALIDATION_CCMP)
            .fichierMiseValidationCcmp(UPDATED_FICHIER_MISE_VALIDATION_CCMP)
            .fichierMiseValidationCcmpContentType(UPDATED_FICHIER_MISE_VALIDATION_CCMP_CONTENT_TYPE)
            .referenceMiseValidationCcmp(UPDATED_REFERENCE_MISE_VALIDATION_CCMP)
            .dateValidation1(UPDATED_DATE_VALIDATION_1)
            .commentaireValidation(UPDATED_COMMENTAIRE_VALIDATION)
            .referenceValidation(UPDATED_REFERENCE_VALIDATION)
            .fichierValidation(UPDATED_FICHIER_VALIDATION)
            .fichierValidationContentType(UPDATED_FICHIER_VALIDATION_CONTENT_TYPE)
            .dateValidation2(UPDATED_DATE_VALIDATION_2)
            .dateRejet(UPDATED_DATE_REJET)
            .datePublication(UPDATED_DATE_PUBLICATION)
            .commentairePublication(UPDATED_COMMENTAIRE_PUBLICATION)
            .numPlan(UPDATED_NUM_PLAN);
        PlanPassationDTO planPassationDTO = planPassationMapper.toDto(updatedPlanPassation);

        restPlanPassationMockMvc.perform(put("/api/plan-passations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planPassationDTO)))
            .andExpect(status().isOk());

        // Validate the PlanPassation in the database
        List<PlanPassation> planPassationList = planPassationRepository.findAll();
        assertThat(planPassationList).hasSize(databaseSizeBeforeUpdate);
        PlanPassation testPlanPassation = planPassationList.get(planPassationList.size() - 1);
        assertThat(testPlanPassation.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testPlanPassation.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testPlanPassation.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testPlanPassation.getAnnee()).isEqualTo(UPDATED_ANNEE);
        assertThat(testPlanPassation.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testPlanPassation.getDateMiseValidation()).isEqualTo(UPDATED_DATE_MISE_VALIDATION);
        assertThat(testPlanPassation.getDateValidation()).isEqualTo(UPDATED_DATE_VALIDATION);
        assertThat(testPlanPassation.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testPlanPassation.getCommentaireMiseEnValidationAC()).isEqualTo(UPDATED_COMMENTAIRE_MISE_EN_VALIDATION_AC);
        assertThat(testPlanPassation.getReferenceMiseValidationAC()).isEqualTo(UPDATED_REFERENCE_MISE_VALIDATION_AC);
        assertThat(testPlanPassation.getFichierMiseValidationAC()).isEqualTo(UPDATED_FICHIER_MISE_VALIDATION_AC);
        assertThat(testPlanPassation.getFichierMiseValidationACContentType()).isEqualTo(UPDATED_FICHIER_MISE_VALIDATION_AC_CONTENT_TYPE);
        assertThat(testPlanPassation.getDateMiseEnValidationCcmp()).isEqualTo(UPDATED_DATE_MISE_EN_VALIDATION_CCMP);
        assertThat(testPlanPassation.getFichierMiseValidationCcmp()).isEqualTo(UPDATED_FICHIER_MISE_VALIDATION_CCMP);
        assertThat(testPlanPassation.getFichierMiseValidationCcmpContentType()).isEqualTo(UPDATED_FICHIER_MISE_VALIDATION_CCMP_CONTENT_TYPE);
        assertThat(testPlanPassation.getReferenceMiseValidationCcmp()).isEqualTo(UPDATED_REFERENCE_MISE_VALIDATION_CCMP);
        assertThat(testPlanPassation.getDateValidation1()).isEqualTo(UPDATED_DATE_VALIDATION_1);
        assertThat(testPlanPassation.getCommentaireValidation()).isEqualTo(UPDATED_COMMENTAIRE_VALIDATION);
        assertThat(testPlanPassation.getReferenceValidation()).isEqualTo(UPDATED_REFERENCE_VALIDATION);
        assertThat(testPlanPassation.getFichierValidation()).isEqualTo(UPDATED_FICHIER_VALIDATION);
        assertThat(testPlanPassation.getFichierValidationContentType()).isEqualTo(UPDATED_FICHIER_VALIDATION_CONTENT_TYPE);
        assertThat(testPlanPassation.getDateValidation2()).isEqualTo(UPDATED_DATE_VALIDATION_2);
        assertThat(testPlanPassation.getDateRejet()).isEqualTo(UPDATED_DATE_REJET);
        assertThat(testPlanPassation.getDatePublication()).isEqualTo(UPDATED_DATE_PUBLICATION);
        assertThat(testPlanPassation.getCommentairePublication()).isEqualTo(UPDATED_COMMENTAIRE_PUBLICATION);
        assertThat(testPlanPassation.getNumPlan()).isEqualTo(UPDATED_NUM_PLAN);
    }

    @Test
    @Transactional
    public void updateNonExistingPlanPassation() throws Exception {
        int databaseSizeBeforeUpdate = planPassationRepository.findAll().size();

        // Create the PlanPassation
        PlanPassationDTO planPassationDTO = planPassationMapper.toDto(planPassation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanPassationMockMvc.perform(put("/api/plan-passations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planPassationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlanPassation in the database
        List<PlanPassation> planPassationList = planPassationRepository.findAll();
        assertThat(planPassationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlanPassation() throws Exception {
        // Initialize the database
        planPassationRepository.saveAndFlush(planPassation);

        int databaseSizeBeforeDelete = planPassationRepository.findAll().size();

        // Delete the planPassation
        restPlanPassationMockMvc.perform(delete("/api/plan-passations/{id}", planPassation.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlanPassation> planPassationList = planPassationRepository.findAll();
        assertThat(planPassationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
