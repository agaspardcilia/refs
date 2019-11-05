package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.CourtierPartenaireDelegataires;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CourtierPartenaireDelegataires entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourtierPartenaireDelegatairesRepository extends JpaRepository<CourtierPartenaireDelegataires, Long> {

}
