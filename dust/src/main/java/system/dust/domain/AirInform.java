package system.dust.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirInform {

    @JsonProperty("날짜") // json에 있는 필드를 AirInform 필드와 매핑하기 위함
    private String date;

    @JsonProperty("측정소명")
    private String place;

    @JsonProperty("측정소코드")
    private String code;

    @JsonProperty("PM10")
    private String PM10;

    @JsonProperty("PM2.5")
    private String PM2_5;
}
