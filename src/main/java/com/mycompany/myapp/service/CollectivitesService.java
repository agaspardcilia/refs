package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Collectivites;
import com.mycompany.myapp.repository.CollectivitesRepository;
import com.mycompany.myapp.service.dto.CollectivitesDTO;
import com.mycompany.myapp.service.mapper.CollectivitesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Collectivites}.
 */
@Service
@Transactional
public class CollectivitesService {

    private final Logger log = LoggerFactory.getLogger(CollectivitesService.class);

    private final CollectivitesRepository collectivitesRepository;

    private final CollectivitesMapper collectivitesMapper;

    public CollectivitesService(CollectivitesRepository collectivitesRepository, CollectivitesMapper collectivitesMapper) {
        this.collectivitesRepository = collectivitesRepository;
        this.collectivitesMapper = collectivitesMapper;
    }

    /**
     * Save a collectivites.
     *
     * @param collectivitesDTO the entity to save.
     * @return the persisted entity.
     */
    public CollectivitesDTO save(CollectivitesDTO collectivitesDTO) {
        log.debug("Request to save Collectivites : {}", collectivitesDTO);
        Collectivites collectivites = collectivitesMapper.toEntity(collectivitesDTO);
        collectivites = collectivitesRepository.save(collectivites);
        return collectivitesMapper.toDto(collectivites);
    }

    /**
     * Get all the collectivites.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CollectivitesDTO> findAll() {
        log.debug("Request to get all Collectivites");
        return collectivitesRepository.findAll().stream()
            .map(collectivitesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one collectivites by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CollectivitesDTO> findOne(Long id) {
        log.debug("Request to get Collectivites : {}", id);
        return collectivitesRepository.findById(id)
            .map(collectivitesMapper::toDto);
    }

    /**
     * Delete the collectivites by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Collectivites : {}", id);
        collectivitesRepository.deleteById(id);
    }
}
