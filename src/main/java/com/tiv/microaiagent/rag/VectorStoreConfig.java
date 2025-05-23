package com.tiv.microaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 向量数据库配置
 */
@Configuration
public class VectorStoreConfig {

    @Resource
    private DocumentLoader documentLoader;

    @Bean
    VectorStore vectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        List<Document> documents = documentLoader.loadDocuments();
        simpleVectorStore.add(documents);
        return simpleVectorStore;
    }

}
