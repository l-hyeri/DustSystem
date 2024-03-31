package system.dust.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import system.dust.domain.Measurements;
import system.dust.repository.SendAlertRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebHookService {

    @Value("${webhook.url}")
    private String webhookUrl;  // 데이터를 전달하기 위해 사용

    private final RestTemplate restTemplate;
    private final SendAlertRepository sendAlertRepository;

    public void sendWebHook(String alertLevel) {    // 발신

        Measurements measurement = sendAlertRepository.findBySteps(alertLevel);

        if (measurement != null) {

            Map<String, Object> notification = new HashMap<>();
            notification.put("grade", measurement.getGrade());
            notification.put("steps", measurement.getSteps());
            notification.put("message", measurement.getMessage());

            System.out.println("발신 데이터: " + notification);

            restTemplate.postForObject(webhookUrl, notification, String.class);
        }
    }

}

