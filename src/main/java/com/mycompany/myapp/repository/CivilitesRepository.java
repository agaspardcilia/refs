package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.Civilites;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Civilites entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CivilitesRepository extends JpaRepository<Civilites, Long> {

}
