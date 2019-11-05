package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TypeGaranties;
import com.mycompany.myapp.repository.TypeGarantiesRepository;
import com.mycompany.myapp.service.dto.TypeGarantiesDTO;
import com.mycompany.myapp.service.mapper.TypeGarantiesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link TypeGaranties}.
 */
@Service
@Transactional
public class TypeGarantiesService {

    private final Logger log = LoggerFactory.getLogger(TypeGarantiesService.class);

    private final TypeGarantiesRepository typeGarantiesRepository;

    private final TypeGarantiesMapper typeGarantiesMapper;

    public TypeGarantiesService(TypeGarantiesRepository typeGarantiesRepository, TypeGarantiesMapper typeGarantiesMapper) {
        this.typeGarantiesRepository = typeGarantiesRepository;
        this.typeGarantiesMapper = typeGarantiesMapper;
    }

    /**
     * Save a typeGaranties.
     *
     * @param typeGarantiesDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeGarantiesDTO save(TypeGarantiesDTO typeGarantiesDTO) {
        log.debug("Request to save TypeGaranties : {}", typeGarantiesDTO);
        TypeGaranties typeGaranties = typeGarantiesMapper.toEntity(typeGarantiesDTO);
        typeGaranties = typeGarantiesRepository.save(typeGaranties);
        return typeGarantiesMapper.toDto(typeGaranties);
    }

    /**
     * Get all the typeGaranties.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TypeGarantiesDTO> findAll() {
        log.debug("Request to get all TypeGaranties");
        return typeGarantiesRepository.findAll().stream()
            .map(typeGarantiesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one typeGaranties by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeGarantiesDTO> findOne(Long id) {
        log.debug("Request to get TypeGaranties : {}", id);
        return typeGarantiesRepository.findById(id)
            .map(typeGarantiesMapper::toDto);
    }

    /**
     * Delete the typeGaranties by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeGaranties : {}", id);
        typeGarantiesRepository.deleteById(id);
    }
}
