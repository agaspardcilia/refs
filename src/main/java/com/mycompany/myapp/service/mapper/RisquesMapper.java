package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.RisquesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Risques} and its DTO {@link RisquesDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RisquesMapper extends EntityMapper<RisquesDTO, Risques> {



    default Risques fromId(Long id) {
        if (id == null) {
            return null;
        }
        Risques risques = new Risques();
        risques.setId(id);
        return risques;
    }
}
