package com.ai.crud.agents;

import com.ai.crud.model.GeneratedCRUD;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeGeneratorAgent {

    private static final Logger log = LoggerFactory.getLogger(CodeGeneratorAgent.class);

    @Autowired
    private ChatLanguageModel codingModel;

    @Autowired
    private RAGContextAgent ragAgent;

    public GeneratedCRUD generate(String entityDescription) {
        return generate(entityDescription, true);
    }

    public GeneratedCRUD generate(String entityDescription, boolean useRag) {
        String context = useRag ? ragAgent.retrieveContext(entityDescription) : "";
        String prompt = buildPrompt(context, entityDescription);
        String response = codingModel.generate(prompt);

        GeneratedCRUD result = parseResponse(response);
        result.setRawResponse(response);
        return result;
    }

    private String buildPrompt(String context, String entityDescription) {
        String contextSection = context.isBlank()
                ? "(No project context provided — use standard Spring Boot conventions)"
                : context;

        return """
                You are a senior Java developer. Use EXACTLY these project conventions:
                
                %s
                
                Generate a complete Spring Boot CRUD implementation for:
                %s
                
                IMPORTANT RULES:
                - Use package com.ai.crud for all classes
                - Use jakarta.persistence (not javax.persistence)
                - Return ONLY valid Java code, no explanation text
                - Separate each file with its exact header marker
                - Each file must be a complete, compilable Java class
                
                Return exactly four files using these exact headers:
                === Entity.java ===
                === Repository.java ===
                === Service.java ===
                === Controller.java ===
                """.formatted(contextSection, entityDescription);
    }

    private GeneratedCRUD parseResponse(String response) {
        GeneratedCRUD result = new GeneratedCRUD();
        result.setEntityName(extractEntityName(response));
        result.setEntityCode(extractSection(response, "Entity.java"));
        result.setRepositoryCode(extractSection(response, "Repository.java"));
        result.setServiceCode(extractSection(response, "Service.java"));
        result.setControllerCode(extractSection(response, "Controller.java"));
        return result;
    }

    private String extractSection(String response, String marker) {
        String startMarker = "=== " + marker + " ===";
        int start = response.indexOf(startMarker);
        if (start == -1) return "";
        start += startMarker.length();
        int end = response.indexOf("===", start);
        if (end == -1) end = response.length();
        return response.substring(start, end).trim();
    }

    private String extractEntityName(String response) {
        String entitySection = extractSection(response, "Entity.java");
        if (entitySection.isBlank()) return "Unknown";
        int classIdx = entitySection.indexOf("public class ");
        if (classIdx == -1) return "Unknown";
        int nameStart = classIdx + "public class ".length();
        int nameEnd = entitySection.indexOf(" ", nameStart);
        if (nameEnd == -1) nameEnd = entitySection.indexOf("{", nameStart);
        if (nameEnd == -1) return "Unknown";
        return entitySection.substring(nameStart, nameEnd).trim();
    }
}