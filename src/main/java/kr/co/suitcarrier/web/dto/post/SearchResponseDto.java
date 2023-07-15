package kr.co.suitcarrier.web.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Schema(description = "검색 결과")
public class SearchResponseDto {
    private List<PostCardResponseDto> responseDtos;

    @Builder
    public SearchResponseDto(List<PostCardResponseDto> postCardResponseDtos) {
        this.responseDtos = postCardResponseDtos;
    }
}
