package com.document.search.service;

import com.document.search.model.DocumentMetadata;
import com.document.search.repository.DocumentMetadataRepository;
import com.document.search.service.elastic.DocumentIndexer;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DocumentSearchService {
    private final DocumentIndexer documentIndexer;
    private final DocumentMetadataRepository documentMetadataRepository;

    public DocumentSearchService(DocumentIndexer documentIndexer, DocumentMetadataRepository documentMetadataRepository) {
        this.documentIndexer = documentIndexer;
        this.documentMetadataRepository = documentMetadataRepository;
    }

    public Set<DocumentMetadata> search(String q) {
        return documentIndexer.search(q)
                .stream()
                .map(x -> documentMetadataRepository.findById(x.getId()))
                .filter(x -> x.isPresent())
                .map(x -> x.get())
                .collect(Collectors.toSet());
    }
}
