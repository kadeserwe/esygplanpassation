package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.PlanpassationmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.SygRealisation;
import sn.ssi.sigmap.repository.SygRealisationRepository;
import sn.ssi.sigmap.service.SygRealisationService;

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
 * Integration tests for the {@link SygRealisationResource} REST controller.
 */
@SpringBootTest(classes = { PlanpassationmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SygRealisationResourceIT {

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
    private SygRealisationRepository sygRealisationRepository;

    @Autowired
    private SygRealisationService sygRealisationService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSygRealisationMockMvc;

    private SygRealisation sygRealisation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SygRealisation createEntity(EntityManager em) {
        SygRealisation sygRealisation = new SygRealisation()
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
        return sygRealisation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SygRealisation createUpdatedEntity(EntityManager em) {
        SygRealisation sygRealisation = new SygRealisation()
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
        return sygRealisation;
    }

    @BeforeEach
    public void initTest() {
        sygRealisation = createEntity(em);
    }

    @Test
    @Transactional
    public void createSygRealisation() throws Exception {
        int databaseSizeBeforeCreate = sygRealisationRepository.findAll().size();
        // Create the SygRealisation
        restSygRealisationMockMvc.perform(post("/api/syg-realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygRealisation)))
            .andExpect(status().isCreated());

        // Validate the SygRealisation in the database
        List<SygRealisation> sygRealisationList = sygRealisationRepository.findAll();
        assertThat(sygRealisationList).hasSize(databaseSizeBeforeCreate + 1);
        SygRealisation testSygRealisation = sygRealisationList.get(sygRealisationList.size() - 1);
        assertThat(testSygRealisation.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testSygRealisation.getDateAttribution()).isEqualTo(DEFAULT_DATE_ATTRIBUTION);
        assertThat(testSygRealisation.getDelaiexecution()).isEqualTo(DEFAULT_DELAIEXECUTION);
        assertThat(testSygRealisation.getObjet()).isEqualTo(DEFAULT_OBJET);
        assertThat(testSygRealisation.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testSygRealisation.getExamenDncmp()).isEqualTo(DEFAULT_EXAMEN_DNCMP);
        assertThat(testSygRealisation.getExamenPtf()).isEqualTo(DEFAULT_EXAMEN_PTF);
        assertThat(testSygRealisation.getChapitreImputation()).isEqualTo(DEFAULT_CHAPITRE_IMPUTATION);
        assertThat(testSygRealisation.getAutorisationEngagement()).isEqualTo(DEFAULT_AUTORISATION_ENGAGEMENT);
        assertThat(testSygRealisation.getDateReceptionDossier()).isEqualTo(DEFAULT_DATE_RECEPTION_DOSSIER);
        assertThat(testSygRealisation.getDateNonObjection()).isEqualTo(DEFAULT_DATE_NON_OBJECTION);
        assertThat(testSygRealisation.getDateAutorisation()).isEqualTo(DEFAULT_DATE_AUTORISATION);
        assertThat(testSygRealisation.getDateRecepNonObjection()).isEqualTo(DEFAULT_DATE_RECEP_NON_OBJECTION);
        assertThat(testSygRealisation.getDateRecepDossCorrige()).isEqualTo(DEFAULT_DATE_RECEP_DOSS_CORRIGE);
        assertThat(testSygRealisation.getDatePubParPrmp()).isEqualTo(DEFAULT_DATE_PUB_PAR_PRMP);
        assertThat(testSygRealisation.getDateOuverturePlis()).isEqualTo(DEFAULT_DATE_OUVERTURE_PLIS);
        assertThat(testSygRealisation.getDateRecepNonObjectOcmp()).isEqualTo(DEFAULT_DATE_RECEP_NON_OBJECT_OCMP);
        assertThat(testSygRealisation.getDateRecepRapportEva()).isEqualTo(DEFAULT_DATE_RECEP_RAPPORT_EVA);
        assertThat(testSygRealisation.getDateRecepNonObjectPtf()).isEqualTo(DEFAULT_DATE_RECEP_NON_OBJECT_PTF);
        assertThat(testSygRealisation.getDateExamenJuridique()).isEqualTo(DEFAULT_DATE_EXAMEN_JURIDIQUE);
        assertThat(testSygRealisation.getDateNotifContrat()).isEqualTo(DEFAULT_DATE_NOTIF_CONTRAT);
        assertThat(testSygRealisation.getDateApprobationContrat()).isEqualTo(DEFAULT_DATE_APPROBATION_CONTRAT);
    }

    @Test
    @Transactional
    public void createSygRealisationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sygRealisationRepository.findAll().size();

        // Create the SygRealisation with an existing ID
        sygRealisation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSygRealisationMockMvc.perform(post("/api/syg-realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygRealisation)))
            .andExpect(status().isBadRequest());

        // Validate the SygRealisation in the database
        List<SygRealisation> sygRealisationList = sygRealisationRepository.findAll();
        assertThat(sygRealisationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sygRealisationRepository.findAll().size();
        // set the field null
        sygRealisation.setLibelle(null);

        // Create the SygRealisation, which fails.


        restSygRealisationMockMvc.perform(post("/api/syg-realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygRealisation)))
            .andExpect(status().isBadRequest());

        List<SygRealisation> sygRealisationList = sygRealisationRepository.findAll();
        assertThat(sygRealisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateAttributionIsRequired() throws Exception {
        int databaseSizeBeforeTest = sygRealisationRepository.findAll().size();
        // set the field null
        sygRealisation.setDateAttribution(null);

        // Create the SygRealisation, which fails.


        restSygRealisationMockMvc.perform(post("/api/syg-realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygRealisation)))
            .andExpect(status().isBadRequest());

        List<SygRealisation> sygRealisationList = sygRealisationRepository.findAll();
        assertThat(sygRealisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSygRealisations() throws Exception {
        // Initialize the database
        sygRealisationRepository.saveAndFlush(sygRealisation);

        // Get all the sygRealisationList
        restSygRealisationMockMvc.perform(get("/api/syg-realisations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sygRealisation.getId().intValue())))
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
    public void getSygRealisation() throws Exception {
        // Initialize the database
        sygRealisationRepository.saveAndFlush(sygRealisation);

        // Get the sygRealisation
        restSygRealisationMockMvc.perform(get("/api/syg-realisations/{id}", sygRealisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sygRealisation.getId().intValue()))
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
    public void getNonExistingSygRealisation() throws Exception {
        // Get the sygRealisation
        restSygRealisationMockMvc.perform(get("/api/syg-realisations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSygRealisation() throws Exception {
        // Initialize the database
        sygRealisationService.save(sygRealisation);

        int databaseSizeBeforeUpdate = sygRealisationRepository.findAll().size();

        // Update the sygRealisation
        SygRealisation updatedSygRealisation = sygRealisationRepository.findById(sygRealisation.getId()).get();
        // Disconnect from session so that the updates on updatedSygRealisation are not directly saved in db
        em.detach(updatedSygRealisation);
        updatedSygRealisation
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

        restSygRealisationMockMvc.perform(put("/api/syg-realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSygRealisation)))
            .andExpect(status().isOk());

        // Validate the SygRealisation in the database
        List<SygRealisation> sygRealisationList = sygRealisationRepository.findAll();
        assertThat(sygRealisationList).hasSize(databaseSizeBeforeUpdate);
        SygRealisation testSygRealisation = sygRealisationList.get(sygRealisationList.size() - 1);
        assertThat(testSygRealisation.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSygRealisation.getDateAttribution()).isEqualTo(UPDATED_DATE_ATTRIBUTION);
        assertThat(testSygRealisation.getDelaiexecution()).isEqualTo(UPDATED_DELAIEXECUTION);
        assertThat(testSygRealisation.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testSygRealisation.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testSygRealisation.getExamenDncmp()).isEqualTo(UPDATED_EXAMEN_DNCMP);
        assertThat(testSygRealisation.getExamenPtf()).isEqualTo(UPDATED_EXAMEN_PTF);
        assertThat(testSygRealisation.getChapitreImputation()).isEqualTo(UPDATED_CHAPITRE_IMPUTATION);
        assertThat(testSygRealisation.getAutorisationEngagement()).isEqualTo(UPDATED_AUTORISATION_ENGAGEMENT);
        assertThat(testSygRealisation.getDateReceptionDossier()).isEqualTo(UPDATED_DATE_RECEPTION_DOSSIER);
        assertThat(testSygRealisation.getDateNonObjection()).isEqualTo(UPDATED_DATE_NON_OBJECTION);
        assertThat(testSygRealisation.getDateAutorisation()).isEqualTo(UPDATED_DATE_AUTORISATION);
        assertThat(testSygRealisation.getDateRecepNonObjection()).isEqualTo(UPDATED_DATE_RECEP_NON_OBJECTION);
        assertThat(testSygRealisation.getDateRecepDossCorrige()).isEqualTo(UPDATED_DATE_RECEP_DOSS_CORRIGE);
        assertThat(testSygRealisation.getDatePubParPrmp()).isEqualTo(UPDATED_DATE_PUB_PAR_PRMP);
        assertThat(testSygRealisation.getDateOuverturePlis()).isEqualTo(UPDATED_DATE_OUVERTURE_PLIS);
        assertThat(testSygRealisation.getDateRecepNonObjectOcmp()).isEqualTo(UPDATED_DATE_RECEP_NON_OBJECT_OCMP);
        assertThat(testSygRealisation.getDateRecepRapportEva()).isEqualTo(UPDATED_DATE_RECEP_RAPPORT_EVA);
        assertThat(testSygRealisation.getDateRecepNonObjectPtf()).isEqualTo(UPDATED_DATE_RECEP_NON_OBJECT_PTF);
        assertThat(testSygRealisation.getDateExamenJuridique()).isEqualTo(UPDATED_DATE_EXAMEN_JURIDIQUE);
        assertThat(testSygRealisation.getDateNotifContrat()).isEqualTo(UPDATED_DATE_NOTIF_CONTRAT);
        assertThat(testSygRealisation.getDateApprobationContrat()).isEqualTo(UPDATED_DATE_APPROBATION_CONTRAT);
    }

    @Test
    @Transactional
    public void updateNonExistingSygRealisation() throws Exception {
        int databaseSizeBeforeUpdate = sygRealisationRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSygRealisationMockMvc.perform(put("/api/syg-realisations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sygRealisation)))
            .andExpect(status().isBadRequest());

        // Validate the SygRealisation in the database
        List<SygRealisation> sygRealisationList = sygRealisationRepository.findAll();
        assertThat(sygRealisationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSygRealisation() throws Exception {
        // Initialize the database
        sygRealisationService.save(sygRealisation);

        int databaseSizeBeforeDelete = sygRealisationRepository.findAll().size();

        // Delete the sygRealisation
        restSygRealisationMockMvc.perform(delete("/api/syg-realisations/{id}", sygRealisation.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SygRealisation> sygRealisationList = sygRealisationRepository.findAll();
        assertThat(sygRealisationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
