package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ключ будет автоматически увеличиваться
    private Long id;

    @Column(name = "PRODUCT_NAME", length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "PRODUCT_PRICE", columnDefinition = "Decimal(10,2) default '0.00' ", nullable = false)
    private BigDecimal price;

    @Column(name = "PRODUCT_IS_ACTIVE", columnDefinition = "Boolean default 'true' ", nullable = false)
    private boolean isActive;

    public Product(String name, BigDecimal price, boolean isActive) {
        this.name = name;
        this.price = price;
        this.isActive = isActive;
    }
}