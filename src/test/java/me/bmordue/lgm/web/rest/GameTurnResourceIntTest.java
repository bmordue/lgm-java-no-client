package me.bmordue.lgm.web.rest;

import me.bmordue.lgm.LgmApp;

import me.bmordue.lgm.domain.GameTurn;
import me.bmordue.lgm.domain.Landscape;
import me.bmordue.lgm.domain.Game;
import me.bmordue.lgm.repository.GameTurnRepository;
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
 * Test class for the GameTurnResource REST controller.
 *
 * @see GameTurnResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LgmApp.class)
public class GameTurnResourceIntTest {

    private static final Integer DEFAULT_TURN_NUMBER = 1;
    private static final Integer UPDATED_TURN_NUMBER = 2;

    @Autowired
    private GameTurnRepository gameTurnRepository;

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

    private MockMvc restGameTurnMockMvc;

    private GameTurn gameTurn;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GameTurnResource gameTurnResource = new GameTurnResource(gameTurnRepository);
        this.restGameTurnMockMvc = MockMvcBuilders.standaloneSetup(gameTurnResource)
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
    public static GameTurn createEntity(EntityManager em) {
        GameTurn gameTurn = new GameTurn()
            .turnNumber(DEFAULT_TURN_NUMBER);
        // Add required entity
        Landscape landscape = LandscapeResourceIntTest.createEntity(em);
        em.persist(landscape);
        em.flush();
        gameTurn.setLandscape(landscape);
        // Add required entity
        Game game = GameResourceIntTest.createEntity(em);
        em.persist(game);
        em.flush();
        gameTurn.setGame(game);
        return gameTurn;
    }

    @Before
    public void initTest() {
        gameTurn = createEntity(em);
    }

    @Test
    @Transactional
    public void createGameTurn() throws Exception {
        int databaseSizeBeforeCreate = gameTurnRepository.findAll().size();

        // Create the GameTurn
        restGameTurnMockMvc.perform(post("/api/game-turns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameTurn)))
            .andExpect(status().isCreated());

        // Validate the GameTurn in the database
        List<GameTurn> gameTurnList = gameTurnRepository.findAll();
        assertThat(gameTurnList).hasSize(databaseSizeBeforeCreate + 1);
        GameTurn testGameTurn = gameTurnList.get(gameTurnList.size() - 1);
        assertThat(testGameTurn.getTurnNumber()).isEqualTo(DEFAULT_TURN_NUMBER);
    }

    @Test
    @Transactional
    public void createGameTurnWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gameTurnRepository.findAll().size();

        // Create the GameTurn with an existing ID
        gameTurn.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameTurnMockMvc.perform(post("/api/game-turns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameTurn)))
            .andExpect(status().isBadRequest());

        // Validate the GameTurn in the database
        List<GameTurn> gameTurnList = gameTurnRepository.findAll();
        assertThat(gameTurnList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTurnNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameTurnRepository.findAll().size();
        // set the field null
        gameTurn.setTurnNumber(null);

        // Create the GameTurn, which fails.

        restGameTurnMockMvc.perform(post("/api/game-turns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameTurn)))
            .andExpect(status().isBadRequest());

        List<GameTurn> gameTurnList = gameTurnRepository.findAll();
        assertThat(gameTurnList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGameTurns() throws Exception {
        // Initialize the database
        gameTurnRepository.saveAndFlush(gameTurn);

        // Get all the gameTurnList
        restGameTurnMockMvc.perform(get("/api/game-turns?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameTurn.getId().intValue())))
            .andExpect(jsonPath("$.[*].turnNumber").value(hasItem(DEFAULT_TURN_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getGameTurn() throws Exception {
        // Initialize the database
        gameTurnRepository.saveAndFlush(gameTurn);

        // Get the gameTurn
        restGameTurnMockMvc.perform(get("/api/game-turns/{id}", gameTurn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gameTurn.getId().intValue()))
            .andExpect(jsonPath("$.turnNumber").value(DEFAULT_TURN_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingGameTurn() throws Exception {
        // Get the gameTurn
        restGameTurnMockMvc.perform(get("/api/game-turns/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGameTurn() throws Exception {
        // Initialize the database
        gameTurnRepository.saveAndFlush(gameTurn);

        int databaseSizeBeforeUpdate = gameTurnRepository.findAll().size();

        // Update the gameTurn
        GameTurn updatedGameTurn = gameTurnRepository.findById(gameTurn.getId()).get();
        // Disconnect from session so that the updates on updatedGameTurn are not directly saved in db
        em.detach(updatedGameTurn);
        updatedGameTurn
            .turnNumber(UPDATED_TURN_NUMBER);

        restGameTurnMockMvc.perform(put("/api/game-turns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGameTurn)))
            .andExpect(status().isOk());

        // Validate the GameTurn in the database
        List<GameTurn> gameTurnList = gameTurnRepository.findAll();
        assertThat(gameTurnList).hasSize(databaseSizeBeforeUpdate);
        GameTurn testGameTurn = gameTurnList.get(gameTurnList.size() - 1);
        assertThat(testGameTurn.getTurnNumber()).isEqualTo(UPDATED_TURN_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingGameTurn() throws Exception {
        int databaseSizeBeforeUpdate = gameTurnRepository.findAll().size();

        // Create the GameTurn

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameTurnMockMvc.perform(put("/api/game-turns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameTurn)))
            .andExpect(status().isBadRequest());

        // Validate the GameTurn in the database
        List<GameTurn> gameTurnList = gameTurnRepository.findAll();
        assertThat(gameTurnList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGameTurn() throws Exception {
        // Initialize the database
        gameTurnRepository.saveAndFlush(gameTurn);

        int databaseSizeBeforeDelete = gameTurnRepository.findAll().size();

        // Get the gameTurn
        restGameTurnMockMvc.perform(delete("/api/game-turns/{id}", gameTurn.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GameTurn> gameTurnList = gameTurnRepository.findAll();
        assertThat(gameTurnList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameTurn.class);
        GameTurn gameTurn1 = new GameTurn();
        gameTurn1.setId(1L);
        GameTurn gameTurn2 = new GameTurn();
        gameTurn2.setId(gameTurn1.getId());
        assertThat(gameTurn1).isEqualTo(gameTurn2);
        gameTurn2.setId(2L);
        assertThat(gameTurn1).isNotEqualTo(gameTurn2);
        gameTurn1.setId(null);
        assertThat(gameTurn1).isNotEqualTo(gameTurn2);
    }
}
