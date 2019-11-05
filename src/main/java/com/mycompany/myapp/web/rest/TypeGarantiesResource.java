package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.TypeGarantiesService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.TypeGarantiesDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.TypeGaranties}.
 */
@RestController
@RequestMapping("/api")
public class TypeGarantiesResource {

    private final Logger log = LoggerFactory.getLogger(TypeGarantiesResource.class);

    private static final String ENTITY_NAME = "typeGaranties";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeGarantiesService typeGarantiesService;

    public TypeGarantiesResource(TypeGarantiesService typeGarantiesService) {
        this.typeGarantiesService = typeGarantiesService;
    }

    /**
     * {@code POST  /type-garanties} : Create a new typeGaranties.
     *
     * @param typeGarantiesDTO the typeGarantiesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeGarantiesDTO, or with status {@code 400 (Bad Request)} if the typeGaranties has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-garanties")
    public ResponseEntity<TypeGarantiesDTO> createTypeGaranties(@RequestBody TypeGarantiesDTO typeGarantiesDTO) throws URISyntaxException {
        log.debug("REST request to save TypeGaranties : {}", typeGarantiesDTO);
        if (typeGarantiesDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeGaranties cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeGarantiesDTO result = typeGarantiesService.save(typeGarantiesDTO);
        return ResponseEntity.created(new URI("/api/type-garanties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-garanties} : Updates an existing typeGaranties.
     *
     * @param typeGarantiesDTO the typeGarantiesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeGarantiesDTO,
     * or with status {@code 400 (Bad Request)} if the typeGarantiesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeGarantiesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-garanties")
    public ResponseEntity<TypeGarantiesDTO> updateTypeGaranties(@RequestBody TypeGarantiesDTO typeGarantiesDTO) throws URISyntaxException {
        log.debug("REST request to update TypeGaranties : {}", typeGarantiesDTO);
        if (typeGarantiesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeGarantiesDTO result = typeGarantiesService.save(typeGarantiesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeGarantiesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /type-garanties} : get all the typeGaranties.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeGaranties in body.
     */
    @GetMapping("/type-garanties")
    public List<TypeGarantiesDTO> getAllTypeGaranties() {
        log.debug("REST request to get all TypeGaranties");
        return typeGarantiesService.findAll();
    }

    /**
     * {@code GET  /type-garanties/:id} : get the "id" typeGaranties.
     *
     * @param id the id of the typeGarantiesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeGarantiesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-garanties/{id}")
    public ResponseEntity<TypeGarantiesDTO> getTypeGaranties(@PathVariable Long id) {
        log.debug("REST request to get TypeGaranties : {}", id);
        Optional<TypeGarantiesDTO> typeGarantiesDTO = typeGarantiesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeGarantiesDTO);
    }

    /**
     * {@code DELETE  /type-garanties/:id} : delete the "id" typeGaranties.
     *
     * @param id the id of the typeGarantiesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-garanties/{id}")
    public ResponseEntity<Void> deleteTypeGaranties(@PathVariable Long id) {
        log.debug("REST request to delete TypeGaranties : {}", id);
        typeGarantiesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
