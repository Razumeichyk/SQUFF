package com.squff.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.squff.domain.Order;
import com.squff.domain.enumeration.Status;
import com.squff.repository.rowmapper.ClientRowMapper;
import com.squff.repository.rowmapper.DriverRowMapper;
import com.squff.repository.rowmapper.OrderRowMapper;
import com.squff.service.EntityManager;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Order entity.
 */
@SuppressWarnings("unused")
class OrderRepositoryInternalImpl implements OrderRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final DriverRowMapper driverMapper;
    private final ClientRowMapper clientMapper;
    private final OrderRowMapper orderMapper;

    private static final Table entityTable = Table.aliased("jhi_order", EntityManager.ENTITY_ALIAS);
    private static final Table driverTable = Table.aliased("driver", "driver");
    private static final Table clientTable = Table.aliased("client", "client");

    public OrderRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        DriverRowMapper driverMapper,
        ClientRowMapper clientMapper,
        OrderRowMapper orderMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.driverMapper = driverMapper;
        this.clientMapper = clientMapper;
        this.orderMapper = orderMapper;
    }

    @Override
    public Flux<Order> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Order> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Order> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = OrderSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(DriverSqlHelper.getColumns(driverTable, "driver"));
        columns.addAll(ClientSqlHelper.getColumns(clientTable, "client"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(driverTable)
            .on(Column.create("driver_id", entityTable))
            .equals(Column.create("id", driverTable))
            .leftOuterJoin(clientTable)
            .on(Column.create("client_id", entityTable))
            .equals(Column.create("id", clientTable));

        String select = entityManager.createSelect(selectFrom, Order.class, pageable, criteria);
        String alias = entityTable.getReferenceName().getReference();
        String selectWhere = Optional
            .ofNullable(criteria)
            .map(
                crit ->
                    new StringBuilder(select)
                        .append(" ")
                        .append("WHERE")
                        .append(" ")
                        .append(alias)
                        .append(".")
                        .append(crit.toString())
                        .toString()
            )
            .orElse(select); // TODO remove once https://github.com/spring-projects/spring-data-jdbc/issues/907 will be fixed
        return db.sql(selectWhere).map(this::process);
    }

    @Override
    public Flux<Order> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Order> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Order process(Row row, RowMetadata metadata) {
        Order entity = orderMapper.apply(row, "e");
        entity.setDriver(driverMapper.apply(row, "driver"));
        entity.setClient(clientMapper.apply(row, "client"));
        return entity;
    }

    @Override
    public <S extends Order> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Order> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update Order with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(Order entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class OrderSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("generated_code", table, columnPrefix + "_generated_code"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("shipped_at", table, columnPrefix + "_shipped_at"));
        columns.add(Column.aliased("recieved_at", table, columnPrefix + "_recieved_at"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));

        columns.add(Column.aliased("driver_id", table, columnPrefix + "_driver_id"));
        columns.add(Column.aliased("client_id", table, columnPrefix + "_client_id"));
        return columns;
    }
}
