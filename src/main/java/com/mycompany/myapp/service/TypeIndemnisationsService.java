package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TypeIndemnisations;
import com.mycompany.myapp.repository.TypeIndemnisationsRepository;
import com.mycompany.myapp.service.dto.TypeIndemnisationsDTO;
import com.mycompany.myapp.service.mapper.TypeIndemnisationsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link TypeIndemnisations}.
 */
@Service
@Transactional
public class TypeIndemnisationsService {

    private final Logger log = LoggerFactory.getLogger(TypeIndemnisationsService.class);

    private final TypeIndemnisationsRepository typeIndemnisationsRepository;

    private final TypeIndemnisationsMapper typeIndemnisationsMapper;

    public TypeIndemnisationsService(TypeIndemnisationsRepository typeIndemnisationsRepository, TypeIndemnisationsMapper typeIndemnisationsMapper) {
        this.typeIndemnisationsRepository = typeIndemnisationsRepository;
        this.typeIndemnisationsMapper = typeIndemnisationsMapper;
    }

    /**
     * Save a typeIndemnisations.
     *
     * @param typeIndemnisationsDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeIndemnisationsDTO save(TypeIndemnisationsDTO typeIndemnisationsDTO) {
        log.debug("Request to save TypeIndemnisations : {}", typeIndemnisationsDTO);
        TypeIndemnisations typeIndemnisations = typeIndemnisationsMapper.toEntity(typeIndemnisationsDTO);
        typeIndemnisations = typeIndemnisationsRepository.save(typeIndemnisations);
        return typeIndemnisationsMapper.toDto(typeIndemnisations);
    }

    /**
     * Get all the typeIndemnisations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TypeIndemnisationsDTO> findAll() {
        log.debug("Request to get all TypeIndemnisations");
        return typeIndemnisationsRepository.findAll().stream()
            .map(typeIndemnisationsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one typeIndemnisations by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeIndemnisationsDTO> findOne(Long id) {
        log.debug("Request to get TypeIndemnisations : {}", id);
        return typeIndemnisationsRepository.findById(id)
            .map(typeIndemnisationsMapper::toDto);
    }

    /**
     * Delete the typeIndemnisations by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeIndemnisations : {}", id);
        typeIndemnisationsRepository.deleteById(id);
    }
}
