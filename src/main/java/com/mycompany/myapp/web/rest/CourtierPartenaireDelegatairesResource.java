package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.CourtierPartenaireDelegatairesService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.CourtierPartenaireDelegatairesDTO;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.CourtierPartenaireDelegataires}.
 */
@RestController
@RequestMapping("/api")
public class CourtierPartenaireDelegatairesResource {

    private final Logger log = LoggerFactory.getLogger(CourtierPartenaireDelegatairesResource.class);

    private static final String ENTITY_NAME = "courtierPartenaireDelegataires";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourtierPartenaireDelegatairesService courtierPartenaireDelegatairesService;

    public CourtierPartenaireDelegatairesResource(CourtierPartenaireDelegatairesService courtierPartenaireDelegatairesService) {
        this.courtierPartenaireDelegatairesService = courtierPartenaireDelegatairesService;
    }

    /**
     * {@code POST  /courtier-partenaire-delegataires} : Create a new courtierPartenaireDelegataires.
     *
     * @param courtierPartenaireDelegatairesDTO the courtierPartenaireDelegatairesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courtierPartenaireDelegatairesDTO, or with status {@code 400 (Bad Request)} if the courtierPartenaireDelegataires has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/courtier-partenaire-delegataires")
    public ResponseEntity<CourtierPartenaireDelegatairesDTO> createCourtierPartenaireDelegataires(@RequestBody CourtierPartenaireDelegatairesDTO courtierPartenaireDelegatairesDTO) throws URISyntaxException {
        log.debug("REST request to save CourtierPartenaireDelegataires : {}", courtierPartenaireDelegatairesDTO);
        if (courtierPartenaireDelegatairesDTO.getId() != null) {
            throw new BadRequestAlertException("A new courtierPartenaireDelegataires cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourtierPartenaireDelegatairesDTO result = courtierPartenaireDelegatairesService.save(courtierPartenaireDelegatairesDTO);
        return ResponseEntity.created(new URI("/api/courtier-partenaire-delegataires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /courtier-partenaire-delegataires} : Updates an existing courtierPartenaireDelegataires.
     *
     * @param courtierPartenaireDelegatairesDTO the courtierPartenaireDelegatairesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courtierPartenaireDelegatairesDTO,
     * or with status {@code 400 (Bad Request)} if the courtierPartenaireDelegatairesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courtierPartenaireDelegatairesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/courtier-partenaire-delegataires")
    public ResponseEntity<CourtierPartenaireDelegatairesDTO> updateCourtierPartenaireDelegataires(@RequestBody CourtierPartenaireDelegatairesDTO courtierPartenaireDelegatairesDTO) throws URISyntaxException {
        log.debug("REST request to update CourtierPartenaireDelegataires : {}", courtierPartenaireDelegatairesDTO);
        if (courtierPartenaireDelegatairesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourtierPartenaireDelegatairesDTO result = courtierPartenaireDelegatairesService.save(courtierPartenaireDelegatairesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courtierPartenaireDelegatairesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /courtier-partenaire-delegataires} : get all the courtierPartenaireDelegataires.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courtierPartenaireDelegataires in body.
     */
    @GetMapping("/courtier-partenaire-delegataires")
    public List<CourtierPartenaireDelegatairesDTO> getAllCourtierPartenaireDelegataires() {
        log.debug("REST request to get all CourtierPartenaireDelegataires");
        return courtierPartenaireDelegatairesService.findAll();
    }

    /**
     * {@code GET  /courtier-partenaire-delegataires/:id} : get the "id" courtierPartenaireDelegataires.
     *
     * @param id the id of the courtierPartenaireDelegatairesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courtierPartenaireDelegatairesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courtier-partenaire-delegataires/{id}")
    public ResponseEntity<CourtierPartenaireDelegatairesDTO> getCourtierPartenaireDelegataires(@PathVariable Long id) {
        log.debug("REST request to get CourtierPartenaireDelegataires : {}", id);
        Optional<CourtierPartenaireDelegatairesDTO> courtierPartenaireDelegatairesDTO = courtierPartenaireDelegatairesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courtierPartenaireDelegatairesDTO);
    }

    /**
     * {@code DELETE  /courtier-partenaire-delegataires/:id} : delete the "id" courtierPartenaireDelegataires.
     *
     * @param id the id of the courtierPartenaireDelegatairesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/courtier-partenaire-delegataires/{id}")
    public ResponseEntity<Void> deleteCourtierPartenaireDelegataires(@PathVariable Long id) {
        log.debug("REST request to delete CourtierPartenaireDelegataires : {}", id);
        courtierPartenaireDelegatairesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
