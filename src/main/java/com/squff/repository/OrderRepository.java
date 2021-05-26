package com.squff.repository;

import com.squff.domain.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends R2dbcRepository<Order, Long>, OrderRepositoryInternal {
    @Query("SELECT * FROM jhi_order entity WHERE entity.driver_id = :id")
    Flux<Order> findByDriver(Long id);

    @Query("SELECT * FROM jhi_order entity WHERE entity.driver_id IS NULL")
    Flux<Order> findAllWhereDriverIsNull();

    @Query("SELECT * FROM jhi_order entity WHERE entity.client_id = :id")
    Flux<Order> findByClient(Long id);

    @Query("SELECT * FROM jhi_order entity WHERE entity.client_id IS NULL")
    Flux<Order> findAllWhereClientIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Order> findAll();

    @Override
    Mono<Order> findById(Long id);

    @Override
    <S extends Order> Mono<S> save(S entity);
}

interface OrderRepositoryInternal {
    <S extends Order> Mono<S> insert(S entity);
    <S extends Order> Mono<S> save(S entity);
    Mono<Integer> update(Order entity);

    Flux<Order> findAll();
    Mono<Order> findById(Long id);
    Flux<Order> findAllBy(Pageable pageable);
    Flux<Order> findAllBy(Pageable pageable, Criteria criteria);
}
