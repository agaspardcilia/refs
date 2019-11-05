package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.SupportDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Support} and its DTO {@link SupportDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SupportMapper extends EntityMapper<SupportDTO, Support> {



    default Support fromId(Long id) {
        if (id == null) {
            return null;
        }
        Support support = new Support();
        support.setId(id);
        return support;
    }
}
