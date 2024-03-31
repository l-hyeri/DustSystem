package system.dust.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import system.dust.domain.Measurements;

@Repository
@RequiredArgsConstructor
public class InitDbRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Measurements measurements) {
        em.persist(measurements);
    }
}