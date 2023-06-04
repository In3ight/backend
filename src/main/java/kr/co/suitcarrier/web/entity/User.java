package kr.co.suitcarrier.web.entity;

import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import lombok.Getter;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
public class User {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="uuid", unique = true, nullable = false)
    @GenericGenerator(name = "uuid4", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    @Column(name="role", nullable = false)
    @ColumnDefault("'USER'")
    @Enumerated(EnumType.STRING)
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

    @Column(name="created_at", nullable = false)
    private String createdAt;

    @Column(name="updated_at", nullable = false)
    private String updatedAt;

    @Column(name="enabled", nullable = false)
    @ColumnDefault("true")
    private boolean enabled;

    public enum Role {
        ADMIN, USER
    }
    
}
