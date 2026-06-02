```java
package com.ai.crud.service;

import com.ai.crud.entity.Invoice;
import com.ai.crud.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(invoiceRepository);
    }

    @Test
    public void givenValidId_whenFindById_thenReturnEntity() {
        // Arrange
        Long validId = 1L;
        Invoice expectedInvoice = new Invoice();
        expectedInvoice.setId("valid-id");
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(expectedInvoice);
        given(invoiceRepository.findById(validId)).willReturn(Optional.of(expectedInvoice));

        // Act
        Invoice actualInvoice = invoiceService.findById(validId);

        // Assert
        assertEquals(expectedInvoice, actualInvoice);
    }

    @Test
    public void givenNullInput_whenFindById_thenReturnNull() {
        // Arrange
        Long invalidId = null;
        List<Invoice> invoices = new ArrayList<>();
        given(invoiceRepository.findById(invalidId)).willReturn(Optional.empty());

        // Act
        Invoice actualInvoice = invoiceService.findById(invalidId);

        // Assert
        assertNull(actualInvoice);
    }

    @Test
    public void givenValidInput_whenCreate_thenReturnCreatedEntity() {
        // Arrange
        Long validId = 1L;
        Invoice expectedInvoice = new Invoice();
        expectedInvoice.setId("valid-id");
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(expectedInvoice);
        given(invoiceRepository.save(any(Invoice.class))).willReturn(expectedInvoice);

        // Act
        Invoice actualInvoice = invoiceService.create(new Invoice());

        // Assert
        assertEquals(expectedInvoice, actualInvoice);
    }

    @Test
    public void givenNullInput_whenCreate_thenReturnNull() {
        // Arrange
        List<Invoice> invoices = new ArrayList<>();
        given(invoiceRepository.save(any(Invoice.class))).willReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> invoiceService.create(null));
    }

    @Test
    public void givenValidInput_whenUpdate_thenReturnUpdatedEntity() {
        // Arrange
        Long validId = 1L;
        Invoice expectedInvoice = new Invoice();
        expectedInvoice.setId("valid-id");
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(expectedInvoice);
        given(invoiceRepository.save(any(Invoice.class))).willReturn(expectedInvoice);

        // Act
        invoiceService.update(new Invoice());

        // Assert
        verify(invoiceRepository).save(any(Invoice.class));
    }

    @Test
    public void givenValidInput_whenDeleteById_thenReturnTrue() {
        // Arrange
        Long validId = 1L;
        List<Invoice> invoices = new ArrayList<>();
        given(invoiceRepository.existsById(validId)).willReturn(true);

        // Act
        boolean result = invoiceService.deleteById(validId);

        // Assert
        assertTrue(result);
    }

    @Test
    public void givenInvalidInput_whenDeleteById_thenReturnFalse() {
        // Arrange
        Long invalidId = null;
        List<Invoice> invoices = new ArrayList<>();
        given(invoiceRepository.existsById(invalidId)).willReturn(false);

        // Act & Assert
        boolean result = invoiceService.deleteById(invalidId);
        assertFalse(result);
    }
}
```