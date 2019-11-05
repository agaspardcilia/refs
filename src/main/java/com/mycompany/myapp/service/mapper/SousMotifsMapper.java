package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.SousMotifsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SousMotifs} and its DTO {@link SousMotifsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SousMotifsMapper extends EntityMapper<SousMotifsDTO, SousMotifs> {



    default SousMotifs fromId(Long id) {
        if (id == null) {
            return null;
        }
        SousMotifs sousMotifs = new SousMotifs();
        sousMotifs.setId(id);
        return sousMotifs;
    }
}
