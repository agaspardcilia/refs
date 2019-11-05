package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.TypeClientsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeClients} and its DTO {@link TypeClientsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeClientsMapper extends EntityMapper<TypeClientsDTO, TypeClients> {



    default TypeClients fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeClients typeClients = new TypeClients();
        typeClients.setId(id);
        return typeClients;
    }
}
