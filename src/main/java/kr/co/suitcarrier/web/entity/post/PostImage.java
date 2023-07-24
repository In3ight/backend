package kr.co.suitcarrier.web.entity.post;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "post_image")
public class PostImage {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @Column(name="thumnail", nullable = false)
    private String thumnail;

    @Column(name="image_url", nullable = false)
    private String imageUrl;

    @Builder
    public PostImage(Post post, String uri) {
        this.post = post;
        this.imageUrl = uri;
        this.thumnail = uri;
    }
}
