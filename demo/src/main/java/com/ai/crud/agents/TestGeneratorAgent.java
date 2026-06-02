package com.ai.crud.agents;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestGeneratorAgent {

    private static final Logger log = LoggerFactory.getLogger(TestGeneratorAgent.class);

    @Autowired
    private ChatLanguageModel codingModel;

    public String generateTests(String javaSourceCode, String className) {
        log.debug("TestGeneratorAgent: Generating tests for: {}", className);
        String response = codingModel.generate(buildPrompt(javaSourceCode, className));
        log.debug("TestGeneratorAgent: Done ({} chars)", response.length());
        return response;
    }

    private String buildPrompt(String javaSourceCode, String className) {
        return """
                You are an expert in Java testing. Generate comprehensive JUnit 5 tests
                for the following class. Requirements:
                
                - Use @ExtendWith(MockitoExtension.class)
                - Mock all dependencies with @Mock
                - Inject mocks with @InjectMocks
                - Cover: happy path, null inputs, edge cases, exception scenarios
                - Use descriptive test method names with the given_when_then pattern
                  e.g.: givenValidId_whenFindById_thenReturnEntity()
                - Minimum 5 test methods for Service classes
                - Use package com.ai.crud for the test class
                - Import: org.junit.jupiter.api.*, org.mockito.*, org.mockito.junit.jupiter.*
                - Use assertThrows() for exception scenarios
                - Return ONLY valid Java code, no explanation
                
                Class to test (class name: %s):
                %s
                """.formatted(className, javaSourceCode);
    }
}