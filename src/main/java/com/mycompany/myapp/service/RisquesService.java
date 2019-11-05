package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Risques;
import com.mycompany.myapp.repository.RisquesRepository;
import com.mycompany.myapp.service.dto.RisquesDTO;
import com.mycompany.myapp.service.mapper.RisquesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Risques}.
 */
@Service
@Transactional
public class RisquesService {

    private final Logger log = LoggerFactory.getLogger(RisquesService.class);

    private final RisquesRepository risquesRepository;

    private final RisquesMapper risquesMapper;

    public RisquesService(RisquesRepository risquesRepository, RisquesMapper risquesMapper) {
        this.risquesRepository = risquesRepository;
        this.risquesMapper = risquesMapper;
    }

    /**
     * Save a risques.
     *
     * @param risquesDTO the entity to save.
     * @return the persisted entity.
     */
    public RisquesDTO save(RisquesDTO risquesDTO) {
        log.debug("Request to save Risques : {}", risquesDTO);
        Risques risques = risquesMapper.toEntity(risquesDTO);
        risques = risquesRepository.save(risques);
        return risquesMapper.toDto(risques);
    }

    /**
     * Get all the risques.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RisquesDTO> findAll() {
        log.debug("Request to get all Risques");
        return risquesRepository.findAll().stream()
            .map(risquesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one risques by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RisquesDTO> findOne(Long id) {
        log.debug("Request to get Risques : {}", id);
        return risquesRepository.findById(id)
            .map(risquesMapper::toDto);
    }

    /**
     * Delete the risques by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Risques : {}", id);
        risquesRepository.deleteById(id);
    }
}
