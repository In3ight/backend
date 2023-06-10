package kr.co.suitcarrier.web.entity;

import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@RequiredArgsConstructor
@Table(name = "user")
public class User extends BaseTimeEntity {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="uuid", unique = true)
    private String uuid;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'USER'")
    private Role role;

    @Column(name="email", unique = true, nullable = false)
    private String email;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="nickname", nullable = false)
    private String nickname;

    @Column(name="contact", nullable = false)
    private String contact;

    @Column(name="enabled")
    @ColumnDefault("true")
    private boolean enabled;

    public enum Role {
        ADMIN, USER
    }

    @Builder
    public User(String name, String email, String nickname, String password, String contact) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.contact = contact;
    }
    
    @PrePersist
    public void autofill() {
        this.setUuid(UUID.randomUUID().toString());
    }

}
