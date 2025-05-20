package com.tiv.microaiagent.rag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文档加载器
 */
@Data
@Slf4j
@Component
@AllArgsConstructor
public class DocumentLoader {

    private final ResourcePatternResolver resourcePatternResolver;

    /**
     * 加载文档
     *
     * @return
     */
    public List<Document> loadDocuments() {
        List<Document> documents = new ArrayList<>();
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:doc/*.md");
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        .withAdditionalMetadata("filename", filename)
                        .build();
                MarkdownDocumentReader markdownDocumentReader = new MarkdownDocumentReader(resource, config);
                documents.addAll(markdownDocumentReader.get());
            }
        } catch (IOException e) {
            log.error("文档加载失败", e);
        }
        return documents;
    }

}
