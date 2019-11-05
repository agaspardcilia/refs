package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.SourceTransmissionsService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.SourceTransmissionsDTO;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.SourceTransmissions}.
 */
@RestController
@RequestMapping("/api")
public class SourceTransmissionsResource {

    private final Logger log = LoggerFactory.getLogger(SourceTransmissionsResource.class);

    private static final String ENTITY_NAME = "sourceTransmissions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SourceTransmissionsService sourceTransmissionsService;

    public SourceTransmissionsResource(SourceTransmissionsService sourceTransmissionsService) {
        this.sourceTransmissionsService = sourceTransmissionsService;
    }

    /**
     * {@code POST  /source-transmissions} : Create a new sourceTransmissions.
     *
     * @param sourceTransmissionsDTO the sourceTransmissionsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sourceTransmissionsDTO, or with status {@code 400 (Bad Request)} if the sourceTransmissions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/source-transmissions")
    public ResponseEntity<SourceTransmissionsDTO> createSourceTransmissions(@RequestBody SourceTransmissionsDTO sourceTransmissionsDTO) throws URISyntaxException {
        log.debug("REST request to save SourceTransmissions : {}", sourceTransmissionsDTO);
        if (sourceTransmissionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new sourceTransmissions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SourceTransmissionsDTO result = sourceTransmissionsService.save(sourceTransmissionsDTO);
        return ResponseEntity.created(new URI("/api/source-transmissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /source-transmissions} : Updates an existing sourceTransmissions.
     *
     * @param sourceTransmissionsDTO the sourceTransmissionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceTransmissionsDTO,
     * or with status {@code 400 (Bad Request)} if the sourceTransmissionsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sourceTransmissionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/source-transmissions")
    public ResponseEntity<SourceTransmissionsDTO> updateSourceTransmissions(@RequestBody SourceTransmissionsDTO sourceTransmissionsDTO) throws URISyntaxException {
        log.debug("REST request to update SourceTransmissions : {}", sourceTransmissionsDTO);
        if (sourceTransmissionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SourceTransmissionsDTO result = sourceTransmissionsService.save(sourceTransmissionsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourceTransmissionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /source-transmissions} : get all the sourceTransmissions.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sourceTransmissions in body.
     */
    @GetMapping("/source-transmissions")
    public List<SourceTransmissionsDTO> getAllSourceTransmissions() {
        log.debug("REST request to get all SourceTransmissions");
        return sourceTransmissionsService.findAll();
    }

    /**
     * {@code GET  /source-transmissions/:id} : get the "id" sourceTransmissions.
     *
     * @param id the id of the sourceTransmissionsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sourceTransmissionsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/source-transmissions/{id}")
    public ResponseEntity<SourceTransmissionsDTO> getSourceTransmissions(@PathVariable Long id) {
        log.debug("REST request to get SourceTransmissions : {}", id);
        Optional<SourceTransmissionsDTO> sourceTransmissionsDTO = sourceTransmissionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sourceTransmissionsDTO);
    }

    /**
     * {@code DELETE  /source-transmissions/:id} : delete the "id" sourceTransmissions.
     *
     * @param id the id of the sourceTransmissionsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/source-transmissions/{id}")
    public ResponseEntity<Void> deleteSourceTransmissions(@PathVariable Long id) {
        log.debug("REST request to delete SourceTransmissions : {}", id);
        sourceTransmissionsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
