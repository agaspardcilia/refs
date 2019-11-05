package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ReclamantTypes;
import com.mycompany.myapp.repository.ReclamantTypesRepository;
import com.mycompany.myapp.service.dto.ReclamantTypesDTO;
import com.mycompany.myapp.service.mapper.ReclamantTypesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ReclamantTypes}.
 */
@Service
@Transactional
public class ReclamantTypesService {

    private final Logger log = LoggerFactory.getLogger(ReclamantTypesService.class);

    private final ReclamantTypesRepository reclamantTypesRepository;

    private final ReclamantTypesMapper reclamantTypesMapper;

    public ReclamantTypesService(ReclamantTypesRepository reclamantTypesRepository, ReclamantTypesMapper reclamantTypesMapper) {
        this.reclamantTypesRepository = reclamantTypesRepository;
        this.reclamantTypesMapper = reclamantTypesMapper;
    }

    /**
     * Save a reclamantTypes.
     *
     * @param reclamantTypesDTO the entity to save.
     * @return the persisted entity.
     */
    public ReclamantTypesDTO save(ReclamantTypesDTO reclamantTypesDTO) {
        log.debug("Request to save ReclamantTypes : {}", reclamantTypesDTO);
        ReclamantTypes reclamantTypes = reclamantTypesMapper.toEntity(reclamantTypesDTO);
        reclamantTypes = reclamantTypesRepository.save(reclamantTypes);
        return reclamantTypesMapper.toDto(reclamantTypes);
    }

    /**
     * Get all the reclamantTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReclamantTypesDTO> findAll() {
        log.debug("Request to get all ReclamantTypes");
        return reclamantTypesRepository.findAll().stream()
            .map(reclamantTypesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one reclamantTypes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReclamantTypesDTO> findOne(Long id) {
        log.debug("Request to get ReclamantTypes : {}", id);
        return reclamantTypesRepository.findById(id)
            .map(reclamantTypesMapper::toDto);
    }

    /**
     * Delete the reclamantTypes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ReclamantTypes : {}", id);
        reclamantTypesRepository.deleteById(id);
    }
}
