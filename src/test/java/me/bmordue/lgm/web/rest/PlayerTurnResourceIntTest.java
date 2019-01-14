package me.bmordue.lgm.web.rest;

import me.bmordue.lgm.LgmApp;

import me.bmordue.lgm.domain.PlayerTurn;
import me.bmordue.lgm.domain.GameTurn;
import me.bmordue.lgm.domain.Player;
import me.bmordue.lgm.repository.PlayerTurnRepository;
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
 * Test class for the PlayerTurnResource REST controller.
 *
 * @see PlayerTurnResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LgmApp.class)
public class PlayerTurnResourceIntTest {

    @Autowired
    private PlayerTurnRepository playerTurnRepository;

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

    private MockMvc restPlayerTurnMockMvc;

    private PlayerTurn playerTurn;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlayerTurnResource playerTurnResource = new PlayerTurnResource(playerTurnRepository);
        this.restPlayerTurnMockMvc = MockMvcBuilders.standaloneSetup(playerTurnResource)
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
    public static PlayerTurn createEntity(EntityManager em) {
        PlayerTurn playerTurn = new PlayerTurn();
        // Add required entity
        GameTurn gameTurn = GameTurnResourceIntTest.createEntity(em);
        em.persist(gameTurn);
        em.flush();
        playerTurn.setTurn(gameTurn);
        // Add required entity
        Player player = PlayerResourceIntTest.createEntity(em);
        em.persist(player);
        em.flush();
        playerTurn.setPlayer(player);
        return playerTurn;
    }

    @Before
    public void initTest() {
        playerTurn = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlayerTurn() throws Exception {
        int databaseSizeBeforeCreate = playerTurnRepository.findAll().size();

        // Create the PlayerTurn
        restPlayerTurnMockMvc.perform(post("/api/player-turns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerTurn)))
            .andExpect(status().isCreated());

        // Validate the PlayerTurn in the database
        List<PlayerTurn> playerTurnList = playerTurnRepository.findAll();
        assertThat(playerTurnList).hasSize(databaseSizeBeforeCreate + 1);
        PlayerTurn testPlayerTurn = playerTurnList.get(playerTurnList.size() - 1);
    }

    @Test
    @Transactional
    public void createPlayerTurnWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = playerTurnRepository.findAll().size();

        // Create the PlayerTurn with an existing ID
        playerTurn.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerTurnMockMvc.perform(post("/api/player-turns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerTurn)))
            .andExpect(status().isBadRequest());

        // Validate the PlayerTurn in the database
        List<PlayerTurn> playerTurnList = playerTurnRepository.findAll();
        assertThat(playerTurnList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPlayerTurns() throws Exception {
        // Initialize the database
        playerTurnRepository.saveAndFlush(playerTurn);

        // Get all the playerTurnList
        restPlayerTurnMockMvc.perform(get("/api/player-turns?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerTurn.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getPlayerTurn() throws Exception {
        // Initialize the database
        playerTurnRepository.saveAndFlush(playerTurn);

        // Get the playerTurn
        restPlayerTurnMockMvc.perform(get("/api/player-turns/{id}", playerTurn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(playerTurn.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPlayerTurn() throws Exception {
        // Get the playerTurn
        restPlayerTurnMockMvc.perform(get("/api/player-turns/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlayerTurn() throws Exception {
        // Initialize the database
        playerTurnRepository.saveAndFlush(playerTurn);

        int databaseSizeBeforeUpdate = playerTurnRepository.findAll().size();

        // Update the playerTurn
        PlayerTurn updatedPlayerTurn = playerTurnRepository.findById(playerTurn.getId()).get();
        // Disconnect from session so that the updates on updatedPlayerTurn are not directly saved in db
        em.detach(updatedPlayerTurn);

        restPlayerTurnMockMvc.perform(put("/api/player-turns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlayerTurn)))
            .andExpect(status().isOk());

        // Validate the PlayerTurn in the database
        List<PlayerTurn> playerTurnList = playerTurnRepository.findAll();
        assertThat(playerTurnList).hasSize(databaseSizeBeforeUpdate);
        PlayerTurn testPlayerTurn = playerTurnList.get(playerTurnList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingPlayerTurn() throws Exception {
        int databaseSizeBeforeUpdate = playerTurnRepository.findAll().size();

        // Create the PlayerTurn

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerTurnMockMvc.perform(put("/api/player-turns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerTurn)))
            .andExpect(status().isBadRequest());

        // Validate the PlayerTurn in the database
        List<PlayerTurn> playerTurnList = playerTurnRepository.findAll();
        assertThat(playerTurnList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlayerTurn() throws Exception {
        // Initialize the database
        playerTurnRepository.saveAndFlush(playerTurn);

        int databaseSizeBeforeDelete = playerTurnRepository.findAll().size();

        // Get the playerTurn
        restPlayerTurnMockMvc.perform(delete("/api/player-turns/{id}", playerTurn.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PlayerTurn> playerTurnList = playerTurnRepository.findAll();
        assertThat(playerTurnList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerTurn.class);
        PlayerTurn playerTurn1 = new PlayerTurn();
        playerTurn1.setId(1L);
        PlayerTurn playerTurn2 = new PlayerTurn();
        playerTurn2.setId(playerTurn1.getId());
        assertThat(playerTurn1).isEqualTo(playerTurn2);
        playerTurn2.setId(2L);
        assertThat(playerTurn1).isNotEqualTo(playerTurn2);
        playerTurn1.setId(null);
        assertThat(playerTurn1).isNotEqualTo(playerTurn2);
    }
}
