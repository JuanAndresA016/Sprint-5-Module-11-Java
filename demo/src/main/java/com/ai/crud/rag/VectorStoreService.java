package com.ai.crud.rag;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class VectorStoreService {

    private static final Logger log = LoggerFactory.getLogger(VectorStoreService.class);
    private static final Path STORE_FILE = Paths.get("vector-store.json");

    @Autowired
    private EmbeddingStore<TextSegment> embeddingStore;

    @PreDestroy
    public void saveOnShutdown() {
        if (embeddingStore instanceof InMemoryEmbeddingStore<?> inMemoryStore) {
            try {
                String json = ((InMemoryEmbeddingStore<TextSegment>) inMemoryStore).serializeToJson();
                Files.writeString(STORE_FILE, json);
                log.info("Vector store saved to {}", STORE_FILE.toAbsolutePath());
            } catch (IOException e) {
                log.error("Failed to save vector store", e);
            }
        }
    }

    public boolean loadFromDisk() {
        if (!Files.exists(STORE_FILE)) {
            log.info("No persisted vector store found. Starting fresh.");
            return false;
        }
        try {
            String json = Files.readString(STORE_FILE);
            InMemoryEmbeddingStore.fromJson(json);
            log.info("Vector store loaded from {}", STORE_FILE.toAbsolutePath());
            return true;
        } catch (IOException e) {
            log.error("Failed to load vector store", e);
            return false;
        }
    }
}