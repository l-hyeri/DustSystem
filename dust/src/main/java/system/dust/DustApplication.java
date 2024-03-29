package system.dust;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import system.dust.domain.AirInform;
import system.dust.service.JsonFileReader;

import java.util.List;

@SpringBootApplication
public class DustApplication {

    public static void main(String[] args) {
        SpringApplication.run(DustApplication.class, args);

        JsonFileReader reader = new JsonFileReader();
        List<AirInform> informs = reader.readJsonData("package.json");
    }

}
