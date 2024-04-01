package system.dust.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import system.dust.domain.Inspection;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Long> {

}
