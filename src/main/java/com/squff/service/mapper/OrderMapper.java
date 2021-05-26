package com.squff.service.mapper;

import com.squff.domain.*;
import com.squff.service.dto.OrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring", uses = { DriverMapper.class, ClientMapper.class })
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "driver", source = "driver", qualifiedByName = "id")
    @Mapping(target = "client", source = "client", qualifiedByName = "id")
    OrderDTO toDto(Order s);
}
