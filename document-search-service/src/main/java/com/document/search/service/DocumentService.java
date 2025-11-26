package com.document.search.service;

import com.document.search.model.DocumentMetadata;
import com.document.search.repository.DocumentMetadataRepository;
import com.document.search.model.Status;
import com.document.search.service.storage.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@Service
public class DocumentService {
    private final StorageService storageService;
    private final DocumentMetadataRepository documentMetadataRepository;

    public DocumentService(StorageService storageService, DocumentMetadataRepository documentMetadataRepository) {
        this.storageService = storageService;
        this.documentMetadataRepository = documentMetadataRepository;
    }
    public void deleteById(String id){
        Optional<DocumentMetadata> documentMetadata = documentMetadataRepository.findById(id);
        if(documentMetadata.isPresent()){
            documentMetadata.get().setStatus(Status.DELETED);
            documentMetadataRepository.save(documentMetadata.get());
        }
        //Trigger to delete it from elastic search index.
    }
    public DocumentMetadata index(MultipartFile document){
        String filePath = storageService.store(document);
        DocumentMetadata metadata = DocumentMetadata.builder()
                .path(filePath)
                .status(Status.QUEUED)
                .build();
        //Send notification to indexing service via message queue.
        return documentMetadataRepository.save(metadata);
    }

    public Optional<DocumentMetadata> getById(String id) {
        return documentMetadataRepository.findById(id);
    }
}
