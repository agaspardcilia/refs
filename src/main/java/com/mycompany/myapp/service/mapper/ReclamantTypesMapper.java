package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.ReclamantTypesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReclamantTypes} and its DTO {@link ReclamantTypesDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReclamantTypesMapper extends EntityMapper<ReclamantTypesDTO, ReclamantTypes> {



    default ReclamantTypes fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReclamantTypes reclamantTypes = new ReclamantTypes();
        reclamantTypes.setId(id);
        return reclamantTypes;
    }
}
