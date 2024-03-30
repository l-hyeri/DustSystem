package system.dust.controller;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import system.dust.domain.AirInform;
import system.dust.service.AnalyzeService;
import system.dust.service.JsonFileReader;

import java.util.List;

/**
 * spring에서 controller 역할을 하는 클래스로
 * 해당 프로젝트에서는 json파일을 읽고 DB에 저장하도록 연결하는 역할을 하는 클래스
 * */

@Component
@AllArgsConstructor
public class Controller implements CommandLineRunner {

    private final AnalyzeService analyzeService;

    @Override
    public void run(String... args) throws Exception {
        JsonFileReader reader = new JsonFileReader();
        List<AirInform> informs = reader.readJsonData("package.json");

        analyzeService.processAlerts(informs);
    }
}
