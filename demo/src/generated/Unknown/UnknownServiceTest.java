```java
package com.ai.crud;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
public class UnknownServiceTest {

    @Mock
    private SomeDependency someDependency;

    @InjectMocks
    private UnknownService unknownService;

    @Autowired
    public UnknownServiceTest(SomeDependency someDependency) {
        this.someDependency = someDependency;
    }

    @Test
    void givenValidId_whenFindById_thenReturnEntity() {
        Long id = 1L;
        Entity entity = new Entity();
        when(someDependency.findById(id)).thenReturn(Optional.of(entity));
        
        Optional<Entity> result = unknownService.findById(id);
        
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void givenNullId_whenFindById_thenReturnEmptyOptional() {
        Long id = null;
        when(someDependency.findById(id)).thenReturn(Optional.empty());
        
        Optional<Entity> result = unknownService.findById(id);
        
        assertFalse(result.isPresent());
    }

    @Test
    void givenValidId_whenDeleteById_thenReturnTrue() {
        Long id = 1L;
        when(someDependency.deleteById(id)).thenReturn(true);
        
        boolean result = unknownService.deleteById(id);
        
        assertTrue(result);
    }

    @Test
    void givenNullId_whenDeleteById_thenThrowIllegalArgumentException() {
        Long id = null;
        
        assertThrows(IllegalArgumentException.class, () -> unknownService.deleteById(id));
    }

    @Test
    void givenValidEntity_whenSave_thenReturnSavedEntity() {
        Entity entity = new Entity();
        when(someDependency.save(entity)).thenReturn(entity);
        
        Entity result = unknownService.save(entity);
        
        assertEquals(entity, result);
    }
}
```

Note: The `SomeDependency` class and the `Entity` class are assumed to be part of the application's architecture but were not provided in your request. Ensure that these classes exist in your project for this code to compile successfully.