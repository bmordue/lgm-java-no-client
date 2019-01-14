package me.bmordue.lgm.web.rest;

import com.codahale.metrics.annotation.Timed;
import me.bmordue.lgm.service.TurnOutcomeService;
import me.bmordue.lgm.web.rest.errors.BadRequestAlertException;
import me.bmordue.lgm.web.rest.util.HeaderUtil;
import me.bmordue.lgm.web.rest.util.PaginationUtil;
import me.bmordue.lgm.service.dto.TurnOutcomeDTO;
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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing TurnOutcome.
 */
@RestController
@RequestMapping("/api")
public class TurnOutcomeResource {

    private final Logger log = LoggerFactory.getLogger(TurnOutcomeResource.class);

    private static final String ENTITY_NAME = "turnOutcome";

    private final TurnOutcomeService turnOutcomeService;

    public TurnOutcomeResource(TurnOutcomeService turnOutcomeService) {
        this.turnOutcomeService = turnOutcomeService;
    }

    /**
     * POST  /turn-outcomes : Create a new turnOutcome.
     *
     * @param turnOutcomeDTO the turnOutcomeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new turnOutcomeDTO, or with status 400 (Bad Request) if the turnOutcome has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/turn-outcomes")
    @Timed
    public ResponseEntity<TurnOutcomeDTO> createTurnOutcome(@Valid @RequestBody TurnOutcomeDTO turnOutcomeDTO) throws URISyntaxException {
        log.debug("REST request to save TurnOutcome : {}", turnOutcomeDTO);
        if (turnOutcomeDTO.getId() != null) {
            throw new BadRequestAlertException("A new turnOutcome cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TurnOutcomeDTO result = turnOutcomeService.save(turnOutcomeDTO);
        return ResponseEntity.created(new URI("/api/turn-outcomes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /turn-outcomes : Updates an existing turnOutcome.
     *
     * @param turnOutcomeDTO the turnOutcomeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated turnOutcomeDTO,
     * or with status 400 (Bad Request) if the turnOutcomeDTO is not valid,
     * or with status 500 (Internal Server Error) if the turnOutcomeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/turn-outcomes")
    @Timed
    public ResponseEntity<TurnOutcomeDTO> updateTurnOutcome(@Valid @RequestBody TurnOutcomeDTO turnOutcomeDTO) throws URISyntaxException {
        log.debug("REST request to update TurnOutcome : {}", turnOutcomeDTO);
        if (turnOutcomeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TurnOutcomeDTO result = turnOutcomeService.save(turnOutcomeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, turnOutcomeDTO.getId().toString()))
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
    public ResponseEntity<List<TurnOutcomeDTO>> getAllTurnOutcomes(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("turn-is-null".equals(filter)) {
            log.debug("REST request to get all TurnOutcomes where turn is null");
            return new ResponseEntity<>(turnOutcomeService.findAllWhereTurnIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of TurnOutcomes");
        Page<TurnOutcomeDTO> page = turnOutcomeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/turn-outcomes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /turn-outcomes/:id : get the "id" turnOutcome.
     *
     * @param id the id of the turnOutcomeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the turnOutcomeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/turn-outcomes/{id}")
    @Timed
    public ResponseEntity<TurnOutcomeDTO> getTurnOutcome(@PathVariable Long id) {
        log.debug("REST request to get TurnOutcome : {}", id);
        Optional<TurnOutcomeDTO> turnOutcomeDTO = turnOutcomeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(turnOutcomeDTO);
    }

    /**
     * DELETE  /turn-outcomes/:id : delete the "id" turnOutcome.
     *
     * @param id the id of the turnOutcomeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/turn-outcomes/{id}")
    @Timed
    public ResponseEntity<Void> deleteTurnOutcome(@PathVariable Long id) {
        log.debug("REST request to delete TurnOutcome : {}", id);
        turnOutcomeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
