package com.ai.crud.agents;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RAGContextAgent {

    private static final Logger log = LoggerFactory.getLogger(RAGContextAgent.class);
    private static final int TOP_K = 5;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private EmbeddingStore<TextSegment> embeddingStore;

    public String retrieveContext(String entityDescription) {

        log.info("========== RAG RETRIEVAL START ==========");
        log.info("Query: {}", entityDescription);

        Embedding queryEmbedding = embeddingModel
                .embed(entityDescription)
                .content();

        List<EmbeddingMatch<TextSegment>> matches =
                embeddingStore.findRelevant(queryEmbedding, TOP_K);

        log.info("Retrieved {} segments", matches.size());

        if (matches.isEmpty()) {
            log.warn("No relevant RAG segments found.");
            log.info("========== RAG RETRIEVAL END ==========");
            return "";
        }

        int index = 1;
        for (EmbeddingMatch<TextSegment> match : matches) {

            String text = match.embedded().text();

            log.info("--------------------------------");
            log.info("Segment #{}", index++);
            log.info("Score: {}", match.score());
            log.info("Content:\n{}", text);
        }

        log.info("========== RAG RETRIEVAL END ==========");

        return matches.stream()
                .map(match -> match.embedded().text())
                .collect(Collectors.joining("\n\n---\n\n"));
    }

    public List<EmbeddingMatch<TextSegment>> retrieveMatches(String entityDescription) {

        Embedding queryEmbedding = embeddingModel
                .embed(entityDescription)
                .content();

        return embeddingStore.findRelevant(queryEmbedding, TOP_K);
    }
}