package me.bmordue.lgm.service;

import me.bmordue.lgm.domain.GameTurn;
import me.bmordue.lgm.repository.GameTurnRepository;
import me.bmordue.lgm.service.dto.GameTurnDTO;
import me.bmordue.lgm.service.mapper.GameTurnMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing GameTurn.
 */
@Service
@Transactional
public class GameTurnService {

    private final Logger log = LoggerFactory.getLogger(GameTurnService.class);

    private final GameTurnRepository gameTurnRepository;

    private final GameTurnMapper gameTurnMapper;

    public GameTurnService(GameTurnRepository gameTurnRepository, GameTurnMapper gameTurnMapper) {
        this.gameTurnRepository = gameTurnRepository;
        this.gameTurnMapper = gameTurnMapper;
    }

    /**
     * Save a gameTurn.
     *
     * @param gameTurnDTO the entity to save
     * @return the persisted entity
     */
    public GameTurnDTO save(GameTurnDTO gameTurnDTO) {
        log.debug("Request to save GameTurn : {}", gameTurnDTO);

        GameTurn gameTurn = gameTurnMapper.toEntity(gameTurnDTO);
        gameTurn = gameTurnRepository.save(gameTurn);
        return gameTurnMapper.toDto(gameTurn);
    }

    /**
     * Get all the gameTurns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<GameTurnDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GameTurns");
        return gameTurnRepository.findAll(pageable)
            .map(gameTurnMapper::toDto);
    }


    /**
     * Get one gameTurn by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<GameTurnDTO> findOne(Long id) {
        log.debug("Request to get GameTurn : {}", id);
        return gameTurnRepository.findById(id)
            .map(gameTurnMapper::toDto);
    }

    /**
     * Delete the gameTurn by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete GameTurn : {}", id);
        gameTurnRepository.deleteById(id);
    }
}
