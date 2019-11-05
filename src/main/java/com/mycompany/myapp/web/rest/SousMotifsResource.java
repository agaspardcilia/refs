package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.SousMotifsService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.SousMotifsDTO;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.SousMotifs}.
 */
@RestController
@RequestMapping("/api")
public class SousMotifsResource {

    private final Logger log = LoggerFactory.getLogger(SousMotifsResource.class);

    private static final String ENTITY_NAME = "sousMotifs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SousMotifsService sousMotifsService;

    public SousMotifsResource(SousMotifsService sousMotifsService) {
        this.sousMotifsService = sousMotifsService;
    }

    /**
     * {@code POST  /sous-motifs} : Create a new sousMotifs.
     *
     * @param sousMotifsDTO the sousMotifsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sousMotifsDTO, or with status {@code 400 (Bad Request)} if the sousMotifs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sous-motifs")
    public ResponseEntity<SousMotifsDTO> createSousMotifs(@RequestBody SousMotifsDTO sousMotifsDTO) throws URISyntaxException {
        log.debug("REST request to save SousMotifs : {}", sousMotifsDTO);
        if (sousMotifsDTO.getId() != null) {
            throw new BadRequestAlertException("A new sousMotifs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SousMotifsDTO result = sousMotifsService.save(sousMotifsDTO);
        return ResponseEntity.created(new URI("/api/sous-motifs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sous-motifs} : Updates an existing sousMotifs.
     *
     * @param sousMotifsDTO the sousMotifsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousMotifsDTO,
     * or with status {@code 400 (Bad Request)} if the sousMotifsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sousMotifsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sous-motifs")
    public ResponseEntity<SousMotifsDTO> updateSousMotifs(@RequestBody SousMotifsDTO sousMotifsDTO) throws URISyntaxException {
        log.debug("REST request to update SousMotifs : {}", sousMotifsDTO);
        if (sousMotifsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SousMotifsDTO result = sousMotifsService.save(sousMotifsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sousMotifsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sous-motifs} : get all the sousMotifs.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sousMotifs in body.
     */
    @GetMapping("/sous-motifs")
    public List<SousMotifsDTO> getAllSousMotifs() {
        log.debug("REST request to get all SousMotifs");
        return sousMotifsService.findAll();
    }

    /**
     * {@code GET  /sous-motifs/:id} : get the "id" sousMotifs.
     *
     * @param id the id of the sousMotifsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sousMotifsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sous-motifs/{id}")
    public ResponseEntity<SousMotifsDTO> getSousMotifs(@PathVariable Long id) {
        log.debug("REST request to get SousMotifs : {}", id);
        Optional<SousMotifsDTO> sousMotifsDTO = sousMotifsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sousMotifsDTO);
    }

    /**
     * {@code DELETE  /sous-motifs/:id} : delete the "id" sousMotifs.
     *
     * @param id the id of the sousMotifsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sous-motifs/{id}")
    public ResponseEntity<Void> deleteSousMotifs(@PathVariable Long id) {
        log.debug("REST request to delete SousMotifs : {}", id);
        sousMotifsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
