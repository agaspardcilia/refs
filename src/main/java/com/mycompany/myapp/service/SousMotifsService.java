package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SousMotifs;
import com.mycompany.myapp.repository.SousMotifsRepository;
import com.mycompany.myapp.service.dto.SousMotifsDTO;
import com.mycompany.myapp.service.mapper.SousMotifsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SousMotifs}.
 */
@Service
@Transactional
public class SousMotifsService {

    private final Logger log = LoggerFactory.getLogger(SousMotifsService.class);

    private final SousMotifsRepository sousMotifsRepository;

    private final SousMotifsMapper sousMotifsMapper;

    public SousMotifsService(SousMotifsRepository sousMotifsRepository, SousMotifsMapper sousMotifsMapper) {
        this.sousMotifsRepository = sousMotifsRepository;
        this.sousMotifsMapper = sousMotifsMapper;
    }

    /**
     * Save a sousMotifs.
     *
     * @param sousMotifsDTO the entity to save.
     * @return the persisted entity.
     */
    public SousMotifsDTO save(SousMotifsDTO sousMotifsDTO) {
        log.debug("Request to save SousMotifs : {}", sousMotifsDTO);
        SousMotifs sousMotifs = sousMotifsMapper.toEntity(sousMotifsDTO);
        sousMotifs = sousMotifsRepository.save(sousMotifs);
        return sousMotifsMapper.toDto(sousMotifs);
    }

    /**
     * Get all the sousMotifs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SousMotifsDTO> findAll() {
        log.debug("Request to get all SousMotifs");
        return sousMotifsRepository.findAll().stream()
            .map(sousMotifsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one sousMotifs by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SousMotifsDTO> findOne(Long id) {
        log.debug("Request to get SousMotifs : {}", id);
        return sousMotifsRepository.findById(id)
            .map(sousMotifsMapper::toDto);
    }

    /**
     * Delete the sousMotifs by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SousMotifs : {}", id);
        sousMotifsRepository.deleteById(id);
    }
}
