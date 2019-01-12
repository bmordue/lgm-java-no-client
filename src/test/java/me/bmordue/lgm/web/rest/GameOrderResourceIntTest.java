package me.bmordue.lgm.web.rest;

import me.bmordue.lgm.LgmApp;

import me.bmordue.lgm.domain.GameOrder;
import me.bmordue.lgm.domain.Actor;
import me.bmordue.lgm.domain.Turn;
import me.bmordue.lgm.repository.GameOrderRepository;
import me.bmordue.lgm.repository.search.GameOrderSearchRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import me.bmordue.lgm.domain.enumeration.Direction;
/**
 * Test class for the GameOrderResource REST controller.
 *
 * @see GameOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LgmApp.class)
public class GameOrderResourceIntTest {

    private static final Direction DEFAULT_DIRECTION = Direction.UP_LEFT;
    private static final Direction UPDATED_DIRECTION = Direction.UP_RIGHT;

    @Autowired
    private GameOrderRepository gameOrderRepository;

    /**
     * This repository is mocked in the me.bmordue.lgm.repository.search test package.
     *
     * @see me.bmordue.lgm.repository.search.GameOrderSearchRepositoryMockConfiguration
     */
    @Autowired
    private GameOrderSearchRepository mockGameOrderSearchRepository;

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

    private MockMvc restGameOrderMockMvc;

    private GameOrder gameOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GameOrderResource gameOrderResource = new GameOrderResource(gameOrderRepository, mockGameOrderSearchRepository);
        this.restGameOrderMockMvc = MockMvcBuilders.standaloneSetup(gameOrderResource)
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
    public static GameOrder createEntity(EntityManager em) {
        GameOrder gameOrder = new GameOrder()
            .direction(DEFAULT_DIRECTION);
        // Add required entity
        Actor actor = ActorResourceIntTest.createEntity(em);
        em.persist(actor);
        em.flush();
        gameOrder.setActor(actor);
        // Add required entity
        Turn turn = TurnResourceIntTest.createEntity(em);
        em.persist(turn);
        em.flush();
        gameOrder.setTurn(turn);
        return gameOrder;
    }

    @Before
    public void initTest() {
        gameOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createGameOrder() throws Exception {
        int databaseSizeBeforeCreate = gameOrderRepository.findAll().size();

        // Create the GameOrder
        restGameOrderMockMvc.perform(post("/api/game-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameOrder)))
            .andDo(print())
            .andExpect(status().isCreated());

        // Validate the GameOrder in the database
        List<GameOrder> gameOrderList = gameOrderRepository.findAll();
        assertThat(gameOrderList).hasSize(databaseSizeBeforeCreate + 1);
        GameOrder testGameOrder = gameOrderList.get(gameOrderList.size() - 1);
        assertThat(testGameOrder.getDirection()).isEqualTo(DEFAULT_DIRECTION);

        // Validate the GameOrder in Elasticsearch
        verify(mockGameOrderSearchRepository, times(1)).save(testGameOrder);
    }

    @Test
    @Transactional
    public void createGameOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gameOrderRepository.findAll().size();

        // Create the GameOrder with an existing ID
        gameOrder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameOrderMockMvc.perform(post("/api/game-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameOrder)))
            .andExpect(status().isBadRequest());

        // Validate the GameOrder in the database
        List<GameOrder> gameOrderList = gameOrderRepository.findAll();
        assertThat(gameOrderList).hasSize(databaseSizeBeforeCreate);

        // Validate the GameOrder in Elasticsearch
        verify(mockGameOrderSearchRepository, times(0)).save(gameOrder);
    }

    @Test
    @Transactional
    public void checkDirectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameOrderRepository.findAll().size();
        // set the field null
        gameOrder.setDirection(null);

        // Create the GameOrder, which fails.

        restGameOrderMockMvc.perform(post("/api/game-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameOrder)))
            .andExpect(status().isBadRequest());

        List<GameOrder> gameOrderList = gameOrderRepository.findAll();
        assertThat(gameOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGameOrders() throws Exception {
        // Initialize the database
        gameOrderRepository.saveAndFlush(gameOrder);

        // Get all the gameOrderList
        restGameOrderMockMvc.perform(get("/api/game-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())));
    }
    
    @Test
    @Transactional
    public void getGameOrder() throws Exception {
        // Initialize the database
        gameOrderRepository.saveAndFlush(gameOrder);

        // Get the gameOrder
        restGameOrderMockMvc.perform(get("/api/game-orders/{id}", gameOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gameOrder.getId().intValue()))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGameOrder() throws Exception {
        // Get the gameOrder
        restGameOrderMockMvc.perform(get("/api/game-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGameOrder() throws Exception {
        // Initialize the database
        gameOrderRepository.saveAndFlush(gameOrder);

        int databaseSizeBeforeUpdate = gameOrderRepository.findAll().size();

        // Update the gameOrder
        GameOrder updatedGameOrder = gameOrderRepository.findById(gameOrder.getId()).get();
        // Disconnect from session so that the updates on updatedGameOrder are not directly saved in db
        em.detach(updatedGameOrder);
        updatedGameOrder
            .direction(UPDATED_DIRECTION);

        restGameOrderMockMvc.perform(put("/api/game-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGameOrder)))
            .andDo(print())
            .andExpect(status().isOk());

        // Validate the GameOrder in the database
        List<GameOrder> gameOrderList = gameOrderRepository.findAll();
        assertThat(gameOrderList).hasSize(databaseSizeBeforeUpdate);
        GameOrder testGameOrder = gameOrderList.get(gameOrderList.size() - 1);
        assertThat(testGameOrder.getDirection()).isEqualTo(UPDATED_DIRECTION);

        // Validate the GameOrder in Elasticsearch
        verify(mockGameOrderSearchRepository, times(1)).save(testGameOrder);
    }

    @Test
    @Transactional
    public void updateNonExistingGameOrder() throws Exception {
        int databaseSizeBeforeUpdate = gameOrderRepository.findAll().size();

        // Create the GameOrder

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameOrderMockMvc.perform(put("/api/game-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameOrder)))
            .andExpect(status().isBadRequest());

        // Validate the GameOrder in the database
        List<GameOrder> gameOrderList = gameOrderRepository.findAll();
        assertThat(gameOrderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GameOrder in Elasticsearch
        verify(mockGameOrderSearchRepository, times(0)).save(gameOrder);
    }

    @Test
    @Transactional
    public void deleteGameOrder() throws Exception {
        // Initialize the database
        gameOrderRepository.saveAndFlush(gameOrder);

        int databaseSizeBeforeDelete = gameOrderRepository.findAll().size();

        // Get the gameOrder
        restGameOrderMockMvc.perform(delete("/api/game-orders/{id}", gameOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GameOrder> gameOrderList = gameOrderRepository.findAll();
        assertThat(gameOrderList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the GameOrder in Elasticsearch
        verify(mockGameOrderSearchRepository, times(1)).deleteById(gameOrder.getId());
    }

    @Test
    @Transactional
    public void searchGameOrder() throws Exception {
        // Initialize the database
        gameOrderRepository.saveAndFlush(gameOrder);
        when(mockGameOrderSearchRepository.search(queryStringQuery("id:" + gameOrder.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(gameOrder), PageRequest.of(0, 1), 1));
        // Search the gameOrder
        restGameOrderMockMvc.perform(get("/api/_search/game-orders?query=id:" + gameOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameOrder.class);
        GameOrder gameOrder1 = new GameOrder();
        gameOrder1.setId(1L);
        GameOrder gameOrder2 = new GameOrder();
        gameOrder2.setId(gameOrder1.getId());
        assertThat(gameOrder1).isEqualTo(gameOrder2);
        gameOrder2.setId(2L);
        assertThat(gameOrder1).isNotEqualTo(gameOrder2);
        gameOrder1.setId(null);
        assertThat(gameOrder1).isNotEqualTo(gameOrder2);
    }
}
