package kr.co.suitcarrier.web.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.suitcarrier.web.entity.post.Post;
import kr.co.suitcarrier.web.entity.post.PostState;
import kr.co.suitcarrier.web.entity.post.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
@Schema(description = "게시글 조회(카드형)")
public class PostCardResponseDto {
    String title;
    String description;
    int price;
    int additionalPrice;

    @Builder
    public PostCardResponseDto(Post post) {
        PostState postState = post.getPostState();
        Product product = post.getProduct();

        this.title = post.getTitle();
        this.description = post.getDescription();
        this.price = post.getPrice();
        this.additionalPrice = post.getAdditionalPrice();
    }
}