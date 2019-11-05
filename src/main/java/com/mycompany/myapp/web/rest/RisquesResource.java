package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.RisquesService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.RisquesDTO;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.Risques}.
 */
@RestController
@RequestMapping("/api")
public class RisquesResource {

    private final Logger log = LoggerFactory.getLogger(RisquesResource.class);

    private static final String ENTITY_NAME = "risques";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RisquesService risquesService;

    public RisquesResource(RisquesService risquesService) {
        this.risquesService = risquesService;
    }

    /**
     * {@code POST  /risques} : Create a new risques.
     *
     * @param risquesDTO the risquesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new risquesDTO, or with status {@code 400 (Bad Request)} if the risques has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/risques")
    public ResponseEntity<RisquesDTO> createRisques(@RequestBody RisquesDTO risquesDTO) throws URISyntaxException {
        log.debug("REST request to save Risques : {}", risquesDTO);
        if (risquesDTO.getId() != null) {
            throw new BadRequestAlertException("A new risques cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RisquesDTO result = risquesService.save(risquesDTO);
        return ResponseEntity.created(new URI("/api/risques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /risques} : Updates an existing risques.
     *
     * @param risquesDTO the risquesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated risquesDTO,
     * or with status {@code 400 (Bad Request)} if the risquesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the risquesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/risques")
    public ResponseEntity<RisquesDTO> updateRisques(@RequestBody RisquesDTO risquesDTO) throws URISyntaxException {
        log.debug("REST request to update Risques : {}", risquesDTO);
        if (risquesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RisquesDTO result = risquesService.save(risquesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, risquesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /risques} : get all the risques.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of risques in body.
     */
    @GetMapping("/risques")
    public List<RisquesDTO> getAllRisques() {
        log.debug("REST request to get all Risques");
        return risquesService.findAll();
    }

    /**
     * {@code GET  /risques/:id} : get the "id" risques.
     *
     * @param id the id of the risquesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the risquesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/risques/{id}")
    public ResponseEntity<RisquesDTO> getRisques(@PathVariable Long id) {
        log.debug("REST request to get Risques : {}", id);
        Optional<RisquesDTO> risquesDTO = risquesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(risquesDTO);
    }

    /**
     * {@code DELETE  /risques/:id} : delete the "id" risques.
     *
     * @param id the id of the risquesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/risques/{id}")
    public ResponseEntity<Void> deleteRisques(@PathVariable Long id) {
        log.debug("REST request to delete Risques : {}", id);
        risquesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
