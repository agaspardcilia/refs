package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Motifs;
import com.mycompany.myapp.repository.MotifsRepository;
import com.mycompany.myapp.service.dto.MotifsDTO;
import com.mycompany.myapp.service.mapper.MotifsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Motifs}.
 */
@Service
@Transactional
public class MotifsService {

    private final Logger log = LoggerFactory.getLogger(MotifsService.class);

    private final MotifsRepository motifsRepository;

    private final MotifsMapper motifsMapper;

    public MotifsService(MotifsRepository motifsRepository, MotifsMapper motifsMapper) {
        this.motifsRepository = motifsRepository;
        this.motifsMapper = motifsMapper;
    }

    /**
     * Save a motifs.
     *
     * @param motifsDTO the entity to save.
     * @return the persisted entity.
     */
    public MotifsDTO save(MotifsDTO motifsDTO) {
        log.debug("Request to save Motifs : {}", motifsDTO);
        Motifs motifs = motifsMapper.toEntity(motifsDTO);
        motifs = motifsRepository.save(motifs);
        return motifsMapper.toDto(motifs);
    }

    /**
     * Get all the motifs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MotifsDTO> findAll() {
        log.debug("Request to get all Motifs");
        return motifsRepository.findAll().stream()
            .map(motifsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one motifs by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MotifsDTO> findOne(Long id) {
        log.debug("Request to get Motifs : {}", id);
        return motifsRepository.findById(id)
            .map(motifsMapper::toDto);
    }

    /**
     * Delete the motifs by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Motifs : {}", id);
        motifsRepository.deleteById(id);
    }
}
