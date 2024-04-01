package system.dust.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import system.dust.domain.Alerts;

import java.util.List;

@Repository
public interface AlertsRepository extends JpaRepository<Alerts, Long> {

    List<Alerts> findAllByOrderByTimeAsc(); // 시간 기준 오름차순으로 모든 Alerts 찾기

}
