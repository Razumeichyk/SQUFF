package com.squff.service.mapper;

import com.squff.domain.*;
import com.squff.service.dto.DriverDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Driver} and its DTO {@link DriverDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface DriverMapper extends EntityMapper<DriverDTO, Driver> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    DriverDTO toDto(Driver s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DriverDTO toDtoId(Driver driver);
}
