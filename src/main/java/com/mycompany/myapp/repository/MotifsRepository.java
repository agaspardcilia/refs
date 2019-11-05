package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.Motifs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Motifs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotifsRepository extends JpaRepository<Motifs, Long> {

}
