package kr.co.suitcarrier.web.entity.post;

import jakarta.persistence.*;
import kr.co.suitcarrier.web.entity.BaseTimeEntity;
import kr.co.suitcarrier.web.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "post")
public class Post extends BaseTimeEntity {
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

    @Column(name="price", nullable = false)
    private int price;

    @Column(name="is_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted;

    @Column(name="additional_price", nullable = false)
    private int additionalPrice;

    @Column(name="uuid", unique = true)
    private String uuid;

    @Builder
    public Post(String title, String description, int price, int additionalPrice, User user, PostState postState, Product product) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.additionalPrice = additionalPrice;
        this.user = user;
        this.postState = postState;
        this.product = product;
    }
    @PrePersist
    public void autofill() {
        this.setUuid(UUID.randomUUID().toString());
    }
}
