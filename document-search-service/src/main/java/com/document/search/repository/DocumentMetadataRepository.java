package com.document.search.repository;

import com.document.search.model.DocumentMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentMetadataRepository extends MongoRepository<DocumentMetadata, String>{
}
