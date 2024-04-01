package system.dust.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import system.dust.domain.Measurements;

/**
 * 데이터베이스에 경보단계 등급표 데이터를 담기 위해 사용하는 repository
 * */
@Repository
public interface InitDbRepository extends JpaRepository<Measurements, Long> {

}