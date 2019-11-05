package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.TypeIndemnisations;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TypeIndemnisations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeIndemnisationsRepository extends JpaRepository<TypeIndemnisations, Long> {

}
