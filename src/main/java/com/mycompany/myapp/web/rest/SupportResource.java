package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.SupportService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.SupportDTO;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.Support}.
 */
@RestController
@RequestMapping("/api")
public class SupportResource {

    private final Logger log = LoggerFactory.getLogger(SupportResource.class);

    private static final String ENTITY_NAME = "support";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupportService supportService;

    public SupportResource(SupportService supportService) {
        this.supportService = supportService;
    }

    /**
     * {@code POST  /supports} : Create a new support.
     *
     * @param supportDTO the supportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supportDTO, or with status {@code 400 (Bad Request)} if the support has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/supports")
    public ResponseEntity<SupportDTO> createSupport(@RequestBody SupportDTO supportDTO) throws URISyntaxException {
        log.debug("REST request to save Support : {}", supportDTO);
        if (supportDTO.getId() != null) {
            throw new BadRequestAlertException("A new support cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SupportDTO result = supportService.save(supportDTO);
        return ResponseEntity.created(new URI("/api/supports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /supports} : Updates an existing support.
     *
     * @param supportDTO the supportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supportDTO,
     * or with status {@code 400 (Bad Request)} if the supportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/supports")
    public ResponseEntity<SupportDTO> updateSupport(@RequestBody SupportDTO supportDTO) throws URISyntaxException {
        log.debug("REST request to update Support : {}", supportDTO);
        if (supportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SupportDTO result = supportService.save(supportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /supports} : get all the supports.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supports in body.
     */
    @GetMapping("/supports")
    public List<SupportDTO> getAllSupports() {
        log.debug("REST request to get all Supports");
        return supportService.findAll();
    }

    /**
     * {@code GET  /supports/:id} : get the "id" support.
     *
     * @param id the id of the supportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/supports/{id}")
    public ResponseEntity<SupportDTO> getSupport(@PathVariable Long id) {
        log.debug("REST request to get Support : {}", id);
        Optional<SupportDTO> supportDTO = supportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supportDTO);
    }

    /**
     * {@code DELETE  /supports/:id} : delete the "id" support.
     *
     * @param id the id of the supportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/supports/{id}")
    public ResponseEntity<Void> deleteSupport(@PathVariable Long id) {
        log.debug("REST request to delete Support : {}", id);
        supportService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
