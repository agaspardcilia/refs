package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CivilitesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Civilites} and its DTO {@link CivilitesDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CivilitesMapper extends EntityMapper<CivilitesDTO, Civilites> {



    default Civilites fromId(Long id) {
        if (id == null) {
            return null;
        }
        Civilites civilites = new Civilites();
        civilites.setId(id);
        return civilites;
    }
}
