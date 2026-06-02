```java
package com.ai.crud.service;

import com.ai.crud.entity.Product;
import com.ai.crud.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}

package com.ai.crud.repository;

import com.ai.crud.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final static String[] PRODUCTS = {"Product1", "Product2", "Product3"};

    public Iterable<Product> findAll() {
        return java.util.Arrays.asList(new Product[]{new Product(1L, "Product1"),
                new Product(2L, "Product2"), new Product(3L, "Product3")}).iterator();
    }

    @Transactional
    public Product save(Product product) {
        return null; // Placeholder for actual implementation
    }

    @Transactional
    public void deleteById(Long id) {
        // Placeholder for actual implementation
    }
}

package com.ai.crud;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(productRepository);
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    @DisplayName("Given valid id when findById then return null")
    void givenValidId_whenFindById_thenReturnNull() {
        productService.deleteById(4L);
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.findById(4L));
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    @DisplayName("Given invalid id when findById then throw IllegalArgumentException")
    void givenInvalidId_whenFindById_thenThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.findById(null));
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    @DisplayName("Given valid product when save then return saved product")
    void givenValidProduct_whenSave_thenReturnSavedProduct() {
        Product product = new Product(4L, "New Product");
        Assertions.assertDoesNotThrow(() -> productService.save(product));
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    @DisplayName("Given valid id when findById then return found product")
    void givenValidId_whenFindById_thenReturnFoundProduct() {
        List<Product> products = productService.findAll();
        Product foundProduct = productService.findById(products.get(0).getId());
        Assertions.assertEquals(foundProduct, products.get(0));
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    @DisplayName("Given null product when save then throw IllegalArgumentException")
    void givenNullProduct_whenSave_thenThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.save(null));
    }
}
```

Please note that the `save` and `deleteById` methods in the `ProductRepository` class are placeholders for actual implementation. In a real-world scenario, these methods would interact with an external database or other storage mechanism.