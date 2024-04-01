package system.dust.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import system.dust.domain.Inspection;

/**
 * DB에 측정소 점검 날을 저장하기 위해 사용하는 repository
 * */

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Long> {

}
