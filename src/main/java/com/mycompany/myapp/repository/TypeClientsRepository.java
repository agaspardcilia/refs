package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.TypeClients;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TypeClients entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeClientsRepository extends JpaRepository<TypeClients, Long> {

}
