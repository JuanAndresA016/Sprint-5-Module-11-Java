package com.ai.crud.config;

import com.ai.crud.agents.CodeGeneratorAgent;
import com.ai.crud.agents.OrchestratorAgent;
import com.ai.crud.agents.RAGContextAgent;
import com.ai.crud.model.CRUDResult;
import com.ai.crud.model.GeneratedCRUD;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/generator")
public class GeneratorController {

    private static final Logger log = LoggerFactory.getLogger(GeneratorController.class);

    @Autowired
    private OrchestratorAgent orchestrator;

    @Autowired
    private CodeGeneratorAgent codeAgent;

    @Autowired
    private RAGContextAgent ragAgent;

    @PostMapping("/generate")
    public ResponseEntity<CRUDResult> generate(@RequestBody GenerateRequest req) {
        if (req.getEntityDescription() == null || req.getEntityDescription().isBlank()) {
            return ResponseEntity.badRequest().body(CRUDResult.error("entityDescription is required"));
        }
        CRUDResult result = orchestrator.generateComplete(req.getEntityDescription());
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.internalServerError().body(result);
    }

    @PostMapping("/generate-no-rag")
    public ResponseEntity<GeneratedCRUD> generateWithoutRag(@RequestBody GenerateRequest req) {
        if (req.getEntityDescription() == null || req.getEntityDescription().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(codeAgent.generate(req.getEntityDescription(), false));
    }

    @GetMapping("/rag-context")
    public ResponseEntity<Map<String, Object>> getRagContext(@RequestParam String description) {
        List<EmbeddingMatch<TextSegment>> matches = ragAgent.retrieveMatches(description);
        List<Map<String, Object>> segments = matches.stream()
                .map(m -> Map.of("score", m.score(), "text", m.embedded().text()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(Map.of(
                "description", description,
                "segmentsRetrieved", segments.size(),
                "segments", segments
        ));
    }

    public static class GenerateRequest {
        private String entityDescription;
        public String getEntityDescription() { return entityDescription; }
        public void setEntityDescription(String entityDescription) { this.entityDescription = entityDescription; }
    }
}