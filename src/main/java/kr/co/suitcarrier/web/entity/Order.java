package kr.co.suitcarrier.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "order")
public class Order {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "order_state_id", referencedColumnName = "id")
    private OrderState orderState;

    @Column(name="final_price", nullable = false)
    private String finalPrice;

    @Column(name="rent_date", nullable = false)
    private LocalDateTime rentDate;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name="is_deleted", nullable = false)
    private int isDeleted;

    @Column(name="uuid", unique = true, nullable = false)
    @GenericGenerator(name = "uuid4", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

}
