package system.dust.service;

import system.dust.domain.AirInform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2024.03.29
 * Json에서 데이터를 읽어와 경보 발령 기준이 충족하는지 확인하도록 구현 (PM10)
 * */
public class AnalyzeService {

    public void processAlerts(List<AirInform> informs) {

        Map<String, Double> sumPM10 = new HashMap<>();  // 측정소와 날짜 조합에 대한 PM10농도 누적합계
        Map<String, Integer> count = new HashMap<>();
        Map<String, Integer> hours = new HashMap<>();   // 측정소와 날짜 조합별 발령 상태 시간 관리

        for (AirInform i : informs) {
            // 측정소 이름과 측정 날짜를 가져옴 (substring은 연-월-일만 가져오기 위함.)
            String key = i.getPlace() + "_" + i.getDate().substring(0, 10);

            double pm10Value = Double.parseDouble(i.getPM10()); // String을 double로 변환, 만약 getPM10()이 이미 double을 반환한다면 이 단계는 생략
            sumPM10.put(key, sumPM10.getOrDefault(key, 0.0) + pm10Value);
            count.put(key, count.getOrDefault(key, 0) + 1); // 측정횟수 저장

            double currentAverage = sumPM10.get(key) / count.get(key); // 평균 PM10 농도 계산

            if (currentAverage >= 20) {
                hours.putIfAbsent(key, 0); // 경보 상태가 없으면 초기화
                int time = hours.getOrDefault(key, 0) + 1;   // 기존 연속된 시간에 1시간 추가
                hours.put(key, time);


                if (time >= 2) {
                    String alertTime = i.getDate(); // 경보 발령 시간
                    System.out.println("경보 발령: " + key + " - 경보 발령 시간: " + alertTime);
                }
            }
        }
    }
}
