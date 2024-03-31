package system.dust.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import system.dust.domain.Inspection;
import system.dust.repository.InspectionRepository;

/**
 * PM10과 PM2.5의 값이 0일때 측정소 점검 내역 저장을 처리하는 Service
 * */

@Service
@AllArgsConstructor
public class InspectionService {

    private final InspectionRepository inspectionRepository;

    @Transactional
    public double parseDoubleSafely(String pm, String place, String date, String pmType) {
        if (pm == null) {
            String content = String.format("날짜: %s / %s 측정소 점검이 있던 날입니다.", date, pmType);

            Inspection inspec = new Inspection();
            inspec.setPlace(place);
            inspec.setContent(content);

            inspectionRepository.save(inspec);

            return 0.0;
        }
        try {
            return Double.parseDouble(pm);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
