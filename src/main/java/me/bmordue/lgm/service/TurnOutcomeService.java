package me.bmordue.lgm.service;

import me.bmordue.lgm.domain.TurnOutcome;
import me.bmordue.lgm.repository.TurnOutcomeRepository;
import me.bmordue.lgm.service.dto.TurnOutcomeDTO;
import me.bmordue.lgm.service.mapper.TurnOutcomeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing TurnOutcome.
 */
@Service
@Transactional
public class TurnOutcomeService {

    private final Logger log = LoggerFactory.getLogger(TurnOutcomeService.class);

    private final TurnOutcomeRepository turnOutcomeRepository;

    private final TurnOutcomeMapper turnOutcomeMapper;

    public TurnOutcomeService(TurnOutcomeRepository turnOutcomeRepository, TurnOutcomeMapper turnOutcomeMapper) {
        this.turnOutcomeRepository = turnOutcomeRepository;
        this.turnOutcomeMapper = turnOutcomeMapper;
    }

    /**
     * Save a turnOutcome.
     *
     * @param turnOutcomeDTO the entity to save
     * @return the persisted entity
     */
    public TurnOutcomeDTO save(TurnOutcomeDTO turnOutcomeDTO) {
        log.debug("Request to save TurnOutcome : {}", turnOutcomeDTO);

        TurnOutcome turnOutcome = turnOutcomeMapper.toEntity(turnOutcomeDTO);
        turnOutcome = turnOutcomeRepository.save(turnOutcome);
        return turnOutcomeMapper.toDto(turnOutcome);
    }

    /**
     * Get all the turnOutcomes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TurnOutcomeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TurnOutcomes");
        return turnOutcomeRepository.findAll(pageable)
            .map(turnOutcomeMapper::toDto);
    }



    /**
     *  get all the turnOutcomes where Turn is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<TurnOutcomeDTO> findAllWhereTurnIsNull() {
        log.debug("Request to get all turnOutcomes where Turn is null");
        return StreamSupport
            .stream(turnOutcomeRepository.findAll().spliterator(), false)
            .filter(turnOutcome -> turnOutcome.getTurn() == null)
            .map(turnOutcomeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one turnOutcome by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TurnOutcomeDTO> findOne(Long id) {
        log.debug("Request to get TurnOutcome : {}", id);
        return turnOutcomeRepository.findById(id)
            .map(turnOutcomeMapper::toDto);
    }

    /**
     * Delete the turnOutcome by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TurnOutcome : {}", id);
        turnOutcomeRepository.deleteById(id);
    }
}
