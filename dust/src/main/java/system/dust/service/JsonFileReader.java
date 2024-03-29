package system.dust.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import system.dust.domain.AirInform;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Json파일을 읽어오는 클래스
 */
public class JsonFileReader {

    public List<AirInform> readJsonData(String resourcePath) {

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<AirInform>> typeReference = new TypeReference<>() {
        };

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalStateException("Resource not found: " + resourcePath);
            }
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON", e);
        }
    }
}
