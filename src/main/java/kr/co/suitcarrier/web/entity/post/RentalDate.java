package kr.co.suitcarrier.web.entity.post;

import jakarta.persistence.*;
import kr.co.suitcarrier.web.entity.post.Post;
import lombok.Getter;

@Entity
@Getter
@Table(name = "rental_date")
public class RentalDate {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @Column(name="start_at")
    private String startAt;

    @Column(name="end_at")
    private String endAt;
}
