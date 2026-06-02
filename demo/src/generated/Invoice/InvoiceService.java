```java
package com.ai.crud.service;

import com.ai.crud.entity.Invoice;
import com.ai.crud.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public Iterable<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    public Invoice findById(Long id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    public Invoice create(Invoice invoice) {
        invoice.setId(UUID.randomUUID().toString().replace("-", ""));
        return invoiceRepository.save(invoice);
    }

    @Transactional
    public void update(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    public void deleteById(Long id) {
        invoiceRepository.deleteById(id);
    }
}
```