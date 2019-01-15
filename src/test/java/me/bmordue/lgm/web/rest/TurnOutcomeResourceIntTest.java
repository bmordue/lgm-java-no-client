package me.bmordue.lgm.web.rest;

import me.bmordue.lgm.LgmApp;

import me.bmordue.lgm.domain.TurnOutcome;
import me.bmordue.lgm.domain.GameTurn;
import me.bmordue.lgm.repository.TurnOutcomeRepository;
import me.bmordue.lgm.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static me.bmordue.lgm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TurnOutcomeResource REST controller.
 *
 * @see TurnOutcomeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LgmApp.class)
public class TurnOutcomeResourceIntTest {

    @Autowired
    private TurnOutcomeRepository turnOutcomeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restTurnOutcomeMockMvc;

    private TurnOutcome turnOutcome;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TurnOutcomeResource turnOutcomeResource = new TurnOutcomeResource(turnOutcomeRepository);
        this.restTurnOutcomeMockMvc = MockMvcBuilders.standaloneSetup(turnOutcomeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TurnOutcome createEntity(EntityManager em) {
        TurnOutcome turnOutcome = new TurnOutcome();
        // Add required entity
        GameTurn gameTurn = GameTurnResourceIntTest.createEntity(em);
        em.persist(gameTurn);
        em.flush();
        turnOutcome.setTurn(gameTurn);
        return turnOutcome;
    }

    @Before
    public void initTest() {
        turnOutcome = createEntity(em);
    }

    @Test
    @Transactional
    public void createTurnOutcome() throws Exception {
        int databaseSizeBeforeCreate = turnOutcomeRepository.findAll().size();

        // Create the TurnOutcome
        restTurnOutcomeMockMvc.perform(post("/api/turn-outcomes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turnOutcome)))
            .andExpect(status().isCreated());

        // Validate the TurnOutcome in the database
        List<TurnOutcome> turnOutcomeList = turnOutcomeRepository.findAll();
        assertThat(turnOutcomeList).hasSize(databaseSizeBeforeCreate + 1);
        TurnOutcome testTurnOutcome = turnOutcomeList.get(turnOutcomeList.size() - 1);
    }

    @Test
    @Transactional
    public void createTurnOutcomeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = turnOutcomeRepository.findAll().size();

        // Create the TurnOutcome with an existing ID
        turnOutcome.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTurnOutcomeMockMvc.perform(post("/api/turn-outcomes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turnOutcome)))
            .andExpect(status().isBadRequest());

        // Validate the TurnOutcome in the database
        List<TurnOutcome> turnOutcomeList = turnOutcomeRepository.findAll();
        assertThat(turnOutcomeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTurnOutcomes() throws Exception {
        // Initialize the database
        turnOutcomeRepository.saveAndFlush(turnOutcome);

        // Get all the turnOutcomeList
        restTurnOutcomeMockMvc.perform(get("/api/turn-outcomes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turnOutcome.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getTurnOutcome() throws Exception {
        // Initialize the database
        turnOutcomeRepository.saveAndFlush(turnOutcome);

        // Get the turnOutcome
        restTurnOutcomeMockMvc.perform(get("/api/turn-outcomes/{id}", turnOutcome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(turnOutcome.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTurnOutcome() throws Exception {
        // Get the turnOutcome
        restTurnOutcomeMockMvc.perform(get("/api/turn-outcomes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTurnOutcome() throws Exception {
        // Initialize the database
        turnOutcomeRepository.saveAndFlush(turnOutcome);

        int databaseSizeBeforeUpdate = turnOutcomeRepository.findAll().size();

        // Update the turnOutcome
        TurnOutcome updatedTurnOutcome = turnOutcomeRepository.findById(turnOutcome.getId()).get();
        // Disconnect from session so that the updates on updatedTurnOutcome are not directly saved in db
        em.detach(updatedTurnOutcome);

        restTurnOutcomeMockMvc.perform(put("/api/turn-outcomes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTurnOutcome)))
            .andExpect(status().isOk());

        // Validate the TurnOutcome in the database
        List<TurnOutcome> turnOutcomeList = turnOutcomeRepository.findAll();
        assertThat(turnOutcomeList).hasSize(databaseSizeBeforeUpdate);
        TurnOutcome testTurnOutcome = turnOutcomeList.get(turnOutcomeList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingTurnOutcome() throws Exception {
        int databaseSizeBeforeUpdate = turnOutcomeRepository.findAll().size();

        // Create the TurnOutcome

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurnOutcomeMockMvc.perform(put("/api/turn-outcomes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turnOutcome)))
            .andExpect(status().isBadRequest());

        // Validate the TurnOutcome in the database
        List<TurnOutcome> turnOutcomeList = turnOutcomeRepository.findAll();
        assertThat(turnOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTurnOutcome() throws Exception {
        // Initialize the database
        turnOutcomeRepository.saveAndFlush(turnOutcome);

        int databaseSizeBeforeDelete = turnOutcomeRepository.findAll().size();

        // Get the turnOutcome
        restTurnOutcomeMockMvc.perform(delete("/api/turn-outcomes/{id}", turnOutcome.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TurnOutcome> turnOutcomeList = turnOutcomeRepository.findAll();
        assertThat(turnOutcomeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TurnOutcome.class);
        TurnOutcome turnOutcome1 = new TurnOutcome();
        turnOutcome1.setId(1L);
        TurnOutcome turnOutcome2 = new TurnOutcome();
        turnOutcome2.setId(turnOutcome1.getId());
        assertThat(turnOutcome1).isEqualTo(turnOutcome2);
        turnOutcome2.setId(2L);
        assertThat(turnOutcome1).isNotEqualTo(turnOutcome2);
        turnOutcome1.setId(null);
        assertThat(turnOutcome1).isNotEqualTo(turnOutcome2);
    }
}
