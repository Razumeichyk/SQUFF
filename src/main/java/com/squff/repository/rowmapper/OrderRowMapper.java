package com.squff.repository.rowmapper;

import com.squff.domain.Order;
import com.squff.domain.enumeration.Status;
import com.squff.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Order}, with proper type conversions.
 */
@Service
public class OrderRowMapper implements BiFunction<Row, String, Order> {

    private final ColumnConverter converter;

    public OrderRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Order} stored in the database.
     */
    @Override
    public Order apply(Row row, String prefix) {
        Order entity = new Order();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setGeneratedCode(converter.fromRow(row, prefix + "_generated_code", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", LocalDate.class));
        entity.setShippedAt(converter.fromRow(row, prefix + "_shipped_at", LocalDate.class));
        entity.setRecievedAt(converter.fromRow(row, prefix + "_recieved_at", LocalDate.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", Status.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setDriverId(converter.fromRow(row, prefix + "_driver_id", Long.class));
        entity.setClientId(converter.fromRow(row, prefix + "_client_id", Long.class));
        return entity;
    }
}
