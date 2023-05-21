package kr.co.suitcarrier.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
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

    @Column(name="origin_price")
    private String originPrice;

    @Column(name="brand")
    private String brand;

    @Column(name="uuid", unique = true, nullable = false)
    @GenericGenerator(name = "uuid4", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

}
