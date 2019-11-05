package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.CollectivitesService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.CollectivitesDTO;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.Collectivites}.
 */
@RestController
@RequestMapping("/api")
public class CollectivitesResource {

    private final Logger log = LoggerFactory.getLogger(CollectivitesResource.class);

    private static final String ENTITY_NAME = "collectivites";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CollectivitesService collectivitesService;

    public CollectivitesResource(CollectivitesService collectivitesService) {
        this.collectivitesService = collectivitesService;
    }

    /**
     * {@code POST  /collectivites} : Create a new collectivites.
     *
     * @param collectivitesDTO the collectivitesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new collectivitesDTO, or with status {@code 400 (Bad Request)} if the collectivites has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/collectivites")
    public ResponseEntity<CollectivitesDTO> createCollectivites(@RequestBody CollectivitesDTO collectivitesDTO) throws URISyntaxException {
        log.debug("REST request to save Collectivites : {}", collectivitesDTO);
        if (collectivitesDTO.getId() != null) {
            throw new BadRequestAlertException("A new collectivites cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CollectivitesDTO result = collectivitesService.save(collectivitesDTO);
        return ResponseEntity.created(new URI("/api/collectivites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /collectivites} : Updates an existing collectivites.
     *
     * @param collectivitesDTO the collectivitesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collectivitesDTO,
     * or with status {@code 400 (Bad Request)} if the collectivitesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the collectivitesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/collectivites")
    public ResponseEntity<CollectivitesDTO> updateCollectivites(@RequestBody CollectivitesDTO collectivitesDTO) throws URISyntaxException {
        log.debug("REST request to update Collectivites : {}", collectivitesDTO);
        if (collectivitesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CollectivitesDTO result = collectivitesService.save(collectivitesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collectivitesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /collectivites} : get all the collectivites.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collectivites in body.
     */
    @GetMapping("/collectivites")
    public List<CollectivitesDTO> getAllCollectivites() {
        log.debug("REST request to get all Collectivites");
        return collectivitesService.findAll();
    }

    /**
     * {@code GET  /collectivites/:id} : get the "id" collectivites.
     *
     * @param id the id of the collectivitesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the collectivitesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/collectivites/{id}")
    public ResponseEntity<CollectivitesDTO> getCollectivites(@PathVariable Long id) {
        log.debug("REST request to get Collectivites : {}", id);
        Optional<CollectivitesDTO> collectivitesDTO = collectivitesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collectivitesDTO);
    }

    /**
     * {@code DELETE  /collectivites/:id} : delete the "id" collectivites.
     *
     * @param id the id of the collectivitesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/collectivites/{id}")
    public ResponseEntity<Void> deleteCollectivites(@PathVariable Long id) {
        log.debug("REST request to delete Collectivites : {}", id);
        collectivitesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
