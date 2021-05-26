package com.squff.service;

import com.squff.service.dto.ClientDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.squff.domain.Client}.
 */
public interface ClientService {
    /**
     * Save a client.
     *
     * @param clientDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ClientDTO> save(ClientDTO clientDTO);

    /**
     * Partially updates a client.
     *
     * @param clientDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ClientDTO> partialUpdate(ClientDTO clientDTO);

    /**
     * Get all the clients.
     *
     * @return the list of entities.
     */
    Flux<ClientDTO> findAll();

    /**
     * Returns the number of clients available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" client.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ClientDTO> findOne(Long id);

    /**
     * Delete the "id" client.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
