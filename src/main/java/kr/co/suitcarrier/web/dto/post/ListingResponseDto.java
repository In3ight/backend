package kr.co.suitcarrier.web.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "게시글 목록 리스팅")
public class ListingResponseDto {
    private List<PostCardResponseDto> responseDtos;

    @Builder
    public ListingResponseDto(List<PostCardResponseDto> postCardResponseDtos) {
        this.responseDtos = postCardResponseDtos;
    }
}
