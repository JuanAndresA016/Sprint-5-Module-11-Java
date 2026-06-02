```java
package com.ai.crud.entity;

import jakarta.persistence.*;

@Entity(name = "products")
@Table(name = "product_items")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, length = 255)
    private String category;

    @Column(nullable = false)
    private Integer stockQuantity;

    // Getters and Setters
}
```