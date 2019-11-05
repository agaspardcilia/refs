package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.SourceTransmissions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SourceTransmissions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SourceTransmissionsRepository extends JpaRepository<SourceTransmissions, Long> {

}
