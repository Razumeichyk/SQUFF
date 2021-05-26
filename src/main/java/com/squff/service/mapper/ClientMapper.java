package com.squff.service.mapper;

import com.squff.domain.*;
import com.squff.service.dto.ClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    ClientDTO toDto(Client s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoId(Client client);
}
