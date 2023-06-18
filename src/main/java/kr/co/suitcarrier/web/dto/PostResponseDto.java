package kr.co.suitcarrier.web.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Setter(AccessLevel.NONE)
@Builder
@Schema(description = "게시글 조회")
public class PostResponseDto {
    private int price;

}
