package com.ai.crud.agents;

import com.ai.crud.model.CRUDResult;
import com.ai.crud.model.GeneratedCRUD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class OrchestratorAgent {

    private static final Logger log = LoggerFactory.getLogger(OrchestratorAgent.class);

    @Autowired
    private CodeGeneratorAgent codeAgent;

    @Autowired
    private TestGeneratorAgent testAgent;

    public CRUDResult generateComplete(String entityDescription) {
        log.info("OrchestratorAgent: Starting pipeline for: '{}'", entityDescription);
        try {
            GeneratedCRUD crud = codeAgent.generate(entityDescription);
            log.info("CRUD generated for: {}", crud.getEntityName());

            String serviceTests = testAgent.generateTests(
                    crud.getServiceCode(),
                    crud.getEntityName() + "Service");

            saveGeneratedFiles(crud, serviceTests);
            updateKnowledgeBase(crud, entityDescription);

            log.info("OrchestratorAgent: Pipeline complete.");
            return new CRUDResult(crud, serviceTests);

        } catch (Exception e) {
            log.error("OrchestratorAgent: Pipeline failed", e);
            return CRUDResult.error("Generation failed: " + e.getMessage());
        }
    }

    private void saveGeneratedFiles(GeneratedCRUD crud, String serviceTests) throws IOException {
        String entityName = crud.getEntityName();
        Path outputDir = Paths.get("src", "generated", entityName);
        Files.createDirectories(outputDir);

        writeFile(outputDir.resolve(entityName + ".java"), crud.getEntityCode());
        writeFile(outputDir.resolve(entityName + "Repository.java"), crud.getRepositoryCode());
        writeFile(outputDir.resolve(entityName + "Service.java"), crud.getServiceCode());
        writeFile(outputDir.resolve(entityName + "Controller.java"), crud.getControllerCode());
        writeFile(outputDir.resolve(entityName + "ServiceTest.java"), serviceTests);
    }

    private void updateKnowledgeBase(GeneratedCRUD crud, String entityDescription) {
        try {
            Path kbFile = Paths.get("src", "main", "resources", "knowledge-base", "existing-entities.txt");
            if (Files.exists(kbFile)) {
                String entry = "\n\nExisting entity: " + crud.getEntityName() +
                        "\nDescription: " + entityDescription +
                        "\nGenerated at: " + java.time.LocalDateTime.now();
                Files.writeString(kbFile, entry, StandardOpenOption.APPEND);
                log.info("Knowledge base updated with: {}", crud.getEntityName());
            }
        } catch (IOException e) {
            log.warn("Could not update knowledge base: {}", e.getMessage());
        }
    }

    private void writeFile(Path path, String content) throws IOException {
        if (content != null && !content.isBlank()) {
            Files.writeString(path, content);
        }
    }
}