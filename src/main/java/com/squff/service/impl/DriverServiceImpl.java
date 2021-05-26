package com.squff.service.impl;

import com.squff.domain.Driver;
import com.squff.repository.DriverRepository;
import com.squff.service.DriverService;
import com.squff.service.dto.DriverDTO;
import com.squff.service.mapper.DriverMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Driver}.
 */
@Service
@Transactional
public class DriverServiceImpl implements DriverService {

    private final Logger log = LoggerFactory.getLogger(DriverServiceImpl.class);

    private final DriverRepository driverRepository;

    private final DriverMapper driverMapper;

    public DriverServiceImpl(DriverRepository driverRepository, DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    @Override
    public Mono<DriverDTO> save(DriverDTO driverDTO) {
        log.debug("Request to save Driver : {}", driverDTO);
        return driverRepository.save(driverMapper.toEntity(driverDTO)).map(driverMapper::toDto);
    }

    @Override
    public Mono<DriverDTO> partialUpdate(DriverDTO driverDTO) {
        log.debug("Request to partially update Driver : {}", driverDTO);

        return driverRepository
            .findById(driverDTO.getId())
            .map(
                existingDriver -> {
                    driverMapper.partialUpdate(existingDriver, driverDTO);
                    return existingDriver;
                }
            )
            .flatMap(driverRepository::save)
            .map(driverMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<DriverDTO> findAll() {
        log.debug("Request to get all Drivers");
        return driverRepository.findAll().map(driverMapper::toDto);
    }

    public Mono<Long> countAll() {
        return driverRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<DriverDTO> findOne(Long id) {
        log.debug("Request to get Driver : {}", id);
        return driverRepository.findById(id).map(driverMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Driver : {}", id);
        return driverRepository.deleteById(id);
    }
}
