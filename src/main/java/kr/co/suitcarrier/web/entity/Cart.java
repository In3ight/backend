package kr.co.suitcarrier.web.entity;

import jakarta.persistence.*;
import kr.co.suitcarrier.web.entity.post.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "cart")
public class Cart {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @Column(name="rent_date")
    private LocalDateTime rentDate;

    @Column(name="return_date")
    private LocalDateTime returnDate;

    @Column(name="rent_possible", nullable = false)
    private int rentPossible;

    @Column(name="uuid", unique = true)
    private String uuid;

    @PrePersist
    public void autofill() {
        this.setUuid(UUID.randomUUID().toString());
    }
}
