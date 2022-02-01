package sn.ssi.sigmap.service.impl;

import sn.ssi.sigmap.service.PlanPassationService;
import sn.ssi.sigmap.domain.PlanPassation;
import sn.ssi.sigmap.repository.PlanPassationRepository;
import sn.ssi.sigmap.service.dto.PlanPassationDTO;
import sn.ssi.sigmap.service.mapper.PlanPassationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PlanPassation}.
 */
@Service
@Transactional
public class PlanPassationServiceImpl implements PlanPassationService {

    private final Logger log = LoggerFactory.getLogger(PlanPassationServiceImpl.class);

    private final PlanPassationRepository planPassationRepository;

    private final PlanPassationMapper planPassationMapper;

    public PlanPassationServiceImpl(PlanPassationRepository planPassationRepository, PlanPassationMapper planPassationMapper) {
        this.planPassationRepository = planPassationRepository;
        this.planPassationMapper = planPassationMapper;
    }

    @Override
    public PlanPassationDTO save(PlanPassationDTO planPassationDTO) {
        log.debug("Request to save PlanPassation : {}", planPassationDTO);
        PlanPassation planPassation = planPassationMapper.toEntity(planPassationDTO);
        planPassation = planPassationRepository.save(planPassation);
        return planPassationMapper.toDto(planPassation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlanPassationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PlanPassations");
        return planPassationRepository.findAll(pageable)
            .map(planPassationMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PlanPassationDTO> findOne(Long id) {
        log.debug("Request to get PlanPassation : {}", id);
        return planPassationRepository.findById(id)
            .map(planPassationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PlanPassation : {}", id);
        planPassationRepository.deleteById(id);
    }
}
