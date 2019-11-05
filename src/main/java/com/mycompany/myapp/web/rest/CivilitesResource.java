package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.CivilitesService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.CivilitesDTO;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.Civilites}.
 */
@RestController
@RequestMapping("/api")
public class CivilitesResource {

    private final Logger log = LoggerFactory.getLogger(CivilitesResource.class);

    private static final String ENTITY_NAME = "civilites";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CivilitesService civilitesService;

    public CivilitesResource(CivilitesService civilitesService) {
        this.civilitesService = civilitesService;
    }

    /**
     * {@code POST  /civilites} : Create a new civilites.
     *
     * @param civilitesDTO the civilitesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new civilitesDTO, or with status {@code 400 (Bad Request)} if the civilites has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/civilites")
    public ResponseEntity<CivilitesDTO> createCivilites(@RequestBody CivilitesDTO civilitesDTO) throws URISyntaxException {
        log.debug("REST request to save Civilites : {}", civilitesDTO);
        if (civilitesDTO.getId() != null) {
            throw new BadRequestAlertException("A new civilites cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CivilitesDTO result = civilitesService.save(civilitesDTO);
        return ResponseEntity.created(new URI("/api/civilites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /civilites} : Updates an existing civilites.
     *
     * @param civilitesDTO the civilitesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated civilitesDTO,
     * or with status {@code 400 (Bad Request)} if the civilitesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the civilitesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/civilites")
    public ResponseEntity<CivilitesDTO> updateCivilites(@RequestBody CivilitesDTO civilitesDTO) throws URISyntaxException {
        log.debug("REST request to update Civilites : {}", civilitesDTO);
        if (civilitesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CivilitesDTO result = civilitesService.save(civilitesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, civilitesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /civilites} : get all the civilites.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of civilites in body.
     */
    @GetMapping("/civilites")
    public List<CivilitesDTO> getAllCivilites() {
        log.debug("REST request to get all Civilites");
        return civilitesService.findAll();
    }

    /**
     * {@code GET  /civilites/:id} : get the "id" civilites.
     *
     * @param id the id of the civilitesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the civilitesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/civilites/{id}")
    public ResponseEntity<CivilitesDTO> getCivilites(@PathVariable Long id) {
        log.debug("REST request to get Civilites : {}", id);
        Optional<CivilitesDTO> civilitesDTO = civilitesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(civilitesDTO);
    }

    /**
     * {@code DELETE  /civilites/:id} : delete the "id" civilites.
     *
     * @param id the id of the civilitesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/civilites/{id}")
    public ResponseEntity<Void> deleteCivilites(@PathVariable Long id) {
        log.debug("REST request to delete Civilites : {}", id);
        civilitesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
