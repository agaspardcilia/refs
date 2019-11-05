package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CourtierPartenaireDelegatairesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CourtierPartenaireDelegataires} and its DTO {@link CourtierPartenaireDelegatairesDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CourtierPartenaireDelegatairesMapper extends EntityMapper<CourtierPartenaireDelegatairesDTO, CourtierPartenaireDelegataires> {



    default CourtierPartenaireDelegataires fromId(Long id) {
        if (id == null) {
            return null;
        }
        CourtierPartenaireDelegataires courtierPartenaireDelegataires = new CourtierPartenaireDelegataires();
        courtierPartenaireDelegataires.setId(id);
        return courtierPartenaireDelegataires;
    }
}
