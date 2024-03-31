package system.dust.service;

import org.springframework.stereotype.Service;

/**
 * 경보 단계를 결정하는 Service
 * */

@Service
public class AlertLevelDetermineService {

    public String determineAlertLevel(double pm10Average, double pm25Average) {
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
}
