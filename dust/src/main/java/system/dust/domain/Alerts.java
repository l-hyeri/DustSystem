package system.dust.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * 경보 발령시 데이터 베이스에 저장하기 위한 Enttiy
 **/

@Entity
@Getter
@Setter
public class Alerts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long test_id;   // 수정 필요 : 측정소 (구별)

    private String steps;   // 경보 단계

    private String time;    // 발령 시간
}
