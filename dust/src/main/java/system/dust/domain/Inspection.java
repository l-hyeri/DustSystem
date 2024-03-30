package system.dust.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * 측정소별 점검 내역 Entity
 * */

@Entity
@Getter
@Setter
public class Inspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long num;   // 측정 번호

    private String place;   // 측정소명

    private String content; // 점검 내용 (날짜 + 점검중인 미세먼지 종류 + 내용)
}
