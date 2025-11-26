package com.document.search.repository.elastic;

import com.document.search.model.elastic.DocumentEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DocumentRepository
        extends ElasticsearchRepository<DocumentEntity, String> {
}
