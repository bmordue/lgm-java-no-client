package me.bmordue.lgm.web.rest;

import com.codahale.metrics.annotation.Timed;
import me.bmordue.lgm.domain.Landscape;
import me.bmordue.lgm.repository.LandscapeRepository;
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
 * REST controller for managing Landscape.
 */
@RestController
@RequestMapping("/api")
public class LandscapeResource {

    private final Logger log = LoggerFactory.getLogger(LandscapeResource.class);

    private static final String ENTITY_NAME = "landscape";

    private final LandscapeRepository landscapeRepository;

    public LandscapeResource(LandscapeRepository landscapeRepository) {
        this.landscapeRepository = landscapeRepository;
    }

    /**
     * POST  /landscapes : Create a new landscape.
     *
     * @param landscape the landscape to create
     * @return the ResponseEntity with status 201 (Created) and with body the new landscape, or with status 400 (Bad Request) if the landscape has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/landscapes")
    @Timed
    public ResponseEntity<Landscape> createLandscape(@Valid @RequestBody Landscape landscape) throws URISyntaxException {
        log.debug("REST request to save Landscape : {}", landscape);
        if (landscape.getId() != null) {
            throw new BadRequestAlertException("A new landscape cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Landscape result = landscapeRepository.save(landscape);
        return ResponseEntity.created(new URI("/api/landscapes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /landscapes : Updates an existing landscape.
     *
     * @param landscape the landscape to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated landscape,
     * or with status 400 (Bad Request) if the landscape is not valid,
     * or with status 500 (Internal Server Error) if the landscape couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/landscapes")
    @Timed
    public ResponseEntity<Landscape> updateLandscape(@Valid @RequestBody Landscape landscape) throws URISyntaxException {
        log.debug("REST request to update Landscape : {}", landscape);
        if (landscape.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Landscape result = landscapeRepository.save(landscape);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, landscape.getId().toString()))
            .body(result);
    }

    /**
     * GET  /landscapes : get all the landscapes.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of landscapes in body
     */
    @GetMapping("/landscapes")
    @Timed
    public ResponseEntity<List<Landscape>> getAllLandscapes(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("turn-is-null".equals(filter)) {
            log.debug("REST request to get all Landscapes where turn is null");
            return new ResponseEntity<>(StreamSupport
                .stream(landscapeRepository.findAll().spliterator(), false)
                .filter(landscape -> landscape.getTurn() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Landscapes");
        Page<Landscape> page = landscapeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/landscapes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /landscapes/:id : get the "id" landscape.
     *
     * @param id the id of the landscape to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the landscape, or with status 404 (Not Found)
     */
    @GetMapping("/landscapes/{id}")
    @Timed
    public ResponseEntity<Landscape> getLandscape(@PathVariable Long id) {
        log.debug("REST request to get Landscape : {}", id);
        Optional<Landscape> landscape = landscapeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(landscape);
    }

    /**
     * DELETE  /landscapes/:id : delete the "id" landscape.
     *
     * @param id the id of the landscape to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/landscapes/{id}")
    @Timed
    public ResponseEntity<Void> deleteLandscape(@PathVariable Long id) {
        log.debug("REST request to delete Landscape : {}", id);

        landscapeRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
