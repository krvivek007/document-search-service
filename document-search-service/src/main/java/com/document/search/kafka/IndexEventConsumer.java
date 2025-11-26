package com.document.search.kafka;

import com.document.search.config.multitenant.TenantContext;
import com.document.search.model.DocumentMetadata;
import com.document.search.repository.DocumentMetadataRepository;
import com.document.search.model.elastic.DocumentEntity;
import com.document.search.service.elastic.DocumentIndexer;
import com.document.search.service.storage.StorageService;
import document.search.avro.model.DocumentIndexEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Optional;

@Slf4j
@Service
public class IndexEventConsumer {
    protected final DocumentIndexer documentIndexer;
    protected final StorageService storageService;
    protected final DocumentMetadataRepository documentMetadataRepository;

    public IndexEventConsumer(DocumentIndexer documentIndexer, StorageService storageService,
                              DocumentMetadataRepository documentMetadataRepository) {
        this.documentMetadataRepository = documentMetadataRepository;
        this.storageService = storageService;
        this.documentIndexer = documentIndexer;
    }

    @KafkaListener(topics = "${topic.product}")
    public void consume(@Payload DocumentIndexEvent indexEvent, Acknowledgment ack) {
        log.info("Consumed: " + indexEvent.getHeader().getId());
        try {
            TenantContext.setTenantId(indexEvent.getBody().getTenentId());
            Optional<DocumentMetadata> documentMetadata = documentMetadataRepository.findById(indexEvent.getBody().getDocumentId());
            if (documentMetadata.isEmpty()) {
                log.error("Document metadata not found for ID: " + indexEvent.getBody().getDocumentId());
                ack.acknowledge();
                return;
            }
            Resource resource = storageService.loadAsResource(documentMetadata.get().getPath());
            DocumentEntity entity = new DocumentEntity();
            entity.setId(documentMetadata.get().getId());
            entity.setContent(resource.getContentAsString(Charset.defaultCharset()));
            entity.setTenantId(TenantContext.getTenantId());
            documentIndexer.indexDocument(entity);
            ack.acknowledge();
        } catch (Exception ex) {
            log.error("Error processing index event: " + ex.getMessage());
        } finally {
            TenantContext.clear();
        }
    }
}
