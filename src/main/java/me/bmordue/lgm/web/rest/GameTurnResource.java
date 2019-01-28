package me.bmordue.lgm.web.rest;

import com.codahale.metrics.annotation.Timed;
import me.bmordue.lgm.domain.GameTurn;
import me.bmordue.lgm.repository.GameTurnRepository;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing GameTurn.
 */
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ROLE_USER')")
public class GameTurnResource {

    private final Logger log = LoggerFactory.getLogger(GameTurnResource.class);

    private static final String ENTITY_NAME = "gameTurn";

    private final GameTurnRepository gameTurnRepository;

    public GameTurnResource(GameTurnRepository gameTurnRepository) {
        this.gameTurnRepository = gameTurnRepository;
    }

    /**
     * POST  /game-turns : Create a new gameTurn.
     *
     * @param gameTurn the gameTurn to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gameTurn, or with status 400 (Bad Request) if the gameTurn has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/game-turns")
    @Timed
    public ResponseEntity<GameTurn> createGameTurn(@Valid @RequestBody GameTurn gameTurn) throws URISyntaxException {
        log.debug("REST request to save GameTurn : {}", gameTurn);
        if (gameTurn.getId() != null) {
            throw new BadRequestAlertException("A new gameTurn cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameTurn result = gameTurnRepository.save(gameTurn);
        return ResponseEntity.created(new URI("/api/game-turns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /game-turns : Updates an existing gameTurn.
     *
     * @param gameTurn the gameTurn to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gameTurn,
     * or with status 400 (Bad Request) if the gameTurn is not valid,
     * or with status 500 (Internal Server Error) if the gameTurn couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/game-turns")
    @Timed
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GameTurn> updateGameTurn(@Valid @RequestBody GameTurn gameTurn) throws URISyntaxException {
        log.debug("REST request to update GameTurn : {}", gameTurn);
        if (gameTurn.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GameTurn result = gameTurnRepository.save(gameTurn);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gameTurn.getId().toString()))
            .body(result);
    }

    /**
     * GET  /game-turns : get all the gameTurns.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of gameTurns in body
     */
    @GetMapping("/game-turns")
    @Timed
    public ResponseEntity<List<GameTurn>> getAllGameTurns(Pageable pageable) {
        log.debug("REST request to get a page of GameTurns");
        Page<GameTurn> page = gameTurnRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/game-turns");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /game-turns/:id : get the "id" gameTurn.
     *
     * @param id the id of the gameTurn to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gameTurn, or with status 404 (Not Found)
     */
    @GetMapping("/game-turns/{id}")
    @Timed
    public ResponseEntity<GameTurn> getGameTurn(@PathVariable Long id) {
        log.debug("REST request to get GameTurn : {}", id);
        Optional<GameTurn> gameTurn = gameTurnRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(gameTurn);
    }

    /**
     * DELETE  /game-turns/:id : delete the "id" gameTurn.
     *
     * @param id the id of the gameTurn to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/game-turns/{id}")
    @Timed
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteGameTurn(@PathVariable Long id) {
        log.debug("REST request to delete GameTurn : {}", id);

        gameTurnRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
