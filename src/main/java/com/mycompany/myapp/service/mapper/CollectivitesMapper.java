package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CollectivitesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Collectivites} and its DTO {@link CollectivitesDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CollectivitesMapper extends EntityMapper<CollectivitesDTO, Collectivites> {



    default Collectivites fromId(Long id) {
        if (id == null) {
            return null;
        }
        Collectivites collectivites = new Collectivites();
        collectivites.setId(id);
        return collectivites;
    }
}
