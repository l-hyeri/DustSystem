package system.dust.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import system.dust.domain.Alerts;

@Repository
@RequiredArgsConstructor
public class AlertsRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Alerts alerts) {
        em.persist(alerts);
    }
}
