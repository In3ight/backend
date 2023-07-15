package kr.co.suitcarrier.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
@Schema(description = "캐리어 검색")
public class SearchRequestDto {
    private double longitude;
    private double latitude;
    private String color;
    private String size;

    @Schema(description = "최소금액")
    private int minPrice;

    @Schema(description = "최대금액")
    private int maxPrice;

    private String brand;

}
