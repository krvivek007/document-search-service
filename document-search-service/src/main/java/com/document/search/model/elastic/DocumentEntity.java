package com.document.search.model.elastic;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "documents")
public class DocumentEntity {

    @Id
    private String id;
    private String tenantId;
    private String content;
}
