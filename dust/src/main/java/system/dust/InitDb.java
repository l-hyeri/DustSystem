package system.dust;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import system.dust.domain.Measurements;

import java.util.Arrays;
import java.util.List;

/**
 * 프로그램 실행 전 경보단계 등급표 관련 데이터를 넣기 위한 클래스
 * */

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        List<MeasurementData> measurementDataList = Arrays.asList(
                new MeasurementData(1, "초미세먼지 경보"),
                new MeasurementData(2, "미세먼지 경보"),
                new MeasurementData(3, "초미세먼지 주의보"),
                new MeasurementData(4, "미세먼지 주의보")
        );
        measurementDataList.forEach(data -> initService.dbInit(data.grade, data.steps));
    }
    private static class MeasurementData {
        int grade;
        String steps;

        MeasurementData(int grade, String steps) {
            this.grade = grade;
            this.steps = steps;
        }
    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit(int grade, String steps) {
            Measurements m = createMeasure(grade, steps);
            em.persist(m);
        }


        private Measurements createMeasure(int grade, String steps) {
            Measurements m = new Measurements();
            m.setGrade(grade);
            m.setSteps(steps);

            return m;
        }
    }
}
