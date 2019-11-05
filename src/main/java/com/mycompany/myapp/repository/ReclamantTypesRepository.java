package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.ReclamantTypes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ReclamantTypes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReclamantTypesRepository extends JpaRepository<ReclamantTypes, Long> {

}
