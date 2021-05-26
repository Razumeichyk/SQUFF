package com.squff.repository.rowmapper;

import com.squff.domain.Driver;
import com.squff.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Driver}, with proper type conversions.
 */
@Service
public class DriverRowMapper implements BiFunction<Row, String, Driver> {

    private final ColumnConverter converter;

    public DriverRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Driver} stored in the database.
     */
    @Override
    public Driver apply(Row row, String prefix) {
        Driver entity = new Driver();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPlateNumber(converter.fromRow(row, prefix + "_plate_number", String.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
