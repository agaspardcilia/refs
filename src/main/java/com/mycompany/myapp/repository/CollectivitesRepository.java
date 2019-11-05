package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.Collectivites;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Collectivites entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CollectivitesRepository extends JpaRepository<Collectivites, Long> {

}
