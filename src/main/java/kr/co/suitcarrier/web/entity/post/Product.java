package kr.co.suitcarrier.web.entity.post;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="color")
    private String color;

    @Column(name="size")
    private String size;

    @Column(name="name")
    private String name;

    @Column(name="brand")
    private String brand;

    @Builder
    public Product(String color, String size, String name, String brand) {
        this.color = color;
        this.size = size;
        this.name = name;
        this.brand = brand;
    }

    @Builder
    public Product(String color, String size, String name, String brand) {
        this.color = color;
        this.size = size;
        this.name = name;
        this.brand = brand;
    }

    @PrePersist
    public void autofill() {
        this.setUuid(UUID.randomUUID().toString());
    }
}
