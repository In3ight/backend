package kr.co.suitcarrier.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.suitcarrier.web.entity.User;
import kr.co.suitcarrier.web.entity.post.Post;
import kr.co.suitcarrier.web.entity.post.Review;
import lombok.*;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
@Schema(description = "리뷰 생성")
public class ReviewCreateRequestDto {
    private String content;

    public Review toEntity(User user, Post post) {
        return Review.builder()
                .user(user)
                .post(post)
                .content(content).build();
    }
}
