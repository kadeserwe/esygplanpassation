package sn.ssi.sigmap.service;

import sn.ssi.sigmap.service.dto.PlanPassationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link sn.ssi.sigmap.domain.PlanPassation}.
 */
public interface PlanPassationService {

    /**
     * Save a planPassation.
     *
     * @param planPassationDTO the entity to save.
     * @return the persisted entity.
     */
    PlanPassationDTO save(PlanPassationDTO planPassationDTO);

    /**
     * Get all the planPassations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlanPassationDTO> findAll(Pageable pageable);


    /**
     * Get the "id" planPassation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlanPassationDTO> findOne(Long id);

    /**
     * Delete the "id" planPassation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
