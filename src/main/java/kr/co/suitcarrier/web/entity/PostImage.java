package kr.co.suitcarrier.web.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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
}
