package kr.co.suitcarrier.web.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.suitcarrier.web.entity.User;
import kr.co.suitcarrier.web.entity.post.Post;
import kr.co.suitcarrier.web.entity.post.PostImage;
import kr.co.suitcarrier.web.entity.post.PostState;
import kr.co.suitcarrier.web.entity.post.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Setter(AccessLevel.NONE)
@Builder
@Schema(description = "게시글 생성")
public class PostCreateRequestDto {
    List<MultipartFile> images;
    private String title;
    private String description;
    private int price;
    private int additionalPrice;

    private String color;
    private String size;
    private String name;
    private String brand;

    public Post toEntity(User user, PostState postState, Product product, PostImage image) {
        return Post.builder()
                .title(title)
                .description(description)
                .price(price)
                .additionalPrice(additionalPrice)
                .user(user)
                .postState(postState)
                .product(product).build();
    }
}
