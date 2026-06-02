package com.ai.crud.agents;

import com.ai.crud.model.GeneratedCRUD;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CodeGeneratorAgentTest {

    @Mock
    private ChatLanguageModel codingModel;

    @Mock
    private RAGContextAgent ragAgent;

    @InjectMocks
    private CodeGeneratorAgent codeGeneratorAgent;

    @Test
    public void givenValidDescription_whenGenerateWithRag_thenReturnGeneratedCRUD() {
        when(ragAgent.retrieveContext(anyString())).thenReturn("some context");
        when(codingModel.generate(anyString())).thenReturn(
            "=== Entity.java ===\npublic class Product {}\n" +
            "=== Repository.java ===\npublic interface ProductRepository {}\n" +
            "=== Service.java ===\npublic class ProductService {}\n" +
            "=== Controller.java ===\npublic class ProductController {}"
        );

        GeneratedCRUD result = codeGeneratorAgent.generate("A Product with name and price");

        assertNotNull(result);
        verify(ragAgent, times(1)).retrieveContext(anyString());
        verify(codingModel, times(1)).generate(anyString());
    }

    @Test
    public void givenValidDescription_whenGenerateWithoutRag_thenSkipRagContext() {
        when(codingModel.generate(anyString())).thenReturn(
            "=== Entity.java ===\npublic class Product {}\n" +
            "=== Repository.java ===\npublic interface ProductRepository {}\n" +
            "=== Service.java ===\npublic class ProductService {}\n" +
            "=== Controller.java ===\npublic class ProductController {}"
        );

        GeneratedCRUD result = codeGeneratorAgent.generate("A Product with name and price", false);

        assertNotNull(result);
        verify(ragAgent, never()).retrieveContext(anyString());
    }

    @Test
    public void givenEmptyResponse_whenGenerate_thenReturnUnknownEntityName() {
        when(ragAgent.retrieveContext(anyString())).thenReturn("");
        when(codingModel.generate(anyString())).thenReturn("");

        GeneratedCRUD result = codeGeneratorAgent.generate("A Product");

        assertEquals("Unknown", result.getEntityName());
    }

    @Test
    public void givenValidResponse_whenGenerate_thenExtractEntityName() {
        when(ragAgent.retrieveContext(anyString())).thenReturn("context");
        when(codingModel.generate(anyString())).thenReturn(
            "=== Entity.java ===\npublic class Invoice {}\n" +
            "=== Repository.java ===\npublic interface InvoiceRepository {}\n" +
            "=== Service.java ===\npublic class InvoiceService {}\n" +
            "=== Controller.java ===\npublic class InvoiceController {}"
        );

        GeneratedCRUD result = codeGeneratorAgent.generate("An Invoice");

        assertEquals("Invoice", result.getEntityName());
    }

    @Test
    public void givenNullDescription_whenGenerate_thenModelStillCalled() {
        when(ragAgent.retrieveContext(null)).thenReturn("");
        when(codingModel.generate(anyString())).thenReturn("");

        GeneratedCRUD result = codeGeneratorAgent.generate(null);

        assertNotNull(result);
        verify(codingModel, times(1)).generate(anyString());
    }
}