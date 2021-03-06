package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.domain.SygRealisation;
import sn.ssi.sigmap.service.SygRealisationService;
import sn.ssi.sigmap.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link sn.ssi.sigmap.domain.SygRealisation}.
 */
@RestController
@RequestMapping("/api")
public class SygRealisationResource {

    private final Logger log = LoggerFactory.getLogger(SygRealisationResource.class);

    private static final String ENTITY_NAME = "planpassationmsSygRealisation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SygRealisationService sygRealisationService;

    public SygRealisationResource(SygRealisationService sygRealisationService) {
        this.sygRealisationService = sygRealisationService;
    }

    /**
     * {@code POST  /syg-realisations} : Create a new sygRealisation.
     *
     * @param sygRealisation the sygRealisation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sygRealisation, or with status {@code 400 (Bad Request)} if the sygRealisation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/syg-realisations")
    public ResponseEntity<SygRealisation> createSygRealisation(@Valid @RequestBody SygRealisation sygRealisation) throws URISyntaxException {
        log.debug("REST request to save SygRealisation : {}", sygRealisation);
        if (sygRealisation.getId() != null) {
            throw new BadRequestAlertException("A new sygRealisation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SygRealisation result = sygRealisationService.save(sygRealisation);
        return ResponseEntity.created(new URI("/api/syg-realisations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /syg-realisations} : Updates an existing sygRealisation.
     *
     * @param sygRealisation the sygRealisation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sygRealisation,
     * or with status {@code 400 (Bad Request)} if the sygRealisation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sygRealisation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/syg-realisations")
    public ResponseEntity<SygRealisation> updateSygRealisation(@Valid @RequestBody SygRealisation sygRealisation) throws URISyntaxException {
        log.debug("REST request to update SygRealisation : {}", sygRealisation);
        if (sygRealisation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SygRealisation result = sygRealisationService.save(sygRealisation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sygRealisation.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /syg-realisations} : get all the sygRealisations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sygRealisations in body.
     */
    @GetMapping("/syg-realisations")
    public ResponseEntity<List<SygRealisation>> getAllSygRealisations(Pageable pageable) {
        log.debug("REST request to get a page of SygRealisations");
        Page<SygRealisation> page = sygRealisationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /syg-realisations/:id} : get the "id" sygRealisation.
     *
     * @param id the id of the sygRealisation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sygRealisation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/syg-realisations/{id}")
    public ResponseEntity<SygRealisation> getSygRealisation(@PathVariable Long id) {
        log.debug("REST request to get SygRealisation : {}", id);
        Optional<SygRealisation> sygRealisation = sygRealisationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sygRealisation);
    }

    /**
     * {@code DELETE  /syg-realisations/:id} : delete the "id" sygRealisation.
     *
     * @param id the id of the sygRealisation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/syg-realisations/{id}")
    public ResponseEntity<Void> deleteSygRealisation(@PathVariable Long id) {
        log.debug("REST request to delete SygRealisation : {}", id);
        sygRealisationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
