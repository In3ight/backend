package kr.co.suitcarrier.web.entity.post;

import jakarta.persistence.*;
import kr.co.suitcarrier.web.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "post_state")
public class PostState {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="state")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'LENT_POSSIBLE'")
    private State state;

    public enum State {
    // 대여중, 대여가능, 대여불가
        LENT, LENT_POSSIBLE, LENT_IMPOSSIBLE
    }

    @Builder
    public PostState(State state) {
        this.state = state;
    }



}
