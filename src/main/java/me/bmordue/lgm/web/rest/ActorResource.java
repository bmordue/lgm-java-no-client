package me.bmordue.lgm.web.rest;

import com.codahale.metrics.annotation.Timed;
import me.bmordue.lgm.domain.Actor;
import me.bmordue.lgm.repository.ActorRepository;
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
 * REST controller for managing Actor.
 */
@RestController
@RequestMapping("/api")
public class ActorResource {

    private final Logger log = LoggerFactory.getLogger(ActorResource.class);

    private static final String ENTITY_NAME = "actor";

    private final ActorRepository actorRepository;

    public ActorResource(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    /**
     * POST  /actors : Create a new actor.
     *
     * @param actor the actor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new actor, or with status 400 (Bad Request) if the actor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/actors")
    @Timed
    public ResponseEntity<Actor> createActor(@Valid @RequestBody Actor actor) throws URISyntaxException {
        log.debug("REST request to save Actor : {}", actor);
        if (actor.getId() != null) {
            throw new BadRequestAlertException("A new actor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Actor result = actorRepository.save(actor);
        return ResponseEntity.created(new URI("/api/actors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /actors : Updates an existing actor.
     *
     * @param actor the actor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated actor,
     * or with status 400 (Bad Request) if the actor is not valid,
     * or with status 500 (Internal Server Error) if the actor couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/actors")
    @Timed
    public ResponseEntity<Actor> updateActor(@Valid @RequestBody Actor actor) throws URISyntaxException {
        log.debug("REST request to update Actor : {}", actor);
        if (actor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Actor result = actorRepository.save(actor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, actor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /actors : get all the actors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of actors in body
     */
    @GetMapping("/actors")
    @Timed
    public ResponseEntity<List<Actor>> getAllActors(Pageable pageable) {
        log.debug("REST request to get a page of Actors");
        Page<Actor> page = actorRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/actors");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /actors/:id : get the "id" actor.
     *
     * @param id the id of the actor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the actor, or with status 404 (Not Found)
     */
    @GetMapping("/actors/{id}")
    @Timed
    public ResponseEntity<Actor> getActor(@PathVariable Long id) {
        log.debug("REST request to get Actor : {}", id);
        Optional<Actor> actor = actorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(actor);
    }

    /**
     * DELETE  /actors/:id : delete the "id" actor.
     *
     * @param id the id of the actor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/actors/{id}")
    @Timed
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        log.debug("REST request to delete Actor : {}", id);

        actorRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
