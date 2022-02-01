package sn.ssi.sigmap.web.rest;

import sn.ssi.sigmap.service.PlanPassationService;
import sn.ssi.sigmap.web.rest.errors.BadRequestAlertException;
import sn.ssi.sigmap.service.dto.PlanPassationDTO;

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
 * REST controller for managing {@link sn.ssi.sigmap.domain.PlanPassation}.
 */
@RestController
@RequestMapping("/api")
public class PlanPassationResource {

    private final Logger log = LoggerFactory.getLogger(PlanPassationResource.class);

    private static final String ENTITY_NAME = "planpassationmsPlanPassation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanPassationService planPassationService;

    public PlanPassationResource(PlanPassationService planPassationService) {
        this.planPassationService = planPassationService;
    }

    /**
     * {@code POST  /plan-passations} : Create a new planPassation.
     *
     * @param planPassationDTO the planPassationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planPassationDTO, or with status {@code 400 (Bad Request)} if the planPassation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plan-passations")
    public ResponseEntity<PlanPassationDTO> createPlanPassation(@Valid @RequestBody PlanPassationDTO planPassationDTO) throws URISyntaxException {
        log.debug("REST request to save PlanPassation : {}", planPassationDTO);
        if (planPassationDTO.getId() != null) {
            throw new BadRequestAlertException("A new planPassation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlanPassationDTO result = planPassationService.save(planPassationDTO);
        return ResponseEntity.created(new URI("/api/plan-passations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plan-passations} : Updates an existing planPassation.
     *
     * @param planPassationDTO the planPassationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planPassationDTO,
     * or with status {@code 400 (Bad Request)} if the planPassationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planPassationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plan-passations")
    public ResponseEntity<PlanPassationDTO> updatePlanPassation(@Valid @RequestBody PlanPassationDTO planPassationDTO) throws URISyntaxException {
        log.debug("REST request to update PlanPassation : {}", planPassationDTO);
        if (planPassationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlanPassationDTO result = planPassationService.save(planPassationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planPassationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plan-passations} : get all the planPassations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planPassations in body.
     */
    @GetMapping("/plan-passations")
    public ResponseEntity<List<PlanPassationDTO>> getAllPlanPassations(Pageable pageable) {
        log.debug("REST request to get a page of PlanPassations");
        Page<PlanPassationDTO> page = planPassationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plan-passations/:id} : get the "id" planPassation.
     *
     * @param id the id of the planPassationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planPassationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plan-passations/{id}")
    public ResponseEntity<PlanPassationDTO> getPlanPassation(@PathVariable Long id) {
        log.debug("REST request to get PlanPassation : {}", id);
        Optional<PlanPassationDTO> planPassationDTO = planPassationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planPassationDTO);
    }

    /**
     * {@code DELETE  /plan-passations/:id} : delete the "id" planPassation.
     *
     * @param id the id of the planPassationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plan-passations/{id}")
    public ResponseEntity<Void> deletePlanPassation(@PathVariable Long id) {
        log.debug("REST request to delete PlanPassation : {}", id);
        planPassationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
