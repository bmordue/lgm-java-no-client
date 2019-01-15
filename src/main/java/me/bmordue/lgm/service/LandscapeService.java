package me.bmordue.lgm.service;

import me.bmordue.lgm.domain.Landscape;
import me.bmordue.lgm.repository.LandscapeRepository;
import me.bmordue.lgm.service.dto.LandscapeDTO;
import me.bmordue.lgm.service.mapper.LandscapeMapper;
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
 * Service Implementation for managing Landscape.
 */
@Service
@Transactional
public class LandscapeService {

    private final Logger log = LoggerFactory.getLogger(LandscapeService.class);

    private final LandscapeRepository landscapeRepository;

    private final LandscapeMapper landscapeMapper;

    public LandscapeService(LandscapeRepository landscapeRepository, LandscapeMapper landscapeMapper) {
        this.landscapeRepository = landscapeRepository;
        this.landscapeMapper = landscapeMapper;
    }

    /**
     * Save a landscape.
     *
     * @param landscapeDTO the entity to save
     * @return the persisted entity
     */
    public LandscapeDTO save(LandscapeDTO landscapeDTO) {
        log.debug("Request to save Landscape : {}", landscapeDTO);

        Landscape landscape = landscapeMapper.toEntity(landscapeDTO);
        landscape = landscapeRepository.save(landscape);
        return landscapeMapper.toDto(landscape);
    }

    /**
     * Get all the landscapes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LandscapeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Landscapes");
        return landscapeRepository.findAll(pageable)
            .map(landscapeMapper::toDto);
    }



    /**
     *  get all the landscapes where Turn is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<LandscapeDTO> findAllWhereTurnIsNull() {
        log.debug("Request to get all landscapes where Turn is null");
        return StreamSupport
            .stream(landscapeRepository.findAll().spliterator(), false)
            .filter(landscape -> landscape.getTurn() == null)
            .map(landscapeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one landscape by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<LandscapeDTO> findOne(Long id) {
        log.debug("Request to get Landscape : {}", id);
        return landscapeRepository.findById(id)
            .map(landscapeMapper::toDto);
    }

    /**
     * Delete the landscape by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Landscape : {}", id);
        landscapeRepository.deleteById(id);
    }
}
