package system.dust.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import system.dust.domain.AirInform;
import system.dust.domain.Alerts;
import system.dust.domain.Inspection;
import system.dust.repository.AlertsRepository;
import system.dust.repository.InspectionRepository;

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
 */

@Service
@AllArgsConstructor
public class AnalyzeService {

    private final AlertsRepository alertsRepository;
    private final InspectionRepository inspectionRepository;

    public static String determineAlertLevel(double pm10Average, double pm25Average) {
        // 높은 수준의 경보를 먼저 확인
        if (pm25Average >= 150) {   // 150
            return "초미세먼지 경보";
        } else if (pm10Average >= 300) { //300
            return "미세먼지 경보";
        } else if (pm25Average >= 75) {    //75
            return "초미세먼지 주의보";
        } else if (pm10Average >= 150) { // 150
            return "미세먼지 주의보";
        } else {
            return "경보 없음";
        }
    }

    // null 또는 변환 불가능한 문자열을 안전하게 double 값으로 변환하는 메소드 (nullpointException 처리)
    @Transactional
    public double parseDoubleSafely(String pm, String place, String date, String pmType) {
        if (pm == null) {
            String content = String.format("날짜: %s / %s 측정소 점검이 있던 날입니다.", date, pmType);
            System.out.println(content);
            Inspection inspec = new Inspection();
            inspec.setPlace(place);
            inspec.setContent(content);

            inspectionRepository.save(inspec);

            return 0.0;
        }
        try {
            return Double.parseDouble(pm);
        } catch (NumberFormatException e) {
            // 변환에 실패한 경우, 기본값으로 0.0을 반환
            return 0.0;
        }
    }

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

            double pm10Value = parseDoubleSafely(i.getPM10(), i.getPlace(), i.getDate(), "PM10");
            double pm25Value = parseDoubleSafely(i.getPM2_5(), i.getPlace(), i.getDate(), "PM2.5");
            sumPM10.put(key, sumPM10.getOrDefault(key, 0.0) + pm10Value);
            sumPM25.put(key, sumPM25.getOrDefault(key, 0.0) + pm25Value);
            count.put(key, count.getOrDefault(key, 0) + 1); // 측정횟수 저장

            double pm10Average = sumPM10.get(key) / count.get(key); // 평균 PM10 농도 계산
            double pm25Average = sumPM25.get(key) / count.get(key);

            String alertLevel = determineAlertLevel(pm10Average, pm25Average);
            if (!"경보 없음".equals(alertLevel)) {

                firstAlertTime.putIfAbsent(key, dateTime); // 경보 조건이 만족된 첫 시간 기록

                // 현재 시간과 첫 경보 발령 시간의 차이 계산
                long hoursBetween = java.time.Duration.between(firstAlertTime.get(key), dateTime).toHours();

                if (hoursBetween >= 2) {  // 2시간 이상의 조건의 만족할 경우

                        Alerts alerts = new Alerts();
                        alerts.setPlace(i.getPlace());
                        alerts.setSteps(alertLevel);
                        alerts.setTime(i.getDate());

                        alertsRepository.save(alerts);

                }
            } else {
                // 현재 경보 상태가 아니면 초기화
                firstAlertTime.remove(key);
            }

        }
    }
}
