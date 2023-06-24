package kr.co.suitcarrier.web.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.suitcarrier.web.entity.post.Post;
import lombok.*;

@Data
@Schema(description = "게시글 (카드형)")
public class PostCardResponseDto {
    private long id;
    private int price;
    private String location;
    // 추후 이미지 가져오기 추가

    public PostCardResponseDto(Post entity, String location) {
        this.id = entity.getId();
        this.price = entity.getPrice();
        this.location = location;
    }

}
