package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Categories;
import com.mycompany.myapp.repository.CategoriesRepository;
import com.mycompany.myapp.service.dto.CategoriesDTO;
import com.mycompany.myapp.service.mapper.CategoriesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Categories}.
 */
@Service
@Transactional
public class CategoriesService {

    private final Logger log = LoggerFactory.getLogger(CategoriesService.class);

    private final CategoriesRepository categoriesRepository;

    private final CategoriesMapper categoriesMapper;

    public CategoriesService(CategoriesRepository categoriesRepository, CategoriesMapper categoriesMapper) {
        this.categoriesRepository = categoriesRepository;
        this.categoriesMapper = categoriesMapper;
    }

    /**
     * Save a categories.
     *
     * @param categoriesDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoriesDTO save(CategoriesDTO categoriesDTO) {
        log.debug("Request to save Categories : {}", categoriesDTO);
        Categories categories = categoriesMapper.toEntity(categoriesDTO);
        categories = categoriesRepository.save(categories);
        return categoriesMapper.toDto(categories);
    }

    /**
     * Get all the categories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CategoriesDTO> findAll() {
        log.debug("Request to get all Categories");
        return categoriesRepository.findAll().stream()
            .map(categoriesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one categories by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoriesDTO> findOne(Long id) {
        log.debug("Request to get Categories : {}", id);
        return categoriesRepository.findById(id)
            .map(categoriesMapper::toDto);
    }

    /**
     * Delete the categories by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Categories : {}", id);
        categoriesRepository.deleteById(id);
    }
}
