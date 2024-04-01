package system.dust.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import system.dust.domain.Alerts;

import java.util.List;

/**
 * DB에 경보 발령 기준이 충족된 데이터를 담기 위해 사용하는 repository
 * */

@Repository
public interface AlertsRepository extends JpaRepository<Alerts, Long> {

    List<Alerts> findAllByOrderByTimeAsc(); // 시간 기준 오름차순으로 모든 Alerts 찾기

}
