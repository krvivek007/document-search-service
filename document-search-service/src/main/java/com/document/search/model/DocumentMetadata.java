package com.document.search.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Builder
@Data
public class DocumentMetadata {
    @Id
    private String id; // mongodb document id
    private String path;
    private Status status;
}
