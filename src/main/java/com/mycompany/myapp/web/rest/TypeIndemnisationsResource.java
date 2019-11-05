package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.TypeIndemnisationsService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.TypeIndemnisationsDTO;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.TypeIndemnisations}.
 */
@RestController
@RequestMapping("/api")
public class TypeIndemnisationsResource {

    private final Logger log = LoggerFactory.getLogger(TypeIndemnisationsResource.class);

    private static final String ENTITY_NAME = "typeIndemnisations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeIndemnisationsService typeIndemnisationsService;

    public TypeIndemnisationsResource(TypeIndemnisationsService typeIndemnisationsService) {
        this.typeIndemnisationsService = typeIndemnisationsService;
    }

    /**
     * {@code POST  /type-indemnisations} : Create a new typeIndemnisations.
     *
     * @param typeIndemnisationsDTO the typeIndemnisationsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeIndemnisationsDTO, or with status {@code 400 (Bad Request)} if the typeIndemnisations has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-indemnisations")
    public ResponseEntity<TypeIndemnisationsDTO> createTypeIndemnisations(@RequestBody TypeIndemnisationsDTO typeIndemnisationsDTO) throws URISyntaxException {
        log.debug("REST request to save TypeIndemnisations : {}", typeIndemnisationsDTO);
        if (typeIndemnisationsDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeIndemnisations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeIndemnisationsDTO result = typeIndemnisationsService.save(typeIndemnisationsDTO);
        return ResponseEntity.created(new URI("/api/type-indemnisations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-indemnisations} : Updates an existing typeIndemnisations.
     *
     * @param typeIndemnisationsDTO the typeIndemnisationsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeIndemnisationsDTO,
     * or with status {@code 400 (Bad Request)} if the typeIndemnisationsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeIndemnisationsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-indemnisations")
    public ResponseEntity<TypeIndemnisationsDTO> updateTypeIndemnisations(@RequestBody TypeIndemnisationsDTO typeIndemnisationsDTO) throws URISyntaxException {
        log.debug("REST request to update TypeIndemnisations : {}", typeIndemnisationsDTO);
        if (typeIndemnisationsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeIndemnisationsDTO result = typeIndemnisationsService.save(typeIndemnisationsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeIndemnisationsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /type-indemnisations} : get all the typeIndemnisations.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeIndemnisations in body.
     */
    @GetMapping("/type-indemnisations")
    public List<TypeIndemnisationsDTO> getAllTypeIndemnisations() {
        log.debug("REST request to get all TypeIndemnisations");
        return typeIndemnisationsService.findAll();
    }

    /**
     * {@code GET  /type-indemnisations/:id} : get the "id" typeIndemnisations.
     *
     * @param id the id of the typeIndemnisationsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeIndemnisationsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-indemnisations/{id}")
    public ResponseEntity<TypeIndemnisationsDTO> getTypeIndemnisations(@PathVariable Long id) {
        log.debug("REST request to get TypeIndemnisations : {}", id);
        Optional<TypeIndemnisationsDTO> typeIndemnisationsDTO = typeIndemnisationsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeIndemnisationsDTO);
    }

    /**
     * {@code DELETE  /type-indemnisations/:id} : delete the "id" typeIndemnisations.
     *
     * @param id the id of the typeIndemnisationsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-indemnisations/{id}")
    public ResponseEntity<Void> deleteTypeIndemnisations(@PathVariable Long id) {
        log.debug("REST request to delete TypeIndemnisations : {}", id);
        typeIndemnisationsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
