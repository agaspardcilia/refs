package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.SousMotifs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SousMotifs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SousMotifsRepository extends JpaRepository<SousMotifs, Long> {

}
