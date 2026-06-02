```java
package com.ai.crud.controller;

import com.ai.crud.entity.Invoice;
import com.ai.crud.repository.InvoiceRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @GetMapping
    public Iterable<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> findById(@PathVariable Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if (invoice == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoice);
    }

    @PostMapping
    public ResponseEntity<Invoice> create(@Valid @RequestBody Invoice invoice) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        invoice.setId(uuid);
        invoiceRepository.save(invoice);
        return ResponseEntity.ok(invoice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody Invoice invoiceDetails) {
        if (!invoiceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        invoiceRepository.findById(id).ifPresent(invoice -> {
            invoice.setInvoiceNumber(invoiceDetails.getInvoiceNumber());
            invoice.setIssueDate(invoiceDetails.getIssueDate());
            invoice.setTotal(invoiceDetails.getTotal());
            invoice.setIsPaid(invoiceDetails.getIsPaid());
            invoiceRepository.save(invoice);
        });
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!invoiceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        invoiceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
```

Please ensure your Spring Boot application is configured with the necessary dependencies and annotations to support these classes.