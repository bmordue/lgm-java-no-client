package me.bmordue.lgm.web.rest;

import me.bmordue.lgm.LgmApp;

import me.bmordue.lgm.domain.ActorCommand;
import me.bmordue.lgm.domain.Actor;
import me.bmordue.lgm.repository.ActorCommandRepository;
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

import me.bmordue.lgm.domain.enumeration.Direction;
/**
 * Test class for the ActorCommandResource REST controller.
 *
 * @see ActorCommandResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LgmApp.class)
public class ActorCommandResourceIntTest {

    private static final Direction DEFAULT_DIRECTION = Direction.UP_LEFT;
    private static final Direction UPDATED_DIRECTION = Direction.UP_RIGHT;

    @Autowired
    private ActorCommandRepository actorCommandRepository;

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

    private MockMvc restActorCommandMockMvc;

    private ActorCommand actorCommand;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActorCommandResource actorCommandResource = new ActorCommandResource(actorCommandRepository);
        this.restActorCommandMockMvc = MockMvcBuilders.standaloneSetup(actorCommandResource)
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
    public static ActorCommand createEntity(EntityManager em) {
        ActorCommand actorCommand = new ActorCommand()
            .direction(DEFAULT_DIRECTION);
        // Add required entity
        Actor actor = ActorResourceIntTest.createEntity(em);
        em.persist(actor);
        em.flush();
        actorCommand.setActor(actor);
        return actorCommand;
    }

    @Before
    public void initTest() {
        actorCommand = createEntity(em);
    }

    @Test
    @Transactional
    public void createActorCommand() throws Exception {
        int databaseSizeBeforeCreate = actorCommandRepository.findAll().size();

        // Create the ActorCommand
        restActorCommandMockMvc.perform(post("/api/actor-commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actorCommand)))
            .andExpect(status().isCreated());

        // Validate the ActorCommand in the database
        List<ActorCommand> actorCommandList = actorCommandRepository.findAll();
        assertThat(actorCommandList).hasSize(databaseSizeBeforeCreate + 1);
        ActorCommand testActorCommand = actorCommandList.get(actorCommandList.size() - 1);
        assertThat(testActorCommand.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    @Transactional
    public void createActorCommandWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = actorCommandRepository.findAll().size();

        // Create the ActorCommand with an existing ID
        actorCommand.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActorCommandMockMvc.perform(post("/api/actor-commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actorCommand)))
            .andExpect(status().isBadRequest());

        // Validate the ActorCommand in the database
        List<ActorCommand> actorCommandList = actorCommandRepository.findAll();
        assertThat(actorCommandList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDirectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = actorCommandRepository.findAll().size();
        // set the field null
        actorCommand.setDirection(null);

        // Create the ActorCommand, which fails.

        restActorCommandMockMvc.perform(post("/api/actor-commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actorCommand)))
            .andExpect(status().isBadRequest());

        List<ActorCommand> actorCommandList = actorCommandRepository.findAll();
        assertThat(actorCommandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActorCommands() throws Exception {
        // Initialize the database
        actorCommandRepository.saveAndFlush(actorCommand);

        // Get all the actorCommandList
        restActorCommandMockMvc.perform(get("/api/actor-commands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actorCommand.getId().intValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())));
    }
    
    @Test
    @Transactional
    public void getActorCommand() throws Exception {
        // Initialize the database
        actorCommandRepository.saveAndFlush(actorCommand);

        // Get the actorCommand
        restActorCommandMockMvc.perform(get("/api/actor-commands/{id}", actorCommand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(actorCommand.getId().intValue()))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingActorCommand() throws Exception {
        // Get the actorCommand
        restActorCommandMockMvc.perform(get("/api/actor-commands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActorCommand() throws Exception {
        // Initialize the database
        actorCommandRepository.saveAndFlush(actorCommand);

        int databaseSizeBeforeUpdate = actorCommandRepository.findAll().size();

        // Update the actorCommand
        ActorCommand updatedActorCommand = actorCommandRepository.findById(actorCommand.getId()).get();
        // Disconnect from session so that the updates on updatedActorCommand are not directly saved in db
        em.detach(updatedActorCommand);
        updatedActorCommand
            .direction(UPDATED_DIRECTION);

        restActorCommandMockMvc.perform(put("/api/actor-commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedActorCommand)))
            .andExpect(status().isOk());

        // Validate the ActorCommand in the database
        List<ActorCommand> actorCommandList = actorCommandRepository.findAll();
        assertThat(actorCommandList).hasSize(databaseSizeBeforeUpdate);
        ActorCommand testActorCommand = actorCommandList.get(actorCommandList.size() - 1);
        assertThat(testActorCommand.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    public void updateNonExistingActorCommand() throws Exception {
        int databaseSizeBeforeUpdate = actorCommandRepository.findAll().size();

        // Create the ActorCommand

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActorCommandMockMvc.perform(put("/api/actor-commands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actorCommand)))
            .andExpect(status().isBadRequest());

        // Validate the ActorCommand in the database
        List<ActorCommand> actorCommandList = actorCommandRepository.findAll();
        assertThat(actorCommandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActorCommand() throws Exception {
        // Initialize the database
        actorCommandRepository.saveAndFlush(actorCommand);

        int databaseSizeBeforeDelete = actorCommandRepository.findAll().size();

        // Get the actorCommand
        restActorCommandMockMvc.perform(delete("/api/actor-commands/{id}", actorCommand.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ActorCommand> actorCommandList = actorCommandRepository.findAll();
        assertThat(actorCommandList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActorCommand.class);
        ActorCommand actorCommand1 = new ActorCommand();
        actorCommand1.setId(1L);
        ActorCommand actorCommand2 = new ActorCommand();
        actorCommand2.setId(actorCommand1.getId());
        assertThat(actorCommand1).isEqualTo(actorCommand2);
        actorCommand2.setId(2L);
        assertThat(actorCommand1).isNotEqualTo(actorCommand2);
        actorCommand1.setId(null);
        assertThat(actorCommand1).isNotEqualTo(actorCommand2);
    }
}
