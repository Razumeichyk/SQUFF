package com.squff.service.impl;

import com.squff.domain.Client;
import com.squff.repository.ClientRepository;
import com.squff.service.ClientService;
import com.squff.service.dto.ClientDTO;
import com.squff.service.mapper.ClientMapper;
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
 * Service Implementation for managing {@link Client}.
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public Mono<ClientDTO> save(ClientDTO clientDTO) {
        log.debug("Request to save Client : {}", clientDTO);
        return clientRepository.save(clientMapper.toEntity(clientDTO)).map(clientMapper::toDto);
    }

    @Override
    public Mono<ClientDTO> partialUpdate(ClientDTO clientDTO) {
        log.debug("Request to partially update Client : {}", clientDTO);

        return clientRepository
            .findById(clientDTO.getId())
            .map(
                existingClient -> {
                    clientMapper.partialUpdate(existingClient, clientDTO);
                    return existingClient;
                }
            )
            .flatMap(clientRepository::save)
            .map(clientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ClientDTO> findAll() {
        log.debug("Request to get all Clients");
        return clientRepository.findAll().map(clientMapper::toDto);
    }

    public Mono<Long> countAll() {
        return clientRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ClientDTO> findOne(Long id) {
        log.debug("Request to get Client : {}", id);
        return clientRepository.findById(id).map(clientMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Client : {}", id);
        return clientRepository.deleteById(id);
    }
}
