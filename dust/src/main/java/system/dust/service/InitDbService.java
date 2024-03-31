package system.dust.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import system.dust.domain.Measurements;
import system.dust.repository.InitDbRepository;

@Service
@AllArgsConstructor
public class InitDbService {

    private final InitDbRepository initDbRepository;

    @Transactional
    public void saveMeasurements(int grade, String steps, String message) {

        Measurements m = new Measurements();
        m.setGrade(grade);
        m.setSteps(steps);
        m.setMessage(message);

        initDbRepository.save(m);
    }
}
