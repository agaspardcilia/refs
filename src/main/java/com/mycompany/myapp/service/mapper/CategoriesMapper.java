package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CategoriesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Categories} and its DTO {@link CategoriesDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CategoriesMapper extends EntityMapper<CategoriesDTO, Categories> {



    default Categories fromId(Long id) {
        if (id == null) {
            return null;
        }
        Categories categories = new Categories();
        categories.setId(id);
        return categories;
    }
}
