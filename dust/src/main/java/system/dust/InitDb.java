package system.dust;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import system.dust.service.InitDbService;

import java.util.Arrays;
import java.util.List;

/**
 * 프로그램 실행 전 경보단계 등급표 관련 데이터를 넣기 위한 클래스
 */

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitDbService initDbService;

    @PostConstruct
    public void init() {
        List<MeasurementData> measurementDataList = Arrays.asList(
                new MeasurementData(1, "초미세먼지 경보", "가장 심각한 상태, 건강에 매우 해로움"),
                new MeasurementData(2, "미세먼지 경보", "건강에 매우 해로울 수 있음"),
                new MeasurementData(3, "초미세먼지 주의보", "건강에 해로울 수 있음"),
                new MeasurementData(4, "미세먼지 주의보", "건강에 약간 해로울 수 있음")
        );
        measurementDataList.forEach(data -> initDbService.saveMeasurements(data.grade, data.steps, data.message));
    }

    private static class MeasurementData {
        int grade;
        String steps;
        String message;

        MeasurementData(int grade, String steps, String message) {
            this.grade = grade;
            this.steps = steps;
            this.message = message;
        }
    }
}
