package system.dust.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import system.dust.domain.Alerts;
import system.dust.domain.Measurements;
import system.dust.repository.AlertsRepository;
import system.dust.repository.SendAlertRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebHookService {

    @Value("${webhook.url}")
    private String webhookUrl;  // 데이터를 전달하기 위해 사용

    private final RestTemplate restTemplate;
    private final SendAlertRepository sendAlertRepository;
    private final AlertsRepository alertsRepository;

    public void sendWebHook() {    // 발신

        List<Alerts> alertsList = alertsRepository.findAllByOrderByTimeAsc();

        for(Alerts alert:alertsList) {
            Measurements measurement = sendAlertRepository.findBySteps(alert.getSteps());

            if (measurement != null) {

                Map<String, Object> notification = new HashMap<>();

                notification.put("time", alert.getTime());  // 경보 알림 시간
                notification.put("place", alert.getPlace());    // 경보 알림 장소
                notification.put("grade", measurement.getGrade());  // 경보 등급
                notification.put("message", measurement.getMessage());  // 경보 메시지
                notification.put("steps", measurement.getSteps());  // 경보 단계

                restTemplate.postForObject(webhookUrl, notification, String.class);
            }
        }
    }

}

