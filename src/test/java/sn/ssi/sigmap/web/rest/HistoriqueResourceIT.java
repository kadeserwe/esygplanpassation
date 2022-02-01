package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.PlanpassationmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.Historique;
import sn.ssi.sigmap.domain.PlanPassation;
import sn.ssi.sigmap.repository.HistoriqueRepository;
import sn.ssi.sigmap.service.HistoriqueService;
import sn.ssi.sigmap.service.dto.HistoriqueDTO;
import sn.ssi.sigmap.service.mapper.HistoriqueMapper;

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
 * Integration tests for the {@link HistoriqueResource} REST controller.
 */
@SpringBootTest(classes = { PlanpassationmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class HistoriqueResourceIT {

    private static final LocalDate DEFAULT_DATE_REJET = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_REJET = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_RJET_2 = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RJET_2 = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MISE_VALIDATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MISE_VALIDATION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_COMMENTAIRE_REJET = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE_REJET = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE_MISE_VALIDATION = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE_MISE_VALIDATION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FICHIER_MISE_VALIDATION = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER_MISE_VALIDATION = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FICHIER_MISE_VALIDATION_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_MISE_VALIDATION_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_FICHIER_REJET = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER_REJET = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FICHIER_REJET_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_REJET_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT_2 = "AAAAAAAAAA";
    private static final String UPDATED_ETAT_2 = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE_REJET_2 = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE_REJET_2 = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FICHIER_REJET_2 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER_REJET_2 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FICHIER_REJET_2_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_REJET_2_CONTENT_TYPE = "image/png";

    @Autowired
    private HistoriqueRepository historiqueRepository;

    @Autowired
    private HistoriqueMapper historiqueMapper;

    @Autowired
    private HistoriqueService historiqueService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriqueMockMvc;

    private Historique historique;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historique createEntity(EntityManager em) {
        Historique historique = new Historique()
            .dateRejet(DEFAULT_DATE_REJET)
            .dateRjet2(DEFAULT_DATE_RJET_2)
            .dateMiseValidation(DEFAULT_DATE_MISE_VALIDATION)
            .commentaireRejet(DEFAULT_COMMENTAIRE_REJET)
            .commentaireMiseValidation(DEFAULT_COMMENTAIRE_MISE_VALIDATION)
            .fichierMiseValidation(DEFAULT_FICHIER_MISE_VALIDATION)
            .fichierMiseValidationContentType(DEFAULT_FICHIER_MISE_VALIDATION_CONTENT_TYPE)
            .fichierRejet(DEFAULT_FICHIER_REJET)
            .fichierRejetContentType(DEFAULT_FICHIER_REJET_CONTENT_TYPE)
            .etat(DEFAULT_ETAT)
            .etat2(DEFAULT_ETAT_2)
            .commentaireRejet2(DEFAULT_COMMENTAIRE_REJET_2)
            .fichierRejet2(DEFAULT_FICHIER_REJET_2)
            .fichierRejet2ContentType(DEFAULT_FICHIER_REJET_2_CONTENT_TYPE);
        // Add required entity
        PlanPassation planPassation;
        if (TestUtil.findAll(em, PlanPassation.class).isEmpty()) {
            planPassation = PlanPassationResourceIT.createEntity(em);
            em.persist(planPassation);
            em.flush();
        } else {
            planPassation = TestUtil.findAll(em, PlanPassation.class).get(0);
        }
        historique.setPlanPassation(planPassation);
        return historique;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historique createUpdatedEntity(EntityManager em) {
        Historique historique = new Historique()
            .dateRejet(UPDATED_DATE_REJET)
            .dateRjet2(UPDATED_DATE_RJET_2)
            .dateMiseValidation(UPDATED_DATE_MISE_VALIDATION)
            .commentaireRejet(UPDATED_COMMENTAIRE_REJET)
            .commentaireMiseValidation(UPDATED_COMMENTAIRE_MISE_VALIDATION)
            .fichierMiseValidation(UPDATED_FICHIER_MISE_VALIDATION)
            .fichierMiseValidationContentType(UPDATED_FICHIER_MISE_VALIDATION_CONTENT_TYPE)
            .fichierRejet(UPDATED_FICHIER_REJET)
            .fichierRejetContentType(UPDATED_FICHIER_REJET_CONTENT_TYPE)
            .etat(UPDATED_ETAT)
            .etat2(UPDATED_ETAT_2)
            .commentaireRejet2(UPDATED_COMMENTAIRE_REJET_2)
            .fichierRejet2(UPDATED_FICHIER_REJET_2)
            .fichierRejet2ContentType(UPDATED_FICHIER_REJET_2_CONTENT_TYPE);
        // Add required entity
        PlanPassation planPassation;
        if (TestUtil.findAll(em, PlanPassation.class).isEmpty()) {
            planPassation = PlanPassationResourceIT.createUpdatedEntity(em);
            em.persist(planPassation);
            em.flush();
        } else {
            planPassation = TestUtil.findAll(em, PlanPassation.class).get(0);
        }
        historique.setPlanPassation(planPassation);
        return historique;
    }

    @BeforeEach
    public void initTest() {
        historique = createEntity(em);
    }

    @Test
    @Transactional
    public void createHistorique() throws Exception {
        int databaseSizeBeforeCreate = historiqueRepository.findAll().size();
        // Create the Historique
        HistoriqueDTO historiqueDTO = historiqueMapper.toDto(historique);
        restHistoriqueMockMvc.perform(post("/api/historiques").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueDTO)))
            .andExpect(status().isCreated());

        // Validate the Historique in the database
        List<Historique> historiqueList = historiqueRepository.findAll();
        assertThat(historiqueList).hasSize(databaseSizeBeforeCreate + 1);
        Historique testHistorique = historiqueList.get(historiqueList.size() - 1);
        assertThat(testHistorique.getDateRejet()).isEqualTo(DEFAULT_DATE_REJET);
        assertThat(testHistorique.getDateRjet2()).isEqualTo(DEFAULT_DATE_RJET_2);
        assertThat(testHistorique.getDateMiseValidation()).isEqualTo(DEFAULT_DATE_MISE_VALIDATION);
        assertThat(testHistorique.getCommentaireRejet()).isEqualTo(DEFAULT_COMMENTAIRE_REJET);
        assertThat(testHistorique.getCommentaireMiseValidation()).isEqualTo(DEFAULT_COMMENTAIRE_MISE_VALIDATION);
        assertThat(testHistorique.getFichierMiseValidation()).isEqualTo(DEFAULT_FICHIER_MISE_VALIDATION);
        assertThat(testHistorique.getFichierMiseValidationContentType()).isEqualTo(DEFAULT_FICHIER_MISE_VALIDATION_CONTENT_TYPE);
        assertThat(testHistorique.getFichierRejet()).isEqualTo(DEFAULT_FICHIER_REJET);
        assertThat(testHistorique.getFichierRejetContentType()).isEqualTo(DEFAULT_FICHIER_REJET_CONTENT_TYPE);
        assertThat(testHistorique.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testHistorique.getEtat2()).isEqualTo(DEFAULT_ETAT_2);
        assertThat(testHistorique.getCommentaireRejet2()).isEqualTo(DEFAULT_COMMENTAIRE_REJET_2);
        assertThat(testHistorique.getFichierRejet2()).isEqualTo(DEFAULT_FICHIER_REJET_2);
        assertThat(testHistorique.getFichierRejet2ContentType()).isEqualTo(DEFAULT_FICHIER_REJET_2_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createHistoriqueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = historiqueRepository.findAll().size();

        // Create the Historique with an existing ID
        historique.setId(1L);
        HistoriqueDTO historiqueDTO = historiqueMapper.toDto(historique);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriqueMockMvc.perform(post("/api/historiques").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Historique in the database
        List<Historique> historiqueList = historiqueRepository.findAll();
        assertThat(historiqueList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllHistoriques() throws Exception {
        // Initialize the database
        historiqueRepository.saveAndFlush(historique);

        // Get all the historiqueList
        restHistoriqueMockMvc.perform(get("/api/historiques?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historique.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateRejet").value(hasItem(DEFAULT_DATE_REJET.toString())))
            .andExpect(jsonPath("$.[*].dateRjet2").value(hasItem(DEFAULT_DATE_RJET_2.toString())))
            .andExpect(jsonPath("$.[*].dateMiseValidation").value(hasItem(DEFAULT_DATE_MISE_VALIDATION.toString())))
            .andExpect(jsonPath("$.[*].commentaireRejet").value(hasItem(DEFAULT_COMMENTAIRE_REJET)))
            .andExpect(jsonPath("$.[*].commentaireMiseValidation").value(hasItem(DEFAULT_COMMENTAIRE_MISE_VALIDATION)))
            .andExpect(jsonPath("$.[*].fichierMiseValidationContentType").value(hasItem(DEFAULT_FICHIER_MISE_VALIDATION_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichierMiseValidation").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER_MISE_VALIDATION))))
            .andExpect(jsonPath("$.[*].fichierRejetContentType").value(hasItem(DEFAULT_FICHIER_REJET_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichierRejet").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER_REJET))))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].etat2").value(hasItem(DEFAULT_ETAT_2)))
            .andExpect(jsonPath("$.[*].commentaireRejet2").value(hasItem(DEFAULT_COMMENTAIRE_REJET_2)))
            .andExpect(jsonPath("$.[*].fichierRejet2ContentType").value(hasItem(DEFAULT_FICHIER_REJET_2_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichierRejet2").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER_REJET_2))));
    }
    
    @Test
    @Transactional
    public void getHistorique() throws Exception {
        // Initialize the database
        historiqueRepository.saveAndFlush(historique);

        // Get the historique
        restHistoriqueMockMvc.perform(get("/api/historiques/{id}", historique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historique.getId().intValue()))
            .andExpect(jsonPath("$.dateRejet").value(DEFAULT_DATE_REJET.toString()))
            .andExpect(jsonPath("$.dateRjet2").value(DEFAULT_DATE_RJET_2.toString()))
            .andExpect(jsonPath("$.dateMiseValidation").value(DEFAULT_DATE_MISE_VALIDATION.toString()))
            .andExpect(jsonPath("$.commentaireRejet").value(DEFAULT_COMMENTAIRE_REJET))
            .andExpect(jsonPath("$.commentaireMiseValidation").value(DEFAULT_COMMENTAIRE_MISE_VALIDATION))
            .andExpect(jsonPath("$.fichierMiseValidationContentType").value(DEFAULT_FICHIER_MISE_VALIDATION_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichierMiseValidation").value(Base64Utils.encodeToString(DEFAULT_FICHIER_MISE_VALIDATION)))
            .andExpect(jsonPath("$.fichierRejetContentType").value(DEFAULT_FICHIER_REJET_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichierRejet").value(Base64Utils.encodeToString(DEFAULT_FICHIER_REJET)))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT))
            .andExpect(jsonPath("$.etat2").value(DEFAULT_ETAT_2))
            .andExpect(jsonPath("$.commentaireRejet2").value(DEFAULT_COMMENTAIRE_REJET_2))
            .andExpect(jsonPath("$.fichierRejet2ContentType").value(DEFAULT_FICHIER_REJET_2_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichierRejet2").value(Base64Utils.encodeToString(DEFAULT_FICHIER_REJET_2)));
    }
    @Test
    @Transactional
    public void getNonExistingHistorique() throws Exception {
        // Get the historique
        restHistoriqueMockMvc.perform(get("/api/historiques/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistorique() throws Exception {
        // Initialize the database
        historiqueRepository.saveAndFlush(historique);

        int databaseSizeBeforeUpdate = historiqueRepository.findAll().size();

        // Update the historique
        Historique updatedHistorique = historiqueRepository.findById(historique.getId()).get();
        // Disconnect from session so that the updates on updatedHistorique are not directly saved in db
        em.detach(updatedHistorique);
        updatedHistorique
            .dateRejet(UPDATED_DATE_REJET)
            .dateRjet2(UPDATED_DATE_RJET_2)
            .dateMiseValidation(UPDATED_DATE_MISE_VALIDATION)
            .commentaireRejet(UPDATED_COMMENTAIRE_REJET)
            .commentaireMiseValidation(UPDATED_COMMENTAIRE_MISE_VALIDATION)
            .fichierMiseValidation(UPDATED_FICHIER_MISE_VALIDATION)
            .fichierMiseValidationContentType(UPDATED_FICHIER_MISE_VALIDATION_CONTENT_TYPE)
            .fichierRejet(UPDATED_FICHIER_REJET)
            .fichierRejetContentType(UPDATED_FICHIER_REJET_CONTENT_TYPE)
            .etat(UPDATED_ETAT)
            .etat2(UPDATED_ETAT_2)
            .commentaireRejet2(UPDATED_COMMENTAIRE_REJET_2)
            .fichierRejet2(UPDATED_FICHIER_REJET_2)
            .fichierRejet2ContentType(UPDATED_FICHIER_REJET_2_CONTENT_TYPE);
        HistoriqueDTO historiqueDTO = historiqueMapper.toDto(updatedHistorique);

        restHistoriqueMockMvc.perform(put("/api/historiques").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueDTO)))
            .andExpect(status().isOk());

        // Validate the Historique in the database
        List<Historique> historiqueList = historiqueRepository.findAll();
        assertThat(historiqueList).hasSize(databaseSizeBeforeUpdate);
        Historique testHistorique = historiqueList.get(historiqueList.size() - 1);
        assertThat(testHistorique.getDateRejet()).isEqualTo(UPDATED_DATE_REJET);
        assertThat(testHistorique.getDateRjet2()).isEqualTo(UPDATED_DATE_RJET_2);
        assertThat(testHistorique.getDateMiseValidation()).isEqualTo(UPDATED_DATE_MISE_VALIDATION);
        assertThat(testHistorique.getCommentaireRejet()).isEqualTo(UPDATED_COMMENTAIRE_REJET);
        assertThat(testHistorique.getCommentaireMiseValidation()).isEqualTo(UPDATED_COMMENTAIRE_MISE_VALIDATION);
        assertThat(testHistorique.getFichierMiseValidation()).isEqualTo(UPDATED_FICHIER_MISE_VALIDATION);
        assertThat(testHistorique.getFichierMiseValidationContentType()).isEqualTo(UPDATED_FICHIER_MISE_VALIDATION_CONTENT_TYPE);
        assertThat(testHistorique.getFichierRejet()).isEqualTo(UPDATED_FICHIER_REJET);
        assertThat(testHistorique.getFichierRejetContentType()).isEqualTo(UPDATED_FICHIER_REJET_CONTENT_TYPE);
        assertThat(testHistorique.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testHistorique.getEtat2()).isEqualTo(UPDATED_ETAT_2);
        assertThat(testHistorique.getCommentaireRejet2()).isEqualTo(UPDATED_COMMENTAIRE_REJET_2);
        assertThat(testHistorique.getFichierRejet2()).isEqualTo(UPDATED_FICHIER_REJET_2);
        assertThat(testHistorique.getFichierRejet2ContentType()).isEqualTo(UPDATED_FICHIER_REJET_2_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingHistorique() throws Exception {
        int databaseSizeBeforeUpdate = historiqueRepository.findAll().size();

        // Create the Historique
        HistoriqueDTO historiqueDTO = historiqueMapper.toDto(historique);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueMockMvc.perform(put("/api/historiques").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(historiqueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Historique in the database
        List<Historique> historiqueList = historiqueRepository.findAll();
        assertThat(historiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHistorique() throws Exception {
        // Initialize the database
        historiqueRepository.saveAndFlush(historique);

        int databaseSizeBeforeDelete = historiqueRepository.findAll().size();

        // Delete the historique
        restHistoriqueMockMvc.perform(delete("/api/historiques/{id}", historique.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Historique> historiqueList = historiqueRepository.findAll();
        assertThat(historiqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
