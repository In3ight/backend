package kr.co.suitcarrier.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "찜")
@Getter
public class LikeRequestDto {

    private Long user;
    private Long post;

}
