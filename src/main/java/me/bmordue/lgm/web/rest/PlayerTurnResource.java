package me.bmordue.lgm.web.rest;

import com.codahale.metrics.annotation.Timed;
import me.bmordue.lgm.domain.PlayerTurn;
import me.bmordue.lgm.repository.PlayerTurnRepository;
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

/**
 * REST controller for managing PlayerTurn.
 */
@RestController
@RequestMapping("/api")
public class PlayerTurnResource {

    private final Logger log = LoggerFactory.getLogger(PlayerTurnResource.class);

    private static final String ENTITY_NAME = "playerTurn";

    private final PlayerTurnRepository playerTurnRepository;

    public PlayerTurnResource(PlayerTurnRepository playerTurnRepository) {
        this.playerTurnRepository = playerTurnRepository;
    }

    /**
     * POST  /player-turns : Create a new playerTurn.
     *
     * @param playerTurn the playerTurn to create
     * @return the ResponseEntity with status 201 (Created) and with body the new playerTurn, or with status 400 (Bad Request) if the playerTurn has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/player-turns")
    @Timed
    public ResponseEntity<PlayerTurn> createPlayerTurn(@Valid @RequestBody PlayerTurn playerTurn) throws URISyntaxException {
        log.debug("REST request to save PlayerTurn : {}", playerTurn);
        if (playerTurn.getId() != null) {
            throw new BadRequestAlertException("A new playerTurn cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlayerTurn result = playerTurnRepository.save(playerTurn);
        return ResponseEntity.created(new URI("/api/player-turns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /player-turns : Updates an existing playerTurn.
     *
     * @param playerTurn the playerTurn to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated playerTurn,
     * or with status 400 (Bad Request) if the playerTurn is not valid,
     * or with status 500 (Internal Server Error) if the playerTurn couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/player-turns")
    @Timed
    public ResponseEntity<PlayerTurn> updatePlayerTurn(@Valid @RequestBody PlayerTurn playerTurn) throws URISyntaxException {
        log.debug("REST request to update PlayerTurn : {}", playerTurn);
        if (playerTurn.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlayerTurn result = playerTurnRepository.save(playerTurn);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, playerTurn.getId().toString()))
            .body(result);
    }

    /**
     * GET  /player-turns : get all the playerTurns.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of playerTurns in body
     */
    @GetMapping("/player-turns")
    @Timed
    public ResponseEntity<List<PlayerTurn>> getAllPlayerTurns(Pageable pageable) {
        log.debug("REST request to get a page of PlayerTurns");
        Page<PlayerTurn> page = playerTurnRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/player-turns");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /player-turns/:id : get the "id" playerTurn.
     *
     * @param id the id of the playerTurn to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the playerTurn, or with status 404 (Not Found)
     */
    @GetMapping("/player-turns/{id}")
    @Timed
    public ResponseEntity<PlayerTurn> getPlayerTurn(@PathVariable Long id) {
        log.debug("REST request to get PlayerTurn : {}", id);
        Optional<PlayerTurn> playerTurn = playerTurnRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(playerTurn);
    }

    /**
     * DELETE  /player-turns/:id : delete the "id" playerTurn.
     *
     * @param id the id of the playerTurn to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/player-turns/{id}")
    @Timed
    public ResponseEntity<Void> deletePlayerTurn(@PathVariable Long id) {
        log.debug("REST request to delete PlayerTurn : {}", id);

        playerTurnRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
