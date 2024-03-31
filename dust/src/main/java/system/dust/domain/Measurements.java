package system.dust.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * 경보 단계 등급표 Entity
 * */

@Entity
@Getter
@Setter
public class Measurements {

    @Id
    private int grade;  // 등급

    private String steps;   // 경보단계

    private String message; // 설명
}
