package com.document.search.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Builder
@Data
public class TenantConfig {
    @Id
    private String id;
    private String tenantId;
    private String uri;
    private String databaseName;
}
