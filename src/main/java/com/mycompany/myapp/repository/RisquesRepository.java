package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.Risques;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Risques entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RisquesRepository extends JpaRepository<Risques, Long> {

}
