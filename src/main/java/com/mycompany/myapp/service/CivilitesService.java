package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Civilites;
import com.mycompany.myapp.repository.CivilitesRepository;
import com.mycompany.myapp.service.dto.CivilitesDTO;
import com.mycompany.myapp.service.mapper.CivilitesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Civilites}.
 */
@Service
@Transactional
public class CivilitesService {

    private final Logger log = LoggerFactory.getLogger(CivilitesService.class);

    private final CivilitesRepository civilitesRepository;

    private final CivilitesMapper civilitesMapper;

    public CivilitesService(CivilitesRepository civilitesRepository, CivilitesMapper civilitesMapper) {
        this.civilitesRepository = civilitesRepository;
        this.civilitesMapper = civilitesMapper;
    }

    /**
     * Save a civilites.
     *
     * @param civilitesDTO the entity to save.
     * @return the persisted entity.
     */
    public CivilitesDTO save(CivilitesDTO civilitesDTO) {
        log.debug("Request to save Civilites : {}", civilitesDTO);
        Civilites civilites = civilitesMapper.toEntity(civilitesDTO);
        civilites = civilitesRepository.save(civilites);
        return civilitesMapper.toDto(civilites);
    }

    /**
     * Get all the civilites.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CivilitesDTO> findAll() {
        log.debug("Request to get all Civilites");
        return civilitesRepository.findAll().stream()
            .map(civilitesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one civilites by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CivilitesDTO> findOne(Long id) {
        log.debug("Request to get Civilites : {}", id);
        return civilitesRepository.findById(id)
            .map(civilitesMapper::toDto);
    }

    /**
     * Delete the civilites by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Civilites : {}", id);
        civilitesRepository.deleteById(id);
    }
}
