package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.PlanpassationmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.ConfTableRow;
import sn.ssi.sigmap.repository.ConfTableRowRepository;
import sn.ssi.sigmap.service.ConfTableRowService;

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

import sn.ssi.sigmap.domain.enumeration.DataType;
/**
 * Integration tests for the {@link ConfTableRowResource} REST controller.
 */
@SpringBootTest(classes = { PlanpassationmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ConfTableRowResourceIT {

    private static final String DEFAULT_TABLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TABLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LABEL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COLUMN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COLUMN_NAME = "BBBBBBBBBB";

    private static final DataType DEFAULT_DATA_TYPE = DataType.INT;
    private static final DataType UPDATED_DATA_TYPE = DataType.MONEY;

    @Autowired
    private ConfTableRowRepository confTableRowRepository;

    @Autowired
    private ConfTableRowService confTableRowService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfTableRowMockMvc;

    private ConfTableRow confTableRow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfTableRow createEntity(EntityManager em) {
        ConfTableRow confTableRow = new ConfTableRow()
            .tableName(DEFAULT_TABLE_NAME)
            .labelName(DEFAULT_LABEL_NAME)
            .columnName(DEFAULT_COLUMN_NAME)
            .dataType(DEFAULT_DATA_TYPE);
        return confTableRow;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfTableRow createUpdatedEntity(EntityManager em) {
        ConfTableRow confTableRow = new ConfTableRow()
            .tableName(UPDATED_TABLE_NAME)
            .labelName(UPDATED_LABEL_NAME)
            .columnName(UPDATED_COLUMN_NAME)
            .dataType(UPDATED_DATA_TYPE);
        return confTableRow;
    }

    @BeforeEach
    public void initTest() {
        confTableRow = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfTableRow() throws Exception {
        int databaseSizeBeforeCreate = confTableRowRepository.findAll().size();
        // Create the ConfTableRow
        restConfTableRowMockMvc.perform(post("/api/conf-table-rows").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(confTableRow)))
            .andExpect(status().isCreated());

        // Validate the ConfTableRow in the database
        List<ConfTableRow> confTableRowList = confTableRowRepository.findAll();
        assertThat(confTableRowList).hasSize(databaseSizeBeforeCreate + 1);
        ConfTableRow testConfTableRow = confTableRowList.get(confTableRowList.size() - 1);
        assertThat(testConfTableRow.getTableName()).isEqualTo(DEFAULT_TABLE_NAME);
        assertThat(testConfTableRow.getLabelName()).isEqualTo(DEFAULT_LABEL_NAME);
        assertThat(testConfTableRow.getColumnName()).isEqualTo(DEFAULT_COLUMN_NAME);
        assertThat(testConfTableRow.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
    }

    @Test
    @Transactional
    public void createConfTableRowWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = confTableRowRepository.findAll().size();

        // Create the ConfTableRow with an existing ID
        confTableRow.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfTableRowMockMvc.perform(post("/api/conf-table-rows").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(confTableRow)))
            .andExpect(status().isBadRequest());

        // Validate the ConfTableRow in the database
        List<ConfTableRow> confTableRowList = confTableRowRepository.findAll();
        assertThat(confTableRowList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConfTableRows() throws Exception {
        // Initialize the database
        confTableRowRepository.saveAndFlush(confTableRow);

        // Get all the confTableRowList
        restConfTableRowMockMvc.perform(get("/api/conf-table-rows?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(confTableRow.getId().intValue())))
            .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME)))
            .andExpect(jsonPath("$.[*].labelName").value(hasItem(DEFAULT_LABEL_NAME)))
            .andExpect(jsonPath("$.[*].columnName").value(hasItem(DEFAULT_COLUMN_NAME)))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getConfTableRow() throws Exception {
        // Initialize the database
        confTableRowRepository.saveAndFlush(confTableRow);

        // Get the confTableRow
        restConfTableRowMockMvc.perform(get("/api/conf-table-rows/{id}", confTableRow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(confTableRow.getId().intValue()))
            .andExpect(jsonPath("$.tableName").value(DEFAULT_TABLE_NAME))
            .andExpect(jsonPath("$.labelName").value(DEFAULT_LABEL_NAME))
            .andExpect(jsonPath("$.columnName").value(DEFAULT_COLUMN_NAME))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingConfTableRow() throws Exception {
        // Get the confTableRow
        restConfTableRowMockMvc.perform(get("/api/conf-table-rows/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfTableRow() throws Exception {
        // Initialize the database
        confTableRowService.save(confTableRow);

        int databaseSizeBeforeUpdate = confTableRowRepository.findAll().size();

        // Update the confTableRow
        ConfTableRow updatedConfTableRow = confTableRowRepository.findById(confTableRow.getId()).get();
        // Disconnect from session so that the updates on updatedConfTableRow are not directly saved in db
        em.detach(updatedConfTableRow);
        updatedConfTableRow
            .tableName(UPDATED_TABLE_NAME)
            .labelName(UPDATED_LABEL_NAME)
            .columnName(UPDATED_COLUMN_NAME)
            .dataType(UPDATED_DATA_TYPE);

        restConfTableRowMockMvc.perform(put("/api/conf-table-rows").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfTableRow)))
            .andExpect(status().isOk());

        // Validate the ConfTableRow in the database
        List<ConfTableRow> confTableRowList = confTableRowRepository.findAll();
        assertThat(confTableRowList).hasSize(databaseSizeBeforeUpdate);
        ConfTableRow testConfTableRow = confTableRowList.get(confTableRowList.size() - 1);
        assertThat(testConfTableRow.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
        assertThat(testConfTableRow.getLabelName()).isEqualTo(UPDATED_LABEL_NAME);
        assertThat(testConfTableRow.getColumnName()).isEqualTo(UPDATED_COLUMN_NAME);
        assertThat(testConfTableRow.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingConfTableRow() throws Exception {
        int databaseSizeBeforeUpdate = confTableRowRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfTableRowMockMvc.perform(put("/api/conf-table-rows").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(confTableRow)))
            .andExpect(status().isBadRequest());

        // Validate the ConfTableRow in the database
        List<ConfTableRow> confTableRowList = confTableRowRepository.findAll();
        assertThat(confTableRowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfTableRow() throws Exception {
        // Initialize the database
        confTableRowService.save(confTableRow);

        int databaseSizeBeforeDelete = confTableRowRepository.findAll().size();

        // Delete the confTableRow
        restConfTableRowMockMvc.perform(delete("/api/conf-table-rows/{id}", confTableRow.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfTableRow> confTableRowList = confTableRowRepository.findAll();
        assertThat(confTableRowList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
