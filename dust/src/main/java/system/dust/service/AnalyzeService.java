package system.dust.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import system.dust.domain.AirInform;
import system.dust.domain.Alerts;
import system.dust.repository.AlertsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * [2024.03.29]
 * Json에서 데이터를 읽어와 경보 발령 기준이 충족하는지 확인하도록 구현 (PM10)
 * 경보 발령 기준 추가 및 미세먼지, 초미세먼지 구분 구현
 * NullPointException 처리
 * [2024.03.30]
 * service와 repository 연결
 * 측정소별 점검 내역 db저장 구현
 * [2024.03.31]
 * 메소드 분리 (유지보수 고려)
 * 측정소 점검 내역 저장 코드 Service로 분리 (단일 책임 원칙 고려)
 * 경보 단계 지정 코드 Service로 분리 (단일 책임 원칙 고려)
 * [2024.04.01]
 * 웹훅 구현
 */

@Service
@AllArgsConstructor
public class AnalyzeService {

    private final AlertsRepository alertsRepository;
    private final InspectionService inspectionService;
    private final AlertLevelDetermineService aldService;
    private final WebHookService webHookService;

    @Transactional
    public void processAlerts(List<AirInform> informs) {

        Map<String, Double> sumPM10 = new HashMap<>();  // 측정소와 날짜 조합에 대한 PM10농도 누적합계
        Map<String, Double> sumPM25 = new HashMap<>();
        Map<String, Integer> count = new HashMap<>();
        Map<String, LocalDateTime> firstAlertTime = new HashMap<>(); // 첫 경보 발령 시간 기록

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");

        for (AirInform i : informs) {
            // 측정소 이름과 측정 날짜를 가져옴 (substring은 연-월-일만 가져오기 위함.)
            String key = i.getPlace() + "_" + i.getDate().substring(0, 10);
            LocalDateTime dateTime = LocalDateTime.parse(i.getDate(), formatter);

            double pm10Value = inspectionService.parseDoubleSafely(i.getPM10(), i.getPlace(), i.getDate(), "PM10");
            double pm25Value = inspectionService.parseDoubleSafely(i.getPM2_5(), i.getPlace(), i.getDate(), "PM2.5");

            sumPM10.merge(key, pm10Value, Double::sum);
            sumPM25.merge(key, pm25Value, Double::sum);
            count.merge(key, 1, Integer::sum);  // 측정횟수 저장

            double pm10Average = sumPM10.get(key) / count.get(key); // 평균 PM10 농도 계산
            double pm25Average = sumPM25.get(key) / count.get(key);

            String alertLevel = aldService.determineAlertLevel(pm10Average, pm25Average);
            checkAlerts(key, dateTime, firstAlertTime, alertLevel, i.getPlace(), i.getDate());

        }
    }

    private void checkAlerts(String key, LocalDateTime dateTime,
                              Map<String, LocalDateTime> firstAlertTime,
                              String alertLevel, String place, String date) {   // 경보 알림 조건 확인 메서드
        if (!"경보 없음".equals(alertLevel)) {
            firstAlertTime.putIfAbsent(key, dateTime); // 경보 조건이 만족된 첫 시간 기록

            // 현재 시간과 첫 경보 발령 시간의 차이 계산
            long hoursBetween = java.time.Duration.between(firstAlertTime.get(key), dateTime).toHours();

            if (hoursBetween >= 2) {  // 2시간 이상의 조건의 만족할 경우
                saveAlert(place, alertLevel, date);
            }
        } else {
            // 현재 경보 상태가 아니면 초기화
            firstAlertTime.remove(key);
        }
    }

    private void saveAlert(String place, String alertLevel, String alertTime) { // 경보 알림 발생 시 DB저장 메서드
        Alerts alerts = new Alerts();
        alerts.setPlace(place);
        alerts.setSteps(alertLevel);
        alerts.setTime(alertTime);

        alertsRepository.save(alerts);
        webHookService.sendWebHook(alertLevel);
    }
}
