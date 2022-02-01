package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.PlanpassationmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.Realisation;
import sn.ssi.sigmap.domain.PlanPassation;
import sn.ssi.sigmap.repository.RealisationRepository;
import sn.ssi.sigmap.service.RealisationService;
import sn.ssi.sigmap.service.dto.RealisationDTO;
import sn.ssi.sigmap.service.mapper.RealisationMapper;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RealisationResource} REST controller.
 */
@SpringBootTest(classes = { PlanpassationmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class RealisationResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_ATTRIBUTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ATTRIBUTION = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_DELAIEXECUTION = 1;
    private static final Integer UPDATED_DELAIEXECUTION = 2;

    private static final String DEFAULT_OBJET = "AAAAAAAAAA";
    private static final String UPDATED_OBJET = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MONTANT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT = new BigDecimal(2);

    private static final Integer DEFAULT_EXAMEN_DNCMP = 1;
    private static final Integer UPDATED_EXAMEN_DNCMP = 2;

    private static final Integer DEFAULT_EXAMEN_PTF = 1;
    private static final Integer UPDATED_EXAMEN_PTF = 2;

    private static final String DEFAULT_CHAPITRE_IMPUTATION = "AAAAAAAAAA";
    private static final String UPDATED_CHAPITRE_IMPUTATION = "BBBBBBBBBB";

    private static final String DEFAULT_AUTORISATION_ENGAGEMENT = "AAAAAAAAAA";
    private static final String UPDATED_AUTORISATION_ENGAGEMENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_RECEPTION_DOSSIER = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RECEPTION_DOSSIER = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_NON_OBJECTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NON_OBJECTION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_AUTORISATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_AUTORISATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_RECEP_NON_OBJECTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RECEP_NON_OBJECTION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_RECEP_DOSS_CORRIGE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RECEP_DOSS_CORRIGE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_PUB_PAR_PRMP = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PUB_PAR_PRMP = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_OUVERTURE_PLIS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OUVERTURE_PLIS = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_RECEP_NON_OBJECT_OCMP = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RECEP_NON_OBJECT_OCMP = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_RECEP_RAPPORT_EVA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RECEP_RAPPORT_EVA = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_RECEP_NON_OBJECT_PTF = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RECEP_NON_OBJECT_PTF = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_EXAMEN_JURIDIQUE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EXAMEN_JURIDIQUE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_NOTIF_CONTRAT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NOTIF_CONTRAT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_APPROBATION_CONTRAT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_APPROBATION_CONTRAT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private RealisationRepository realisationRepository;

    @Autowired
    private RealisationMapper realisationMapper;

    @Autowired
    private RealisationService realisationService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRealisationMockMvc;

    private Realisation realisation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Realisation createEntity(EntityManager em) {
        Realisation realisation = new Realisation()
            .libelle(DEFAULT_LIBELLE)
            .dateAttribution(DEFAULT_DATE_ATTRIBUTION)
            .delaiexecution(DEFAULT_DELAIEXECUTION)
            .objet(DEFAULT_OBJET)
            .montant(DEFAULT_MONTANT)
            .examenDncmp(DEFAULT_EXAMEN_DNCMP)
            .examenPtf(DEFAULT_EXAMEN_PTF)
            .chapitreImputation(DEFAULT_CHAPITRE_IMPUTATION)
            .autorisationEngagement(DEFAULT_AUTORISATION_ENGAGEMENT)
            .dateReceptionDossier(DEFAULT_DATE_RECEPTION_DOSSIER)
            .dateNonObjection(DEFAULT_DATE_NON_OBJECTION)
            .dateAutorisation(DEFAULT_DATE_AUTORISATION)
            .dateRecepNonObjection(DEFAULT_DATE_RECEP_NON_OBJECTION)
            .dateRecepDossCorrige(DEFAULT_DATE_RECEP_DOSS_CORRIGE)
            .datePubParPrmp(DEFAULT_DATE_PUB_PAR_PRMP)
            .dateOuverturePlis(DEFAULT_DATE_OUVERTURE_PLIS)
            .dateRecepNonObjectOcmp(DEFAULT_DATE_RECEP_NON_OBJECT_OCMP)
            .dateRecepRapportEva(DEFAULT_DATE_RECEP_RAPPORT_EVA)
            .dateRecepNonObjectPtf(DEFAULT_DATE_RECEP_NON_OBJECT_PTF)
            .dateExamenJuridique(DEFAULT_DATE_EXAMEN_JURIDIQUE)
            .dateNotifContrat(DEFAULT_DATE_NOTIF_CONTRAT)
            .dateApprobationContrat(DEFAULT_DATE_APPROBATION_CONTRAT);
        // Add required entity
        PlanPassation planPassation;
        if (TestUtil.findAll(em, PlanPassation.class).isEmpty()) {
            planPassation = PlanPassationResourceIT.createEntity(em);
            em.persist(planPassation);
            em.flush();
        } else {
            planPassation = TestUtil.findAll(em, PlanPassation.class).get(0);
        }
        realisation.setPlanPassation(planPassation);
        return realisation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Realisation createUpdatedEntity(EntityManager em) {
        Realisation realisation = new Realisation()
            .libelle(UPDATED_LIBELLE)
            .dateAttribution(UPDATED_DATE_ATTRIBUTION)
            .delaiexecution(UPDATED_DELAIEXECUTION)
            .objet(UPDATED_OBJET)
            .montant(UPDATED_MONTANT)
            .examenDncmp(UPDATED_EXAMEN_DNCMP)
            .examenPtf(UPDATED_EXAMEN_PTF)
            .chapitreImputation(UPDATED_CHAPITRE_IMPUTATION)
            .autorisationEngagement(UPDATED_AUTORISATION_ENGAGEMENT)
            .dateReceptionDossier(UPDATED_DATE_RECEPTION_DOSSIER)
            .dateNonObjection(UPDATED_DATE_NON_OBJECTION)
            .dateAutorisation(UPDATED_DATE_AUTORISATION)
            .dateRecepNonObjection(UPDATED_DATE_RECEP_NON_OBJECTION)
            .dateRecepDossCorrige(UPDATED_DATE_RECEP_DOSS_CORRIGE)
            .datePubParPrmp(UPDATED_DATE_PUB_PAR_PRMP)
            .dateOuverturePlis(UPDATED_DATE_OUVERTURE_PLIS)
            .dateRecepNonObjectOcmp(UPDATED_DATE_RECEP_NON_OBJECT_OCMP)
            .dateRecepRapportEva(UPDATED_DATE_RECEP_RAPPORT_EVA)
            .dateRecepNonObjectPtf(UPDATED_DATE_RECEP_NON_OBJECT_PTF)
            .dateExamenJuridique(UPDATED_DATE_EXAMEN_JURIDIQUE)
            .dateNotifContrat(UPDATED_DATE_NOTIF_CONTRAT)
            .dateApprobationContrat(UPDATED_DATE_APPROBATION_CONTRAT);
        // Add required entity
        PlanPassation planPassation;
        if (TestUtil.findAll(em, PlanPassation.class).isEmpty()) {
            planPassation = PlanPassationResourceIT.createUpdatedEntity(em);
            em.persist(planPassation);
            em.flush();
        } else {
            planPassation = TestUtil.findAll(em, PlanPassation.class).get(0);
        }
        realisation.setPlanPassation(planPassation);
        return realisation;
    }

    @BeforeEach
    public void initTest() {
        realisation = createEntity(em);
    }

    @Test
    @Transactional
    public void createRealisation() throws Exception {
        int databaseSizeBeforeCreate = realisationRepository.findAll().size();
        // Create the Realisation
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);
        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isCreated());

        // Validate the Realisation in the database
        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeCreate + 1);
        Realisation testRealisation = realisationList.get(realisationList.size() - 1);
        assertThat(testRealisation.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testRealisation.getDateAttribution()).isEqualTo(DEFAULT_DATE_ATTRIBUTION);
        assertThat(testRealisation.getDelaiexecution()).isEqualTo(DEFAULT_DELAIEXECUTION);
        assertThat(testRealisation.getObjet()).isEqualTo(DEFAULT_OBJET);
        assertThat(testRealisation.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testRealisation.getExamenDncmp()).isEqualTo(DEFAULT_EXAMEN_DNCMP);
        assertThat(testRealisation.getExamenPtf()).isEqualTo(DEFAULT_EXAMEN_PTF);
        assertThat(testRealisation.getChapitreImputation()).isEqualTo(DEFAULT_CHAPITRE_IMPUTATION);
        assertThat(testRealisation.getAutorisationEngagement()).isEqualTo(DEFAULT_AUTORISATION_ENGAGEMENT);
        assertThat(testRealisation.getDateReceptionDossier()).isEqualTo(DEFAULT_DATE_RECEPTION_DOSSIER);
        assertThat(testRealisation.getDateNonObjection()).isEqualTo(DEFAULT_DATE_NON_OBJECTION);
        assertThat(testRealisation.getDateAutorisation()).isEqualTo(DEFAULT_DATE_AUTORISATION);
        assertThat(testRealisation.getDateRecepNonObjection()).isEqualTo(DEFAULT_DATE_RECEP_NON_OBJECTION);
        assertThat(testRealisation.getDateRecepDossCorrige()).isEqualTo(DEFAULT_DATE_RECEP_DOSS_CORRIGE);
        assertThat(testRealisation.getDatePubParPrmp()).isEqualTo(DEFAULT_DATE_PUB_PAR_PRMP);
        assertThat(testRealisation.getDateOuverturePlis()).isEqualTo(DEFAULT_DATE_OUVERTURE_PLIS);
        assertThat(testRealisation.getDateRecepNonObjectOcmp()).isEqualTo(DEFAULT_DATE_RECEP_NON_OBJECT_OCMP);
        assertThat(testRealisation.getDateRecepRapportEva()).isEqualTo(DEFAULT_DATE_RECEP_RAPPORT_EVA);
        assertThat(testRealisation.getDateRecepNonObjectPtf()).isEqualTo(DEFAULT_DATE_RECEP_NON_OBJECT_PTF);
        assertThat(testRealisation.getDateExamenJuridique()).isEqualTo(DEFAULT_DATE_EXAMEN_JURIDIQUE);
        assertThat(testRealisation.getDateNotifContrat()).isEqualTo(DEFAULT_DATE_NOTIF_CONTRAT);
        assertThat(testRealisation.getDateApprobationContrat()).isEqualTo(DEFAULT_DATE_APPROBATION_CONTRAT);
    }

    @Test
    @Transactional
    public void createRealisationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = realisationRepository.findAll().size();

        // Create the Realisation with an existing ID
        realisation.setId(1L);
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Realisation in the database
        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDateAttributionIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setDateAttribution(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDelaiexecutionIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setDelaiexecution(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExamenDncmpIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setExamenDncmp(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExamenPtfIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setExamenPtf(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChapitreImputationIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setChapitreImputation(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAutorisationEngagementIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setAutorisationEngagement(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateReceptionDossierIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setDateReceptionDossier(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateNonObjectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setDateNonObjection(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateAutorisationIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setDateAutorisation(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateRecepNonObjectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setDateRecepNonObjection(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateRecepDossCorrigeIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setDateRecepDossCorrige(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDatePubParPrmpIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setDatePubParPrmp(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateOuverturePlisIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setDateOuverturePlis(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateRecepNonObjectOcmpIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setDateRecepNonObjectOcmp(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateRecepRapportEvaIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setDateRecepRapportEva(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateRecepNonObjectPtfIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setDateRecepNonObjectPtf(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateExamenJuridiqueIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setDateExamenJuridique(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateNotifContratIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setDateNotifContrat(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateApprobationContratIsRequired() throws Exception {
        int databaseSizeBeforeTest = realisationRepository.findAll().size();
        // set the field null
        realisation.setDateApprobationContrat(null);

        // Create the Realisation, which fails.
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);


        restRealisationMockMvc.perform(post("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRealisations() throws Exception {
        // Initialize the database
        realisationRepository.saveAndFlush(realisation);

        // Get all the realisationList
        restRealisationMockMvc.perform(get("/api/realisations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(realisation.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].dateAttribution").value(hasItem(DEFAULT_DATE_ATTRIBUTION.toString())))
            .andExpect(jsonPath("$.[*].delaiexecution").value(hasItem(DEFAULT_DELAIEXECUTION)))
            .andExpect(jsonPath("$.[*].objet").value(hasItem(DEFAULT_OBJET)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
            .andExpect(jsonPath("$.[*].examenDncmp").value(hasItem(DEFAULT_EXAMEN_DNCMP)))
            .andExpect(jsonPath("$.[*].examenPtf").value(hasItem(DEFAULT_EXAMEN_PTF)))
            .andExpect(jsonPath("$.[*].chapitreImputation").value(hasItem(DEFAULT_CHAPITRE_IMPUTATION)))
            .andExpect(jsonPath("$.[*].autorisationEngagement").value(hasItem(DEFAULT_AUTORISATION_ENGAGEMENT)))
            .andExpect(jsonPath("$.[*].dateReceptionDossier").value(hasItem(DEFAULT_DATE_RECEPTION_DOSSIER.toString())))
            .andExpect(jsonPath("$.[*].dateNonObjection").value(hasItem(DEFAULT_DATE_NON_OBJECTION.toString())))
            .andExpect(jsonPath("$.[*].dateAutorisation").value(hasItem(DEFAULT_DATE_AUTORISATION.toString())))
            .andExpect(jsonPath("$.[*].dateRecepNonObjection").value(hasItem(DEFAULT_DATE_RECEP_NON_OBJECTION.toString())))
            .andExpect(jsonPath("$.[*].dateRecepDossCorrige").value(hasItem(DEFAULT_DATE_RECEP_DOSS_CORRIGE.toString())))
            .andExpect(jsonPath("$.[*].datePubParPrmp").value(hasItem(DEFAULT_DATE_PUB_PAR_PRMP.toString())))
            .andExpect(jsonPath("$.[*].dateOuverturePlis").value(hasItem(DEFAULT_DATE_OUVERTURE_PLIS.toString())))
            .andExpect(jsonPath("$.[*].dateRecepNonObjectOcmp").value(hasItem(DEFAULT_DATE_RECEP_NON_OBJECT_OCMP.toString())))
            .andExpect(jsonPath("$.[*].dateRecepRapportEva").value(hasItem(DEFAULT_DATE_RECEP_RAPPORT_EVA.toString())))
            .andExpect(jsonPath("$.[*].dateRecepNonObjectPtf").value(hasItem(DEFAULT_DATE_RECEP_NON_OBJECT_PTF.toString())))
            .andExpect(jsonPath("$.[*].dateExamenJuridique").value(hasItem(DEFAULT_DATE_EXAMEN_JURIDIQUE.toString())))
            .andExpect(jsonPath("$.[*].dateNotifContrat").value(hasItem(DEFAULT_DATE_NOTIF_CONTRAT.toString())))
            .andExpect(jsonPath("$.[*].dateApprobationContrat").value(hasItem(DEFAULT_DATE_APPROBATION_CONTRAT.toString())));
    }
    
    @Test
    @Transactional
    public void getRealisation() throws Exception {
        // Initialize the database
        realisationRepository.saveAndFlush(realisation);

        // Get the realisation
        restRealisationMockMvc.perform(get("/api/realisations/{id}", realisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(realisation.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.dateAttribution").value(DEFAULT_DATE_ATTRIBUTION.toString()))
            .andExpect(jsonPath("$.delaiexecution").value(DEFAULT_DELAIEXECUTION))
            .andExpect(jsonPath("$.objet").value(DEFAULT_OBJET))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.intValue()))
            .andExpect(jsonPath("$.examenDncmp").value(DEFAULT_EXAMEN_DNCMP))
            .andExpect(jsonPath("$.examenPtf").value(DEFAULT_EXAMEN_PTF))
            .andExpect(jsonPath("$.chapitreImputation").value(DEFAULT_CHAPITRE_IMPUTATION))
            .andExpect(jsonPath("$.autorisationEngagement").value(DEFAULT_AUTORISATION_ENGAGEMENT))
            .andExpect(jsonPath("$.dateReceptionDossier").value(DEFAULT_DATE_RECEPTION_DOSSIER.toString()))
            .andExpect(jsonPath("$.dateNonObjection").value(DEFAULT_DATE_NON_OBJECTION.toString()))
            .andExpect(jsonPath("$.dateAutorisation").value(DEFAULT_DATE_AUTORISATION.toString()))
            .andExpect(jsonPath("$.dateRecepNonObjection").value(DEFAULT_DATE_RECEP_NON_OBJECTION.toString()))
            .andExpect(jsonPath("$.dateRecepDossCorrige").value(DEFAULT_DATE_RECEP_DOSS_CORRIGE.toString()))
            .andExpect(jsonPath("$.datePubParPrmp").value(DEFAULT_DATE_PUB_PAR_PRMP.toString()))
            .andExpect(jsonPath("$.dateOuverturePlis").value(DEFAULT_DATE_OUVERTURE_PLIS.toString()))
            .andExpect(jsonPath("$.dateRecepNonObjectOcmp").value(DEFAULT_DATE_RECEP_NON_OBJECT_OCMP.toString()))
            .andExpect(jsonPath("$.dateRecepRapportEva").value(DEFAULT_DATE_RECEP_RAPPORT_EVA.toString()))
            .andExpect(jsonPath("$.dateRecepNonObjectPtf").value(DEFAULT_DATE_RECEP_NON_OBJECT_PTF.toString()))
            .andExpect(jsonPath("$.dateExamenJuridique").value(DEFAULT_DATE_EXAMEN_JURIDIQUE.toString()))
            .andExpect(jsonPath("$.dateNotifContrat").value(DEFAULT_DATE_NOTIF_CONTRAT.toString()))
            .andExpect(jsonPath("$.dateApprobationContrat").value(DEFAULT_DATE_APPROBATION_CONTRAT.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingRealisation() throws Exception {
        // Get the realisation
        restRealisationMockMvc.perform(get("/api/realisations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRealisation() throws Exception {
        // Initialize the database
        realisationRepository.saveAndFlush(realisation);

        int databaseSizeBeforeUpdate = realisationRepository.findAll().size();

        // Update the realisation
        Realisation updatedRealisation = realisationRepository.findById(realisation.getId()).get();
        // Disconnect from session so that the updates on updatedRealisation are not directly saved in db
        em.detach(updatedRealisation);
        updatedRealisation
            .libelle(UPDATED_LIBELLE)
            .dateAttribution(UPDATED_DATE_ATTRIBUTION)
            .delaiexecution(UPDATED_DELAIEXECUTION)
            .objet(UPDATED_OBJET)
            .montant(UPDATED_MONTANT)
            .examenDncmp(UPDATED_EXAMEN_DNCMP)
            .examenPtf(UPDATED_EXAMEN_PTF)
            .chapitreImputation(UPDATED_CHAPITRE_IMPUTATION)
            .autorisationEngagement(UPDATED_AUTORISATION_ENGAGEMENT)
            .dateReceptionDossier(UPDATED_DATE_RECEPTION_DOSSIER)
            .dateNonObjection(UPDATED_DATE_NON_OBJECTION)
            .dateAutorisation(UPDATED_DATE_AUTORISATION)
            .dateRecepNonObjection(UPDATED_DATE_RECEP_NON_OBJECTION)
            .dateRecepDossCorrige(UPDATED_DATE_RECEP_DOSS_CORRIGE)
            .datePubParPrmp(UPDATED_DATE_PUB_PAR_PRMP)
            .dateOuverturePlis(UPDATED_DATE_OUVERTURE_PLIS)
            .dateRecepNonObjectOcmp(UPDATED_DATE_RECEP_NON_OBJECT_OCMP)
            .dateRecepRapportEva(UPDATED_DATE_RECEP_RAPPORT_EVA)
            .dateRecepNonObjectPtf(UPDATED_DATE_RECEP_NON_OBJECT_PTF)
            .dateExamenJuridique(UPDATED_DATE_EXAMEN_JURIDIQUE)
            .dateNotifContrat(UPDATED_DATE_NOTIF_CONTRAT)
            .dateApprobationContrat(UPDATED_DATE_APPROBATION_CONTRAT);
        RealisationDTO realisationDTO = realisationMapper.toDto(updatedRealisation);

        restRealisationMockMvc.perform(put("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isOk());

        // Validate the Realisation in the database
        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeUpdate);
        Realisation testRealisation = realisationList.get(realisationList.size() - 1);
        assertThat(testRealisation.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testRealisation.getDateAttribution()).isEqualTo(UPDATED_DATE_ATTRIBUTION);
        assertThat(testRealisation.getDelaiexecution()).isEqualTo(UPDATED_DELAIEXECUTION);
        assertThat(testRealisation.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testRealisation.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testRealisation.getExamenDncmp()).isEqualTo(UPDATED_EXAMEN_DNCMP);
        assertThat(testRealisation.getExamenPtf()).isEqualTo(UPDATED_EXAMEN_PTF);
        assertThat(testRealisation.getChapitreImputation()).isEqualTo(UPDATED_CHAPITRE_IMPUTATION);
        assertThat(testRealisation.getAutorisationEngagement()).isEqualTo(UPDATED_AUTORISATION_ENGAGEMENT);
        assertThat(testRealisation.getDateReceptionDossier()).isEqualTo(UPDATED_DATE_RECEPTION_DOSSIER);
        assertThat(testRealisation.getDateNonObjection()).isEqualTo(UPDATED_DATE_NON_OBJECTION);
        assertThat(testRealisation.getDateAutorisation()).isEqualTo(UPDATED_DATE_AUTORISATION);
        assertThat(testRealisation.getDateRecepNonObjection()).isEqualTo(UPDATED_DATE_RECEP_NON_OBJECTION);
        assertThat(testRealisation.getDateRecepDossCorrige()).isEqualTo(UPDATED_DATE_RECEP_DOSS_CORRIGE);
        assertThat(testRealisation.getDatePubParPrmp()).isEqualTo(UPDATED_DATE_PUB_PAR_PRMP);
        assertThat(testRealisation.getDateOuverturePlis()).isEqualTo(UPDATED_DATE_OUVERTURE_PLIS);
        assertThat(testRealisation.getDateRecepNonObjectOcmp()).isEqualTo(UPDATED_DATE_RECEP_NON_OBJECT_OCMP);
        assertThat(testRealisation.getDateRecepRapportEva()).isEqualTo(UPDATED_DATE_RECEP_RAPPORT_EVA);
        assertThat(testRealisation.getDateRecepNonObjectPtf()).isEqualTo(UPDATED_DATE_RECEP_NON_OBJECT_PTF);
        assertThat(testRealisation.getDateExamenJuridique()).isEqualTo(UPDATED_DATE_EXAMEN_JURIDIQUE);
        assertThat(testRealisation.getDateNotifContrat()).isEqualTo(UPDATED_DATE_NOTIF_CONTRAT);
        assertThat(testRealisation.getDateApprobationContrat()).isEqualTo(UPDATED_DATE_APPROBATION_CONTRAT);
    }

    @Test
    @Transactional
    public void updateNonExistingRealisation() throws Exception {
        int databaseSizeBeforeUpdate = realisationRepository.findAll().size();

        // Create the Realisation
        RealisationDTO realisationDTO = realisationMapper.toDto(realisation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRealisationMockMvc.perform(put("/api/realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(realisationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Realisation in the database
        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRealisation() throws Exception {
        // Initialize the database
        realisationRepository.saveAndFlush(realisation);

        int databaseSizeBeforeDelete = realisationRepository.findAll().size();

        // Delete the realisation
        restRealisationMockMvc.perform(delete("/api/realisations/{id}", realisation.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Realisation> realisationList = realisationRepository.findAll();
        assertThat(realisationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
