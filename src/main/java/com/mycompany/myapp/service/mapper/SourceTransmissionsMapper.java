package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.SourceTransmissionsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SourceTransmissions} and its DTO {@link SourceTransmissionsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SourceTransmissionsMapper extends EntityMapper<SourceTransmissionsDTO, SourceTransmissions> {



    default SourceTransmissions fromId(Long id) {
        if (id == null) {
            return null;
        }
        SourceTransmissions sourceTransmissions = new SourceTransmissions();
        sourceTransmissions.setId(id);
        return sourceTransmissions;
    }
}
