package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.PlanpassationmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.ParamDate;
import sn.ssi.sigmap.repository.ParamDateRepository;
import sn.ssi.sigmap.service.ParamDateService;
import sn.ssi.sigmap.service.dto.ParamDateDTO;
import sn.ssi.sigmap.service.mapper.ParamDateMapper;

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
 * Integration tests for the {@link ParamDateResource} REST controller.
 */
@SpringBootTest(classes = { PlanpassationmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ParamDateResourceIT {

    private static final LocalDate DEFAULT_DATE_CREAT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREAT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ParamDateRepository paramDateRepository;

    @Autowired
    private ParamDateMapper paramDateMapper;

    @Autowired
    private ParamDateService paramDateService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParamDateMockMvc;

    private ParamDate paramDate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParamDate createEntity(EntityManager em) {
        ParamDate paramDate = new ParamDate()
            .dateCreat(DEFAULT_DATE_CREAT);
        return paramDate;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParamDate createUpdatedEntity(EntityManager em) {
        ParamDate paramDate = new ParamDate()
            .dateCreat(UPDATED_DATE_CREAT);
        return paramDate;
    }

    @BeforeEach
    public void initTest() {
        paramDate = createEntity(em);
    }

    @Test
    @Transactional
    public void createParamDate() throws Exception {
        int databaseSizeBeforeCreate = paramDateRepository.findAll().size();
        // Create the ParamDate
        ParamDateDTO paramDateDTO = paramDateMapper.toDto(paramDate);
        restParamDateMockMvc.perform(post("/api/param-dates").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paramDateDTO)))
            .andExpect(status().isCreated());

        // Validate the ParamDate in the database
        List<ParamDate> paramDateList = paramDateRepository.findAll();
        assertThat(paramDateList).hasSize(databaseSizeBeforeCreate + 1);
        ParamDate testParamDate = paramDateList.get(paramDateList.size() - 1);
        assertThat(testParamDate.getDateCreat()).isEqualTo(DEFAULT_DATE_CREAT);
    }

    @Test
    @Transactional
    public void createParamDateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paramDateRepository.findAll().size();

        // Create the ParamDate with an existing ID
        paramDate.setId(1L);
        ParamDateDTO paramDateDTO = paramDateMapper.toDto(paramDate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParamDateMockMvc.perform(post("/api/param-dates").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paramDateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ParamDate in the database
        List<ParamDate> paramDateList = paramDateRepository.findAll();
        assertThat(paramDateList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllParamDates() throws Exception {
        // Initialize the database
        paramDateRepository.saveAndFlush(paramDate);

        // Get all the paramDateList
        restParamDateMockMvc.perform(get("/api/param-dates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paramDate.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateCreat").value(hasItem(DEFAULT_DATE_CREAT.toString())));
    }
    
    @Test
    @Transactional
    public void getParamDate() throws Exception {
        // Initialize the database
        paramDateRepository.saveAndFlush(paramDate);

        // Get the paramDate
        restParamDateMockMvc.perform(get("/api/param-dates/{id}", paramDate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paramDate.getId().intValue()))
            .andExpect(jsonPath("$.dateCreat").value(DEFAULT_DATE_CREAT.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingParamDate() throws Exception {
        // Get the paramDate
        restParamDateMockMvc.perform(get("/api/param-dates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParamDate() throws Exception {
        // Initialize the database
        paramDateRepository.saveAndFlush(paramDate);

        int databaseSizeBeforeUpdate = paramDateRepository.findAll().size();

        // Update the paramDate
        ParamDate updatedParamDate = paramDateRepository.findById(paramDate.getId()).get();
        // Disconnect from session so that the updates on updatedParamDate are not directly saved in db
        em.detach(updatedParamDate);
        updatedParamDate
            .dateCreat(UPDATED_DATE_CREAT);
        ParamDateDTO paramDateDTO = paramDateMapper.toDto(updatedParamDate);

        restParamDateMockMvc.perform(put("/api/param-dates").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paramDateDTO)))
            .andExpect(status().isOk());

        // Validate the ParamDate in the database
        List<ParamDate> paramDateList = paramDateRepository.findAll();
        assertThat(paramDateList).hasSize(databaseSizeBeforeUpdate);
        ParamDate testParamDate = paramDateList.get(paramDateList.size() - 1);
        assertThat(testParamDate.getDateCreat()).isEqualTo(UPDATED_DATE_CREAT);
    }

    @Test
    @Transactional
    public void updateNonExistingParamDate() throws Exception {
        int databaseSizeBeforeUpdate = paramDateRepository.findAll().size();

        // Create the ParamDate
        ParamDateDTO paramDateDTO = paramDateMapper.toDto(paramDate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParamDateMockMvc.perform(put("/api/param-dates").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paramDateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ParamDate in the database
        List<ParamDate> paramDateList = paramDateRepository.findAll();
        assertThat(paramDateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParamDate() throws Exception {
        // Initialize the database
        paramDateRepository.saveAndFlush(paramDate);

        int databaseSizeBeforeDelete = paramDateRepository.findAll().size();

        // Delete the paramDate
        restParamDateMockMvc.perform(delete("/api/param-dates/{id}", paramDate.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ParamDate> paramDateList = paramDateRepository.findAll();
        assertThat(paramDateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
