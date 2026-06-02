```java
package com.ai.crud.service;

import com.ai.crud.entity.Student;
import com.ai.crud.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Autowired
    public StudentServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidId_whenFindById_thenReturnEntity() {
        Long validId = 1L;
        Student expectedStudent = new Student();
        when(studentRepository.findById(validId)).thenReturn(Optional.of(expectedStudent));
        Student actualStudent = studentService.findById(validId);

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void givenNullInput_whenFindById_thenReturnNull() {
        Long invalidId = null;
        when(studentRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertNull(studentService.findById(invalidId));
    }

    @Test
    void givenValidId_whenDeleteById_thenReturnTrue() {
        Long validId = 1L;
        studentService.deleteById(validId);

        verify(studentRepository, times(1)).deleteById(validId);
    }

    @Test
    void givenNullInput_whenDeleteById_thenThrowException() {
        when(studentRepository.findById(any())).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> studentService.deleteById(null));
    }

    @Test
    void givenValidStudent_whenCreate_thenReturnCreatedEntity() {
        Student validStudent = new Student();
        validStudent.setName("John Doe");
        validStudent.setAge(25);
        when(studentRepository.save(any(Student.class))).thenReturn(validStudent);

        Student createdStudent = studentService.create(validStudent);

        assertEquals(validStudent, createdStudent);
    }

    @Test
    void givenNullStudent_whenCreate_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> studentService.create(null));
    }
}
```