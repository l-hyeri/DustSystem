package system.dust.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import system.dust.domain.Alerts;

@Repository
public interface AlertsRepository extends JpaRepository<Alerts, Long> {

}
