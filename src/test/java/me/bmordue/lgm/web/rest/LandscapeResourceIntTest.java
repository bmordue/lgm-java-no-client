package me.bmordue.lgm.web.rest;

import me.bmordue.lgm.LgmApp;

import me.bmordue.lgm.domain.Landscape;
import me.bmordue.lgm.domain.GameTurn;
import me.bmordue.lgm.repository.LandscapeRepository;
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
 * Test class for the LandscapeResource REST controller.
 *
 * @see LandscapeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LgmApp.class)
public class LandscapeResourceIntTest {

    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;

    private static final String DEFAULT_CELLS = "AAAAAAAAAA";
    private static final String UPDATED_CELLS = "BBBBBBBBBB";

    @Autowired
    private LandscapeRepository landscapeRepository;

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

    private MockMvc restLandscapeMockMvc;

    private Landscape landscape;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LandscapeResource landscapeResource = new LandscapeResource(landscapeRepository);
        this.restLandscapeMockMvc = MockMvcBuilders.standaloneSetup(landscapeResource)
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
    public static Landscape createEntity(EntityManager em) {
        Landscape landscape = new Landscape()
            .width(DEFAULT_WIDTH)
            .height(DEFAULT_HEIGHT)
            .cells(DEFAULT_CELLS);
        // Add required entity
        GameTurn gameTurn = GameTurnResourceIntTest.createEntity(em);
        em.persist(gameTurn);
        em.flush();
        landscape.setTurn(gameTurn);
        return landscape;
    }

    @Before
    public void initTest() {
        landscape = createEntity(em);
    }

    @Test
    @Transactional
    public void createLandscape() throws Exception {
        int databaseSizeBeforeCreate = landscapeRepository.findAll().size();

        // Create the Landscape
        restLandscapeMockMvc.perform(post("/api/landscapes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landscape)))
            .andExpect(status().isCreated());

        // Validate the Landscape in the database
        List<Landscape> landscapeList = landscapeRepository.findAll();
        assertThat(landscapeList).hasSize(databaseSizeBeforeCreate + 1);
        Landscape testLandscape = landscapeList.get(landscapeList.size() - 1);
        assertThat(testLandscape.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testLandscape.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testLandscape.getCells()).isEqualTo(DEFAULT_CELLS);
    }

    @Test
    @Transactional
    public void createLandscapeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = landscapeRepository.findAll().size();

        // Create the Landscape with an existing ID
        landscape.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLandscapeMockMvc.perform(post("/api/landscapes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landscape)))
            .andExpect(status().isBadRequest());

        // Validate the Landscape in the database
        List<Landscape> landscapeList = landscapeRepository.findAll();
        assertThat(landscapeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkWidthIsRequired() throws Exception {
        int databaseSizeBeforeTest = landscapeRepository.findAll().size();
        // set the field null
        landscape.setWidth(null);

        // Create the Landscape, which fails.

        restLandscapeMockMvc.perform(post("/api/landscapes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landscape)))
            .andExpect(status().isBadRequest());

        List<Landscape> landscapeList = landscapeRepository.findAll();
        assertThat(landscapeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = landscapeRepository.findAll().size();
        // set the field null
        landscape.setHeight(null);

        // Create the Landscape, which fails.

        restLandscapeMockMvc.perform(post("/api/landscapes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landscape)))
            .andExpect(status().isBadRequest());

        List<Landscape> landscapeList = landscapeRepository.findAll();
        assertThat(landscapeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCellsIsRequired() throws Exception {
        int databaseSizeBeforeTest = landscapeRepository.findAll().size();
        // set the field null
        landscape.setCells(null);

        // Create the Landscape, which fails.

        restLandscapeMockMvc.perform(post("/api/landscapes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landscape)))
            .andExpect(status().isBadRequest());

        List<Landscape> landscapeList = landscapeRepository.findAll();
        assertThat(landscapeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLandscapes() throws Exception {
        // Initialize the database
        landscapeRepository.saveAndFlush(landscape);

        // Get all the landscapeList
        restLandscapeMockMvc.perform(get("/api/landscapes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(landscape.getId().intValue())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].cells").value(hasItem(DEFAULT_CELLS.toString())));
    }
    
    @Test
    @Transactional
    public void getLandscape() throws Exception {
        // Initialize the database
        landscapeRepository.saveAndFlush(landscape);

        // Get the landscape
        restLandscapeMockMvc.perform(get("/api/landscapes/{id}", landscape.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(landscape.getId().intValue()))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.cells").value(DEFAULT_CELLS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLandscape() throws Exception {
        // Get the landscape
        restLandscapeMockMvc.perform(get("/api/landscapes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLandscape() throws Exception {
        // Initialize the database
        landscapeRepository.saveAndFlush(landscape);

        int databaseSizeBeforeUpdate = landscapeRepository.findAll().size();

        // Update the landscape
        Landscape updatedLandscape = landscapeRepository.findById(landscape.getId()).get();
        // Disconnect from session so that the updates on updatedLandscape are not directly saved in db
        em.detach(updatedLandscape);
        updatedLandscape
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .cells(UPDATED_CELLS);

        restLandscapeMockMvc.perform(put("/api/landscapes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLandscape)))
            .andExpect(status().isOk());

        // Validate the Landscape in the database
        List<Landscape> landscapeList = landscapeRepository.findAll();
        assertThat(landscapeList).hasSize(databaseSizeBeforeUpdate);
        Landscape testLandscape = landscapeList.get(landscapeList.size() - 1);
        assertThat(testLandscape.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testLandscape.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testLandscape.getCells()).isEqualTo(UPDATED_CELLS);
    }

    @Test
    @Transactional
    public void updateNonExistingLandscape() throws Exception {
        int databaseSizeBeforeUpdate = landscapeRepository.findAll().size();

        // Create the Landscape

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLandscapeMockMvc.perform(put("/api/landscapes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(landscape)))
            .andExpect(status().isBadRequest());

        // Validate the Landscape in the database
        List<Landscape> landscapeList = landscapeRepository.findAll();
        assertThat(landscapeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLandscape() throws Exception {
        // Initialize the database
        landscapeRepository.saveAndFlush(landscape);

        int databaseSizeBeforeDelete = landscapeRepository.findAll().size();

        // Get the landscape
        restLandscapeMockMvc.perform(delete("/api/landscapes/{id}", landscape.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Landscape> landscapeList = landscapeRepository.findAll();
        assertThat(landscapeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Landscape.class);
        Landscape landscape1 = new Landscape();
        landscape1.setId(1L);
        Landscape landscape2 = new Landscape();
        landscape2.setId(landscape1.getId());
        assertThat(landscape1).isEqualTo(landscape2);
        landscape2.setId(2L);
        assertThat(landscape1).isNotEqualTo(landscape2);
        landscape1.setId(null);
        assertThat(landscape1).isNotEqualTo(landscape2);
    }
}
