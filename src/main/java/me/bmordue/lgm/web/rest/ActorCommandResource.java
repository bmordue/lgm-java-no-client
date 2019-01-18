package me.bmordue.lgm.web.rest;

import com.codahale.metrics.annotation.Timed;
import me.bmordue.lgm.domain.ActorCommand;
import me.bmordue.lgm.repository.ActorCommandRepository;
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
 * REST controller for managing ActorCommand.
 */
@RestController
@RequestMapping("/api")
public class ActorCommandResource {

    private final Logger log = LoggerFactory.getLogger(ActorCommandResource.class);

    private static final String ENTITY_NAME = "actorCommand";

    private final ActorCommandRepository actorCommandRepository;

    public ActorCommandResource(ActorCommandRepository actorCommandRepository) {
        this.actorCommandRepository = actorCommandRepository;
    }

    /**
     * POST  /actor-commands : Create a new actorCommand.
     *
     * @param actorCommand the actorCommand to create
     * @return the ResponseEntity with status 201 (Created) and with body the new actorCommand, or with status 400 (Bad Request) if the actorCommand has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/actor-commands")
    @Timed
    public ResponseEntity<ActorCommand> createActorCommand(@Valid @RequestBody ActorCommand actorCommand) throws URISyntaxException {
        log.debug("REST request to save ActorCommand : {}", actorCommand);
        if (actorCommand.getId() != null) {
            throw new BadRequestAlertException("A new actorCommand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActorCommand result = actorCommandRepository.save(actorCommand);
        return ResponseEntity.created(new URI("/api/actor-commands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /actor-commands : Updates an existing actorCommand.
     *
     * @param actorCommand the actorCommand to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated actorCommand,
     * or with status 400 (Bad Request) if the actorCommand is not valid,
     * or with status 500 (Internal Server Error) if the actorCommand couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/actor-commands")
    @Timed
    public ResponseEntity<ActorCommand> updateActorCommand(@Valid @RequestBody ActorCommand actorCommand) throws URISyntaxException {
        log.debug("REST request to update ActorCommand : {}", actorCommand);
        if (actorCommand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ActorCommand result = actorCommandRepository.save(actorCommand);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, actorCommand.getId().toString()))
            .body(result);
    }

    /**
     * GET  /actor-commands : get all the actorCommands.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of actorCommands in body
     */
    @GetMapping("/actor-commands")
    @Timed
    public ResponseEntity<List<ActorCommand>> getAllActorCommands(Pageable pageable) {
        log.debug("REST request to get a page of ActorCommands");
        Page<ActorCommand> page = actorCommandRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/actor-commands");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /actor-commands/:id : get the "id" actorCommand.
     *
     * @param id the id of the actorCommand to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the actorCommand, or with status 404 (Not Found)
     */
    @GetMapping("/actor-commands/{id}")
    @Timed
    public ResponseEntity<ActorCommand> getActorCommand(@PathVariable Long id) {
        log.debug("REST request to get ActorCommand : {}", id);
        Optional<ActorCommand> actorCommand = actorCommandRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(actorCommand);
    }

    /**
     * DELETE  /actor-commands/:id : delete the "id" actorCommand.
     *
     * @param id the id of the actorCommand to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/actor-commands/{id}")
    @Timed
    public ResponseEntity<Void> deleteActorCommand(@PathVariable Long id) {
        log.debug("REST request to delete ActorCommand : {}", id);

        actorCommandRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
