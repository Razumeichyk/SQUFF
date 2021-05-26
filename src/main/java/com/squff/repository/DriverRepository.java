package com.squff.repository;

import com.squff.domain.Driver;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Driver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DriverRepository extends R2dbcRepository<Driver, Long>, DriverRepositoryInternal {
    @Query("SELECT * FROM driver entity WHERE entity.user_id = :id")
    Flux<Driver> findByUser(Long id);

    @Query("SELECT * FROM driver entity WHERE entity.user_id IS NULL")
    Flux<Driver> findAllWhereUserIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Driver> findAll();

    @Override
    Mono<Driver> findById(Long id);

    @Override
    <S extends Driver> Mono<S> save(S entity);
}

interface DriverRepositoryInternal {
    <S extends Driver> Mono<S> insert(S entity);
    <S extends Driver> Mono<S> save(S entity);
    Mono<Integer> update(Driver entity);

    Flux<Driver> findAll();
    Mono<Driver> findById(Long id);
    Flux<Driver> findAllBy(Pageable pageable);
    Flux<Driver> findAllBy(Pageable pageable, Criteria criteria);
}
