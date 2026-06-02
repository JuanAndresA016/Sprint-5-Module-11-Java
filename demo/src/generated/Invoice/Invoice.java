```java
package com.ai.crud.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String invoiceNumber;
    private LocalDate issueDate;
    private BigDecimal total;
    private Boolean isPaid;

    // Getters and Setters
}
```