package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TypeClients;
import com.mycompany.myapp.repository.TypeClientsRepository;
import com.mycompany.myapp.service.dto.TypeClientsDTO;
import com.mycompany.myapp.service.mapper.TypeClientsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link TypeClients}.
 */
@Service
@Transactional
public class TypeClientsService {

    private final Logger log = LoggerFactory.getLogger(TypeClientsService.class);

    private final TypeClientsRepository typeClientsRepository;

    private final TypeClientsMapper typeClientsMapper;

    public TypeClientsService(TypeClientsRepository typeClientsRepository, TypeClientsMapper typeClientsMapper) {
        this.typeClientsRepository = typeClientsRepository;
        this.typeClientsMapper = typeClientsMapper;
    }

    /**
     * Save a typeClients.
     *
     * @param typeClientsDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeClientsDTO save(TypeClientsDTO typeClientsDTO) {
        log.debug("Request to save TypeClients : {}", typeClientsDTO);
        TypeClients typeClients = typeClientsMapper.toEntity(typeClientsDTO);
        typeClients = typeClientsRepository.save(typeClients);
        return typeClientsMapper.toDto(typeClients);
    }

    /**
     * Get all the typeClients.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TypeClientsDTO> findAll() {
        log.debug("Request to get all TypeClients");
        return typeClientsRepository.findAll().stream()
            .map(typeClientsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one typeClients by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeClientsDTO> findOne(Long id) {
        log.debug("Request to get TypeClients : {}", id);
        return typeClientsRepository.findById(id)
            .map(typeClientsMapper::toDto);
    }

    /**
     * Delete the typeClients by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeClients : {}", id);
        typeClientsRepository.deleteById(id);
    }
}
