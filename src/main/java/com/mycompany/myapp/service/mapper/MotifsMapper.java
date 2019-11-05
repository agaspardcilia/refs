package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.MotifsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Motifs} and its DTO {@link MotifsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MotifsMapper extends EntityMapper<MotifsDTO, Motifs> {



    default Motifs fromId(Long id) {
        if (id == null) {
            return null;
        }
        Motifs motifs = new Motifs();
        motifs.setId(id);
        return motifs;
    }
}
