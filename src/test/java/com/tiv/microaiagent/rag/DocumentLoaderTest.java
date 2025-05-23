package com.tiv.microaiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DocumentLoaderTest {

    @Resource
    private DocumentLoader documentLoader;

    // option + enter
    @Test
    void loadDocuments() {
        documentLoader.loadDocuments();
    }
}