package com.mycompany.myapp.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ReclamantTypes} entity.
 */
public class ReclamantTypesDTO implements Serializable {

    private Long id;

    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReclamantTypesDTO reclamantTypesDTO = (ReclamantTypesDTO) o;
        if (reclamantTypesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reclamantTypesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReclamantTypesDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
