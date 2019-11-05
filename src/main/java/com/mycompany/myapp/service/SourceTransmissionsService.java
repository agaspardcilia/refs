package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SourceTransmissions;
import com.mycompany.myapp.repository.SourceTransmissionsRepository;
import com.mycompany.myapp.service.dto.SourceTransmissionsDTO;
import com.mycompany.myapp.service.mapper.SourceTransmissionsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SourceTransmissions}.
 */
@Service
@Transactional
public class SourceTransmissionsService {

    private final Logger log = LoggerFactory.getLogger(SourceTransmissionsService.class);

    private final SourceTransmissionsRepository sourceTransmissionsRepository;

    private final SourceTransmissionsMapper sourceTransmissionsMapper;

    public SourceTransmissionsService(SourceTransmissionsRepository sourceTransmissionsRepository, SourceTransmissionsMapper sourceTransmissionsMapper) {
        this.sourceTransmissionsRepository = sourceTransmissionsRepository;
        this.sourceTransmissionsMapper = sourceTransmissionsMapper;
    }

    /**
     * Save a sourceTransmissions.
     *
     * @param sourceTransmissionsDTO the entity to save.
     * @return the persisted entity.
     */
    public SourceTransmissionsDTO save(SourceTransmissionsDTO sourceTransmissionsDTO) {
        log.debug("Request to save SourceTransmissions : {}", sourceTransmissionsDTO);
        SourceTransmissions sourceTransmissions = sourceTransmissionsMapper.toEntity(sourceTransmissionsDTO);
        sourceTransmissions = sourceTransmissionsRepository.save(sourceTransmissions);
        return sourceTransmissionsMapper.toDto(sourceTransmissions);
    }

    /**
     * Get all the sourceTransmissions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SourceTransmissionsDTO> findAll() {
        log.debug("Request to get all SourceTransmissions");
        return sourceTransmissionsRepository.findAll().stream()
            .map(sourceTransmissionsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one sourceTransmissions by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SourceTransmissionsDTO> findOne(Long id) {
        log.debug("Request to get SourceTransmissions : {}", id);
        return sourceTransmissionsRepository.findById(id)
            .map(sourceTransmissionsMapper::toDto);
    }

    /**
     * Delete the sourceTransmissions by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SourceTransmissions : {}", id);
        sourceTransmissionsRepository.deleteById(id);
    }
}
