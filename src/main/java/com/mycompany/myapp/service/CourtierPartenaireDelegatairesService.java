package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CourtierPartenaireDelegataires;
import com.mycompany.myapp.repository.CourtierPartenaireDelegatairesRepository;
import com.mycompany.myapp.service.dto.CourtierPartenaireDelegatairesDTO;
import com.mycompany.myapp.service.mapper.CourtierPartenaireDelegatairesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CourtierPartenaireDelegataires}.
 */
@Service
@Transactional
public class CourtierPartenaireDelegatairesService {

    private final Logger log = LoggerFactory.getLogger(CourtierPartenaireDelegatairesService.class);

    private final CourtierPartenaireDelegatairesRepository courtierPartenaireDelegatairesRepository;

    private final CourtierPartenaireDelegatairesMapper courtierPartenaireDelegatairesMapper;

    public CourtierPartenaireDelegatairesService(CourtierPartenaireDelegatairesRepository courtierPartenaireDelegatairesRepository, CourtierPartenaireDelegatairesMapper courtierPartenaireDelegatairesMapper) {
        this.courtierPartenaireDelegatairesRepository = courtierPartenaireDelegatairesRepository;
        this.courtierPartenaireDelegatairesMapper = courtierPartenaireDelegatairesMapper;
    }

    /**
     * Save a courtierPartenaireDelegataires.
     *
     * @param courtierPartenaireDelegatairesDTO the entity to save.
     * @return the persisted entity.
     */
    public CourtierPartenaireDelegatairesDTO save(CourtierPartenaireDelegatairesDTO courtierPartenaireDelegatairesDTO) {
        log.debug("Request to save CourtierPartenaireDelegataires : {}", courtierPartenaireDelegatairesDTO);
        CourtierPartenaireDelegataires courtierPartenaireDelegataires = courtierPartenaireDelegatairesMapper.toEntity(courtierPartenaireDelegatairesDTO);
        courtierPartenaireDelegataires = courtierPartenaireDelegatairesRepository.save(courtierPartenaireDelegataires);
        return courtierPartenaireDelegatairesMapper.toDto(courtierPartenaireDelegataires);
    }

    /**
     * Get all the courtierPartenaireDelegataires.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CourtierPartenaireDelegatairesDTO> findAll() {
        log.debug("Request to get all CourtierPartenaireDelegataires");
        return courtierPartenaireDelegatairesRepository.findAll().stream()
            .map(courtierPartenaireDelegatairesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one courtierPartenaireDelegataires by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CourtierPartenaireDelegatairesDTO> findOne(Long id) {
        log.debug("Request to get CourtierPartenaireDelegataires : {}", id);
        return courtierPartenaireDelegatairesRepository.findById(id)
            .map(courtierPartenaireDelegatairesMapper::toDto);
    }

    /**
     * Delete the courtierPartenaireDelegataires by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CourtierPartenaireDelegataires : {}", id);
        courtierPartenaireDelegatairesRepository.deleteById(id);
    }
}
