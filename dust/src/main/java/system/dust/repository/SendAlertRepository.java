package system.dust.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import system.dust.domain.Measurements;

/**
 * 경보 단계에 따른 정보를 가져오기 위한 Repository
 * */

@Repository
public interface SendAlertRepository extends JpaRepository<Measurements,String> {
    Measurements findBySteps(String steps);

}
