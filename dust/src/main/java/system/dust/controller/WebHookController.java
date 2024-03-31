package system.dust.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WebHookController {


    @PostMapping("/webhook")
    public ResponseEntity<String> receiveWebHook(@RequestBody Map<String, Object> notification) {   // 수신

        System.out.println("수신 데이터: " + notification);

        // 처리 성공 응답 보내기
        return new ResponseEntity<>(notification+ " / 데이터 수신", HttpStatus.OK);
    }

}
