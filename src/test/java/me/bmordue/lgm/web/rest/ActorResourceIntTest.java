package me.bmordue.lgm.web.rest;

import me.bmordue.lgm.LgmApp;

import me.bmordue.lgm.domain.Actor;
import me.bmordue.lgm.domain.GameTurn;
import me.bmordue.lgm.domain.Player;
import me.bmordue.lgm.repository.ActorRepository;
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

import me.bmordue.lgm.domain.enumeration.ActorState;
/**
 * Test class for the ActorResource REST controller.
 *
 * @see ActorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LgmApp.class)
public class ActorResourceIntTest {

    private static final ActorState DEFAULT_STATE = ActorState.ALIVE;
    private static final ActorState UPDATED_STATE = ActorState.DEAD;

    private static final Integer DEFAULT_POS_X = 1;
    private static final Integer UPDATED_POS_X = 2;

    private static final Integer DEFAULT_POS_Y = 1;
    private static final Integer UPDATED_POS_Y = 2;

    @Autowired
    private ActorRepository actorRepository;

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

    private MockMvc restActorMockMvc;

    private Actor actor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActorResource actorResource = new ActorResource(actorRepository);
        this.restActorMockMvc = MockMvcBuilders.standaloneSetup(actorResource)
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
    public static Actor createEntity(EntityManager em) {
        Actor actor = new Actor()
            .state(DEFAULT_STATE)
            .posX(DEFAULT_POS_X)
            .posY(DEFAULT_POS_Y);
        // Add required entity
        GameTurn gameTurn = GameTurnResourceIntTest.createEntity(em);
        em.persist(gameTurn);
        em.flush();
        actor.setTurn(gameTurn);
        // Add required entity
        Player player = PlayerResourceIntTest.createEntity(em);
        em.persist(player);
        em.flush();
        actor.setPlayer(player);
        return actor;
    }

    @Before
    public void initTest() {
        actor = createEntity(em);
    }

    @Test
    @Transactional
    public void createActor() throws Exception {
        int databaseSizeBeforeCreate = actorRepository.findAll().size();

        // Create the Actor
        restActorMockMvc.perform(post("/api/actors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actor)))
            .andExpect(status().isCreated());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeCreate + 1);
        Actor testActor = actorList.get(actorList.size() - 1);
        assertThat(testActor.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testActor.getPosX()).isEqualTo(DEFAULT_POS_X);
        assertThat(testActor.getPosY()).isEqualTo(DEFAULT_POS_Y);
    }

    @Test
    @Transactional
    public void createActorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = actorRepository.findAll().size();

        // Create the Actor with an existing ID
        actor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActorMockMvc.perform(post("/api/actors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actor)))
            .andExpect(status().isBadRequest());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = actorRepository.findAll().size();
        // set the field null
        actor.setState(null);

        // Create the Actor, which fails.

        restActorMockMvc.perform(post("/api/actors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actor)))
            .andExpect(status().isBadRequest());

        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPosXIsRequired() throws Exception {
        int databaseSizeBeforeTest = actorRepository.findAll().size();
        // set the field null
        actor.setPosX(null);

        // Create the Actor, which fails.

        restActorMockMvc.perform(post("/api/actors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actor)))
            .andExpect(status().isBadRequest());

        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPosYIsRequired() throws Exception {
        int databaseSizeBeforeTest = actorRepository.findAll().size();
        // set the field null
        actor.setPosY(null);

        // Create the Actor, which fails.

        restActorMockMvc.perform(post("/api/actors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actor)))
            .andExpect(status().isBadRequest());

        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActors() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList
        restActorMockMvc.perform(get("/api/actors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actor.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].posX").value(hasItem(DEFAULT_POS_X)))
            .andExpect(jsonPath("$.[*].posY").value(hasItem(DEFAULT_POS_Y)));
    }
    
    @Test
    @Transactional
    public void getActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get the actor
        restActorMockMvc.perform(get("/api/actors/{id}", actor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(actor.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.posX").value(DEFAULT_POS_X))
            .andExpect(jsonPath("$.posY").value(DEFAULT_POS_Y));
    }

    @Test
    @Transactional
    public void getNonExistingActor() throws Exception {
        // Get the actor
        restActorMockMvc.perform(get("/api/actors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        int databaseSizeBeforeUpdate = actorRepository.findAll().size();

        // Update the actor
        Actor updatedActor = actorRepository.findById(actor.getId()).get();
        // Disconnect from session so that the updates on updatedActor are not directly saved in db
        em.detach(updatedActor);
        updatedActor
            .state(UPDATED_STATE)
            .posX(UPDATED_POS_X)
            .posY(UPDATED_POS_Y);

        restActorMockMvc.perform(put("/api/actors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedActor)))
            .andExpect(status().isOk());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);
        Actor testActor = actorList.get(actorList.size() - 1);
        assertThat(testActor.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testActor.getPosX()).isEqualTo(UPDATED_POS_X);
        assertThat(testActor.getPosY()).isEqualTo(UPDATED_POS_Y);
    }

    @Test
    @Transactional
    public void updateNonExistingActor() throws Exception {
        int databaseSizeBeforeUpdate = actorRepository.findAll().size();

        // Create the Actor

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActorMockMvc.perform(put("/api/actors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actor)))
            .andExpect(status().isBadRequest());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        int databaseSizeBeforeDelete = actorRepository.findAll().size();

        // Get the actor
        restActorMockMvc.perform(delete("/api/actors/{id}", actor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Actor.class);
        Actor actor1 = new Actor();
        actor1.setId(1L);
        Actor actor2 = new Actor();
        actor2.setId(actor1.getId());
        assertThat(actor1).isEqualTo(actor2);
        actor2.setId(2L);
        assertThat(actor1).isNotEqualTo(actor2);
        actor1.setId(null);
        assertThat(actor1).isNotEqualTo(actor2);
    }
}
