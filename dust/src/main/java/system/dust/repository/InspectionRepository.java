package system.dust.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import system.dust.domain.Inspection;

@Repository
@RequiredArgsConstructor
public class InspectionRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Inspection inspec) {
        em.persist(inspec);
    }
}
