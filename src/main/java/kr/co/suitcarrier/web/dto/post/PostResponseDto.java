package kr.co.suitcarrier.web.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.suitcarrier.web.entity.post.Post;
import kr.co.suitcarrier.web.entity.post.PostState;
import kr.co.suitcarrier.web.entity.post.Product;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
@Schema(description = "게시글 상세조회")
public class PostResponseDto {
    String title;
    String description;
    int price;
    int additionalPrice;

    String state;

    String color;
    String brand;
    String size;

    public PostResponseDto(Post post) {
        PostState postState = post.getPostState();
        Product product = post.getProduct();

        this.title = post.getTitle();
        this.description = post.getDescription();
        this.price = post.getPrice();
        this.additionalPrice = post.getAdditionalPrice();
        this.state = String.valueOf(postState.getState());
        this.color = product.getColor();
        this.brand = product.getBrand();
        this.size = product.getSize();
    }
}
