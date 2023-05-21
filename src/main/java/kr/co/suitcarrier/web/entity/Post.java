package kr.co.suitcarrier.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "post")
public class Post {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "post_state_id", referencedColumnName = "id")
    private PostState postState;


    @Column(name="title", nullable = false)
    private String title;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="default_price", nullable = false)
    private String defaultPrice;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name="is_deleted", nullable = false)
    private int isDeleted;

    @Column(name="additional_price", nullable = false)
    private String additionalPrice;

    @Column(name="uuid", unique = true, nullable = false)
    @GenericGenerator(name = "uuid4", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;
}
