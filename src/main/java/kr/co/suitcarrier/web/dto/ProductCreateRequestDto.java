package kr.co.suitcarrier.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.suitcarrier.web.entity.post.Post;
import kr.co.suitcarrier.web.entity.post.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
@Builder
@Schema(description = "상품 생성")
public class ProductCreateRequestDto {
    private String color;
    private String size;
    private String name;
    private String brand;

    @Builder
    public Product toEntity() {
        return Product.builder()
                .color(color)
                .size(size)
                .name(name)
                .brand(brand).build();
    }


}
