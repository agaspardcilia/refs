package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.TypeClientsService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.TypeClientsDTO;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.TypeClients}.
 */
@RestController
@RequestMapping("/api")
public class TypeClientsResource {

    private final Logger log = LoggerFactory.getLogger(TypeClientsResource.class);

    private static final String ENTITY_NAME = "typeClients";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeClientsService typeClientsService;

    public TypeClientsResource(TypeClientsService typeClientsService) {
        this.typeClientsService = typeClientsService;
    }

    /**
     * {@code POST  /type-clients} : Create a new typeClients.
     *
     * @param typeClientsDTO the typeClientsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeClientsDTO, or with status {@code 400 (Bad Request)} if the typeClients has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-clients")
    public ResponseEntity<TypeClientsDTO> createTypeClients(@RequestBody TypeClientsDTO typeClientsDTO) throws URISyntaxException {
        log.debug("REST request to save TypeClients : {}", typeClientsDTO);
        if (typeClientsDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeClients cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeClientsDTO result = typeClientsService.save(typeClientsDTO);
        return ResponseEntity.created(new URI("/api/type-clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-clients} : Updates an existing typeClients.
     *
     * @param typeClientsDTO the typeClientsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeClientsDTO,
     * or with status {@code 400 (Bad Request)} if the typeClientsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeClientsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-clients")
    public ResponseEntity<TypeClientsDTO> updateTypeClients(@RequestBody TypeClientsDTO typeClientsDTO) throws URISyntaxException {
        log.debug("REST request to update TypeClients : {}", typeClientsDTO);
        if (typeClientsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeClientsDTO result = typeClientsService.save(typeClientsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeClientsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /type-clients} : get all the typeClients.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeClients in body.
     */
    @GetMapping("/type-clients")
    public List<TypeClientsDTO> getAllTypeClients() {
        log.debug("REST request to get all TypeClients");
        return typeClientsService.findAll();
    }

    /**
     * {@code GET  /type-clients/:id} : get the "id" typeClients.
     *
     * @param id the id of the typeClientsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeClientsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-clients/{id}")
    public ResponseEntity<TypeClientsDTO> getTypeClients(@PathVariable Long id) {
        log.debug("REST request to get TypeClients : {}", id);
        Optional<TypeClientsDTO> typeClientsDTO = typeClientsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeClientsDTO);
    }

    /**
     * {@code DELETE  /type-clients/:id} : delete the "id" typeClients.
     *
     * @param id the id of the typeClientsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-clients/{id}")
    public ResponseEntity<Void> deleteTypeClients(@PathVariable Long id) {
        log.debug("REST request to delete TypeClients : {}", id);
        typeClientsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
