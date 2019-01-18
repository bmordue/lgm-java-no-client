package me.bmordue.lgm.web.rest;

import com.codahale.metrics.annotation.Timed;
import me.bmordue.lgm.domain.TurnOutcome;
import me.bmordue.lgm.repository.TurnOutcomeRepository;
import me.bmordue.lgm.web.rest.errors.BadRequestAlertException;
import me.bmordue.lgm.web.rest.util.HeaderUtil;
import me.bmordue.lgm.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing TurnOutcome.
 */
@RestController
@RequestMapping("/api")
public class TurnOutcomeResource {

    private final Logger log = LoggerFactory.getLogger(TurnOutcomeResource.class);

    private static final String ENTITY_NAME = "turnOutcome";

    private final TurnOutcomeRepository turnOutcomeRepository;

    public TurnOutcomeResource(TurnOutcomeRepository turnOutcomeRepository) {
        this.turnOutcomeRepository = turnOutcomeRepository;
    }

    /**
     * POST  /turn-outcomes : Create a new turnOutcome.
     *
     * @param turnOutcome the turnOutcome to create
     * @return the ResponseEntity with status 201 (Created) and with body the new turnOutcome, or with status 400 (Bad Request) if the turnOutcome has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/turn-outcomes")
    @Timed
    public ResponseEntity<TurnOutcome> createTurnOutcome(@Valid @RequestBody TurnOutcome turnOutcome) throws URISyntaxException {
        log.debug("REST request to save TurnOutcome : {}", turnOutcome);
        if (turnOutcome.getId() != null) {
            throw new BadRequestAlertException("A new turnOutcome cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TurnOutcome result = turnOutcomeRepository.save(turnOutcome);
        return ResponseEntity.created(new URI("/api/turn-outcomes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /turn-outcomes : Updates an existing turnOutcome.
     *
     * @param turnOutcome the turnOutcome to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated turnOutcome,
     * or with status 400 (Bad Request) if the turnOutcome is not valid,
     * or with status 500 (Internal Server Error) if the turnOutcome couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/turn-outcomes")
    @Timed
    public ResponseEntity<TurnOutcome> updateTurnOutcome(@Valid @RequestBody TurnOutcome turnOutcome) throws URISyntaxException {
        log.debug("REST request to update TurnOutcome : {}", turnOutcome);
        if (turnOutcome.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TurnOutcome result = turnOutcomeRepository.save(turnOutcome);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, turnOutcome.getId().toString()))
            .body(result);
    }

    /**
     * GET  /turn-outcomes : get all the turnOutcomes.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of turnOutcomes in body
     */
    @GetMapping("/turn-outcomes")
    @Timed
    public ResponseEntity<List<TurnOutcome>> getAllTurnOutcomes(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("turn-is-null".equals(filter)) {
            log.debug("REST request to get all TurnOutcomes where turn is null");
            return new ResponseEntity<>(StreamSupport
                .stream(turnOutcomeRepository.findAll().spliterator(), false)
                .filter(turnOutcome -> turnOutcome.getTurn() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        log.debug("REST request to get a page of TurnOutcomes");
        Page<TurnOutcome> page = turnOutcomeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/turn-outcomes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /turn-outcomes/:id : get the "id" turnOutcome.
     *
     * @param id the id of the turnOutcome to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the turnOutcome, or with status 404 (Not Found)
     */
    @GetMapping("/turn-outcomes/{id}")
    @Timed
    public ResponseEntity<TurnOutcome> getTurnOutcome(@PathVariable Long id) {
        log.debug("REST request to get TurnOutcome : {}", id);
        Optional<TurnOutcome> turnOutcome = turnOutcomeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(turnOutcome);
    }

    /**
     * DELETE  /turn-outcomes/:id : delete the "id" turnOutcome.
     *
     * @param id the id of the turnOutcome to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/turn-outcomes/{id}")
    @Timed
    public ResponseEntity<Void> deleteTurnOutcome(@PathVariable Long id) {
        log.debug("REST request to delete TurnOutcome : {}", id);

        turnOutcomeRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
