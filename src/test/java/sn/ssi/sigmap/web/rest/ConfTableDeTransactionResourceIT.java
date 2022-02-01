package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.PlanpassationmsApp;
import sn.ssi.sigmap.config.TestSecurityConfiguration;
import sn.ssi.sigmap.domain.ConfTableDeTransaction;
import sn.ssi.sigmap.repository.ConfTableDeTransactionRepository;
import sn.ssi.sigmap.service.ConfTableDeTransactionService;

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
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static sn.ssi.sigmap.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import sn.ssi.sigmap.domain.enumeration.DataType;
/**
 * Integration tests for the {@link ConfTableDeTransactionResource} REST controller.
 */
@SpringBootTest(classes = { PlanpassationmsApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ConfTableDeTransactionResourceIT {

    private static final String DEFAULT_TABLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TABLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COLUMN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COLUMN_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_NUMERO_LIGNE = 1L;
    private static final Long UPDATED_NUMERO_LIGNE = 2L;

    private static final DataType DEFAULT_DATA_TYPE = DataType.INT;
    private static final DataType UPDATED_DATA_TYPE = DataType.MONEY;

    private static final Long DEFAULT_MASTER_ID = 1L;
    private static final Long UPDATED_MASTER_ID = 2L;

    private static final Integer DEFAULT_INT_VALUE = 1;
    private static final Integer UPDATED_INT_VALUE = 2;

    private static final Boolean DEFAULT_BOOLEAN_VALUE = false;
    private static final Boolean UPDATED_BOOLEAN_VALUE = true;

    private static final BigDecimal DEFAULT_MONEY_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONEY_VALUE = new BigDecimal(2);

    private static final String DEFAULT_STRING_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_STRING_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_VALUE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_VALUE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_VALUE = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_INSTANT_VALUE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSTANT_VALUE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ZonedDateTime DEFAULT_ZONED_DATE_TIME_VALUE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ZONED_DATE_TIME_VALUE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_LONG_VALUE = 1L;
    private static final Long UPDATED_LONG_VALUE = 2L;

    private static final Float DEFAULT_FLOAT_VALUE = 1F;
    private static final Float UPDATED_FLOAT_VALUE = 2F;

    private static final Double DEFAULT_DOUBLE_VALUE = 1D;
    private static final Double UPDATED_DOUBLE_VALUE = 2D;

    private static final Duration DEFAULT_DURATION_VALUE = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION_VALUE = Duration.ofHours(12);

    private static final UUID DEFAULT_UUID_VALUE = UUID.randomUUID();
    private static final UUID UPDATED_UUID_VALUE = UUID.randomUUID();

    @Autowired
    private ConfTableDeTransactionRepository confTableDeTransactionRepository;

    @Autowired
    private ConfTableDeTransactionService confTableDeTransactionService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfTableDeTransactionMockMvc;

    private ConfTableDeTransaction confTableDeTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfTableDeTransaction createEntity(EntityManager em) {
        ConfTableDeTransaction confTableDeTransaction = new ConfTableDeTransaction()
            .tableName(DEFAULT_TABLE_NAME)
            .columnName(DEFAULT_COLUMN_NAME)
            .numeroLigne(DEFAULT_NUMERO_LIGNE)
            .dataType(DEFAULT_DATA_TYPE)
            .masterId(DEFAULT_MASTER_ID)
            .intValue(DEFAULT_INT_VALUE)
            .booleanValue(DEFAULT_BOOLEAN_VALUE)
            .moneyValue(DEFAULT_MONEY_VALUE)
            .stringValue(DEFAULT_STRING_VALUE)
            .textValue(DEFAULT_TEXT_VALUE)
            .dateValue(DEFAULT_DATE_VALUE)
            .instantValue(DEFAULT_INSTANT_VALUE)
            .zonedDateTimeValue(DEFAULT_ZONED_DATE_TIME_VALUE)
            .longValue(DEFAULT_LONG_VALUE)
            .floatValue(DEFAULT_FLOAT_VALUE)
            .doubleValue(DEFAULT_DOUBLE_VALUE)
            .durationValue(DEFAULT_DURATION_VALUE)
            .uuidValue(DEFAULT_UUID_VALUE);
        return confTableDeTransaction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfTableDeTransaction createUpdatedEntity(EntityManager em) {
        ConfTableDeTransaction confTableDeTransaction = new ConfTableDeTransaction()
            .tableName(UPDATED_TABLE_NAME)
            .columnName(UPDATED_COLUMN_NAME)
            .numeroLigne(UPDATED_NUMERO_LIGNE)
            .dataType(UPDATED_DATA_TYPE)
            .masterId(UPDATED_MASTER_ID)
            .intValue(UPDATED_INT_VALUE)
            .booleanValue(UPDATED_BOOLEAN_VALUE)
            .moneyValue(UPDATED_MONEY_VALUE)
            .stringValue(UPDATED_STRING_VALUE)
            .textValue(UPDATED_TEXT_VALUE)
            .dateValue(UPDATED_DATE_VALUE)
            .instantValue(UPDATED_INSTANT_VALUE)
            .zonedDateTimeValue(UPDATED_ZONED_DATE_TIME_VALUE)
            .longValue(UPDATED_LONG_VALUE)
            .floatValue(UPDATED_FLOAT_VALUE)
            .doubleValue(UPDATED_DOUBLE_VALUE)
            .durationValue(UPDATED_DURATION_VALUE)
            .uuidValue(UPDATED_UUID_VALUE);
        return confTableDeTransaction;
    }

    @BeforeEach
    public void initTest() {
        confTableDeTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllConfTableDeTransactions() throws Exception {
        // Initialize the database
        confTableDeTransactionRepository.saveAndFlush(confTableDeTransaction);

        // Get all the confTableDeTransactionList
        restConfTableDeTransactionMockMvc.perform(get("/api/conf-table-de-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(confTableDeTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME)))
            .andExpect(jsonPath("$.[*].columnName").value(hasItem(DEFAULT_COLUMN_NAME)))
            .andExpect(jsonPath("$.[*].numeroLigne").value(hasItem(DEFAULT_NUMERO_LIGNE.intValue())))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].masterId").value(hasItem(DEFAULT_MASTER_ID.intValue())))
            .andExpect(jsonPath("$.[*].intValue").value(hasItem(DEFAULT_INT_VALUE)))
            .andExpect(jsonPath("$.[*].booleanValue").value(hasItem(DEFAULT_BOOLEAN_VALUE.booleanValue())))
            .andExpect(jsonPath("$.[*].moneyValue").value(hasItem(DEFAULT_MONEY_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].stringValue").value(hasItem(DEFAULT_STRING_VALUE)))
            .andExpect(jsonPath("$.[*].textValue").value(hasItem(DEFAULT_TEXT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].dateValue").value(hasItem(DEFAULT_DATE_VALUE.toString())))
            .andExpect(jsonPath("$.[*].instantValue").value(hasItem(DEFAULT_INSTANT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].zonedDateTimeValue").value(hasItem(sameInstant(DEFAULT_ZONED_DATE_TIME_VALUE))))
            .andExpect(jsonPath("$.[*].longValue").value(hasItem(DEFAULT_LONG_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].floatValue").value(hasItem(DEFAULT_FLOAT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].doubleValue").value(hasItem(DEFAULT_DOUBLE_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].durationValue").value(hasItem(DEFAULT_DURATION_VALUE.toString())))
            .andExpect(jsonPath("$.[*].uuidValue").value(hasItem(DEFAULT_UUID_VALUE.toString())));
    }
    
    @Test
    @Transactional
    public void getConfTableDeTransaction() throws Exception {
        // Initialize the database
        confTableDeTransactionRepository.saveAndFlush(confTableDeTransaction);

        // Get the confTableDeTransaction
        restConfTableDeTransactionMockMvc.perform(get("/api/conf-table-de-transactions/{id}", confTableDeTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(confTableDeTransaction.getId().intValue()))
            .andExpect(jsonPath("$.tableName").value(DEFAULT_TABLE_NAME))
            .andExpect(jsonPath("$.columnName").value(DEFAULT_COLUMN_NAME))
            .andExpect(jsonPath("$.numeroLigne").value(DEFAULT_NUMERO_LIGNE.intValue()))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()))
            .andExpect(jsonPath("$.masterId").value(DEFAULT_MASTER_ID.intValue()))
            .andExpect(jsonPath("$.intValue").value(DEFAULT_INT_VALUE))
            .andExpect(jsonPath("$.booleanValue").value(DEFAULT_BOOLEAN_VALUE.booleanValue()))
            .andExpect(jsonPath("$.moneyValue").value(DEFAULT_MONEY_VALUE.intValue()))
            .andExpect(jsonPath("$.stringValue").value(DEFAULT_STRING_VALUE))
            .andExpect(jsonPath("$.textValue").value(DEFAULT_TEXT_VALUE.toString()))
            .andExpect(jsonPath("$.dateValue").value(DEFAULT_DATE_VALUE.toString()))
            .andExpect(jsonPath("$.instantValue").value(DEFAULT_INSTANT_VALUE.toString()))
            .andExpect(jsonPath("$.zonedDateTimeValue").value(sameInstant(DEFAULT_ZONED_DATE_TIME_VALUE)))
            .andExpect(jsonPath("$.longValue").value(DEFAULT_LONG_VALUE.intValue()))
            .andExpect(jsonPath("$.floatValue").value(DEFAULT_FLOAT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.doubleValue").value(DEFAULT_DOUBLE_VALUE.doubleValue()))
            .andExpect(jsonPath("$.durationValue").value(DEFAULT_DURATION_VALUE.toString()))
            .andExpect(jsonPath("$.uuidValue").value(DEFAULT_UUID_VALUE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingConfTableDeTransaction() throws Exception {
        // Get the confTableDeTransaction
        restConfTableDeTransactionMockMvc.perform(get("/api/conf-table-de-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
