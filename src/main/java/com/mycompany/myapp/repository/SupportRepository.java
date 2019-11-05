package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.Support;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Support entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupportRepository extends JpaRepository<Support, Long> {

}
