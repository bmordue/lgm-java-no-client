package me.bmordue.lgm.web.rest;

import me.bmordue.lgm.LgmApp;

import me.bmordue.lgm.domain.Turn;
import me.bmordue.lgm.domain.Game;
import me.bmordue.lgm.repository.TurnRepository;
import me.bmordue.lgm.repository.search.TurnSearchRepository;
import me.bmordue.lgm.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static me.bmordue.lgm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TurnResource REST controller.
 *
 * @see TurnResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LgmApp.class)
public class TurnResourceIntTest {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    @Autowired
    private TurnRepository turnRepository;

    /**
     * This repository is mocked in the me.bmordue.lgm.repository.search test package.
     *
     * @see me.bmordue.lgm.repository.search.TurnSearchRepositoryMockConfiguration
     */
    @Autowired
    private TurnSearchRepository mockTurnSearchRepository;

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

    private MockMvc restTurnMockMvc;

    private Turn turn;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TurnResource turnResource = new TurnResource(turnRepository, mockTurnSearchRepository);
        this.restTurnMockMvc = MockMvcBuilders.standaloneSetup(turnResource)
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
    public static Turn createEntity(EntityManager em) {
        Turn turn = new Turn()
            .number(DEFAULT_NUMBER);
        // Add required entity
        Game game = GameResourceIntTest.createEntity(em);
        em.persist(game);
        em.flush();
        turn.setGame(game);
        return turn;
    }

    @Before
    public void initTest() {
        turn = createEntity(em);
    }

    @Test
    @Transactional
    public void createTurn() throws Exception {
        int databaseSizeBeforeCreate = turnRepository.findAll().size();

        // Create the Turn
        restTurnMockMvc.perform(post("/api/turns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turn)))
            .andExpect(status().isCreated());

        // Validate the Turn in the database
        List<Turn> turnList = turnRepository.findAll();
        assertThat(turnList).hasSize(databaseSizeBeforeCreate + 1);
        Turn testTurn = turnList.get(turnList.size() - 1);
        assertThat(testTurn.getNumber()).isEqualTo(DEFAULT_NUMBER);

        // Validate the Turn in Elasticsearch
        verify(mockTurnSearchRepository, times(1)).save(testTurn);
    }

    @Test
    @Transactional
    public void createTurnWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = turnRepository.findAll().size();

        // Create the Turn with an existing ID
        turn.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTurnMockMvc.perform(post("/api/turns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turn)))
            .andExpect(status().isBadRequest());

        // Validate the Turn in the database
        List<Turn> turnList = turnRepository.findAll();
        assertThat(turnList).hasSize(databaseSizeBeforeCreate);

        // Validate the Turn in Elasticsearch
        verify(mockTurnSearchRepository, times(0)).save(turn);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = turnRepository.findAll().size();
        // set the field null
        turn.setNumber(null);

        // Create the Turn, which fails.

        restTurnMockMvc.perform(post("/api/turns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turn)))
            .andExpect(status().isBadRequest());

        List<Turn> turnList = turnRepository.findAll();
        assertThat(turnList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTurns() throws Exception {
        // Initialize the database
        turnRepository.saveAndFlush(turn);

        // Get all the turnList
        restTurnMockMvc.perform(get("/api/turns?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turn.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getTurn() throws Exception {
        // Initialize the database
        turnRepository.saveAndFlush(turn);

        // Get the turn
        restTurnMockMvc.perform(get("/api/turns/{id}", turn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(turn.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingTurn() throws Exception {
        // Get the turn
        restTurnMockMvc.perform(get("/api/turns/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTurn() throws Exception {
        // Initialize the database
        turnRepository.saveAndFlush(turn);

        int databaseSizeBeforeUpdate = turnRepository.findAll().size();

        // Update the turn
        Turn updatedTurn = turnRepository.findById(turn.getId()).get();
        // Disconnect from session so that the updates on updatedTurn are not directly saved in db
        em.detach(updatedTurn);
        updatedTurn
            .number(UPDATED_NUMBER);

        restTurnMockMvc.perform(put("/api/turns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTurn)))
            .andExpect(status().isOk());

        // Validate the Turn in the database
        List<Turn> turnList = turnRepository.findAll();
        assertThat(turnList).hasSize(databaseSizeBeforeUpdate);
        Turn testTurn = turnList.get(turnList.size() - 1);
        assertThat(testTurn.getNumber()).isEqualTo(UPDATED_NUMBER);

        // Validate the Turn in Elasticsearch
        verify(mockTurnSearchRepository, times(1)).save(testTurn);
    }

    @Test
    @Transactional
    public void updateNonExistingTurn() throws Exception {
        int databaseSizeBeforeUpdate = turnRepository.findAll().size();

        // Create the Turn

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurnMockMvc.perform(put("/api/turns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(turn)))
            .andExpect(status().isBadRequest());

        // Validate the Turn in the database
        List<Turn> turnList = turnRepository.findAll();
        assertThat(turnList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Turn in Elasticsearch
        verify(mockTurnSearchRepository, times(0)).save(turn);
    }

    @Test
    @Transactional
    public void deleteTurn() throws Exception {
        // Initialize the database
        turnRepository.saveAndFlush(turn);

        int databaseSizeBeforeDelete = turnRepository.findAll().size();

        // Get the turn
        restTurnMockMvc.perform(delete("/api/turns/{id}", turn.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Turn> turnList = turnRepository.findAll();
        assertThat(turnList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Turn in Elasticsearch
        verify(mockTurnSearchRepository, times(1)).deleteById(turn.getId());
    }

    @Test
    @Transactional
    public void searchTurn() throws Exception {
        // Initialize the database
        turnRepository.saveAndFlush(turn);
        when(mockTurnSearchRepository.search(queryStringQuery("id:" + turn.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(turn), PageRequest.of(0, 1), 1));
        // Search the turn
        restTurnMockMvc.perform(get("/api/_search/turns?query=id:" + turn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turn.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Turn.class);
        Turn turn1 = new Turn();
        turn1.setId(1L);
        Turn turn2 = new Turn();
        turn2.setId(turn1.getId());
        assertThat(turn1).isEqualTo(turn2);
        turn2.setId(2L);
        assertThat(turn1).isNotEqualTo(turn2);
        turn1.setId(null);
        assertThat(turn1).isNotEqualTo(turn2);
    }
}
