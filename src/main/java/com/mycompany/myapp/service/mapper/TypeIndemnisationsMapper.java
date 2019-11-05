package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.TypeIndemnisationsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeIndemnisations} and its DTO {@link TypeIndemnisationsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeIndemnisationsMapper extends EntityMapper<TypeIndemnisationsDTO, TypeIndemnisations> {



    default TypeIndemnisations fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeIndemnisations typeIndemnisations = new TypeIndemnisations();
        typeIndemnisations.setId(id);
        return typeIndemnisations;
    }
}
