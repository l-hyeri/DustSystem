package system.dust.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import system.dust.domain.Measurements;

@Repository
public interface InitDbRepository extends JpaRepository<Measurements, Long> {

}