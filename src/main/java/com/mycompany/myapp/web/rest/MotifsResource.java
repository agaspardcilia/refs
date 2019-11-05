package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.MotifsService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.MotifsDTO;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.Motifs}.
 */
@RestController
@RequestMapping("/api")
public class MotifsResource {

    private final Logger log = LoggerFactory.getLogger(MotifsResource.class);

    private static final String ENTITY_NAME = "motifs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MotifsService motifsService;

    public MotifsResource(MotifsService motifsService) {
        this.motifsService = motifsService;
    }

    /**
     * {@code POST  /motifs} : Create a new motifs.
     *
     * @param motifsDTO the motifsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new motifsDTO, or with status {@code 400 (Bad Request)} if the motifs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/motifs")
    public ResponseEntity<MotifsDTO> createMotifs(@RequestBody MotifsDTO motifsDTO) throws URISyntaxException {
        log.debug("REST request to save Motifs : {}", motifsDTO);
        if (motifsDTO.getId() != null) {
            throw new BadRequestAlertException("A new motifs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MotifsDTO result = motifsService.save(motifsDTO);
        return ResponseEntity.created(new URI("/api/motifs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /motifs} : Updates an existing motifs.
     *
     * @param motifsDTO the motifsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated motifsDTO,
     * or with status {@code 400 (Bad Request)} if the motifsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the motifsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/motifs")
    public ResponseEntity<MotifsDTO> updateMotifs(@RequestBody MotifsDTO motifsDTO) throws URISyntaxException {
        log.debug("REST request to update Motifs : {}", motifsDTO);
        if (motifsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MotifsDTO result = motifsService.save(motifsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, motifsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /motifs} : get all the motifs.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of motifs in body.
     */
    @GetMapping("/motifs")
    public List<MotifsDTO> getAllMotifs() {
        log.debug("REST request to get all Motifs");
        return motifsService.findAll();
    }

    /**
     * {@code GET  /motifs/:id} : get the "id" motifs.
     *
     * @param id the id of the motifsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the motifsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/motifs/{id}")
    public ResponseEntity<MotifsDTO> getMotifs(@PathVariable Long id) {
        log.debug("REST request to get Motifs : {}", id);
        Optional<MotifsDTO> motifsDTO = motifsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(motifsDTO);
    }

    /**
     * {@code DELETE  /motifs/:id} : delete the "id" motifs.
     *
     * @param id the id of the motifsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/motifs/{id}")
    public ResponseEntity<Void> deleteMotifs(@PathVariable Long id) {
        log.debug("REST request to delete Motifs : {}", id);
        motifsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
