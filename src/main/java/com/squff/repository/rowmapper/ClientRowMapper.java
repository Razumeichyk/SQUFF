package com.squff.repository.rowmapper;

import com.squff.domain.Client;
import com.squff.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Client}, with proper type conversions.
 */
@Service
public class ClientRowMapper implements BiFunction<Row, String, Client> {

    private final ColumnConverter converter;

    public ClientRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Client} stored in the database.
     */
    @Override
    public Client apply(Row row, String prefix) {
        Client entity = new Client();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPhoneNumber(converter.fromRow(row, prefix + "_phone_number", String.class));
        entity.setCountry(converter.fromRow(row, prefix + "_country", String.class));
        entity.setCity(converter.fromRow(row, prefix + "_city", String.class));
        entity.setStreetAddress(converter.fromRow(row, prefix + "_street_address", String.class));
        entity.setZipCode(converter.fromRow(row, prefix + "_zip_code", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
