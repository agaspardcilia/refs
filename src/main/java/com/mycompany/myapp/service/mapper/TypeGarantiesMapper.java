package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.TypeGarantiesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeGaranties} and its DTO {@link TypeGarantiesDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeGarantiesMapper extends EntityMapper<TypeGarantiesDTO, TypeGaranties> {



    default TypeGaranties fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeGaranties typeGaranties = new TypeGaranties();
        typeGaranties.setId(id);
        return typeGaranties;
    }
}
