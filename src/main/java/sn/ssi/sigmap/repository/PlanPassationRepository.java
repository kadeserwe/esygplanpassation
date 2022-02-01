package sn.ssi.sigmap.repository;

import sn.ssi.sigmap.domain.PlanPassation;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PlanPassation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanPassationRepository extends JpaRepository<PlanPassation, Long> {
}
