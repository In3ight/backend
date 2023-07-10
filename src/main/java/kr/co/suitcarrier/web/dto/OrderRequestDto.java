package kr.co.suitcarrier.web.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "찜")
@Getter
public class OrderRequestDto {

    private Integer post;
    private String finalPrice;
    private LocalDateTime rentDate;
    private LocalDateTime createdAt;

}
