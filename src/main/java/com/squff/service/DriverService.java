package com.squff.service;

import com.squff.service.dto.DriverDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.squff.domain.Driver}.
 */
public interface DriverService {
    /**
     * Save a driver.
     *
     * @param driverDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<DriverDTO> save(DriverDTO driverDTO);

    /**
     * Partially updates a driver.
     *
     * @param driverDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<DriverDTO> partialUpdate(DriverDTO driverDTO);

    /**
     * Get all the drivers.
     *
     * @return the list of entities.
     */
    Flux<DriverDTO> findAll();

    /**
     * Returns the number of drivers available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" driver.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<DriverDTO> findOne(Long id);

    /**
     * Delete the "id" driver.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
