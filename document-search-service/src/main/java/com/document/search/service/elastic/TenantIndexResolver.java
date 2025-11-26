package com.document.search.service.elastic;

import com.document.search.config.multitenant.TenantContext;
import org.springframework.stereotype.Component;

@Component
public class TenantIndexResolver {

    public String getIndex(String baseIndex) {
        String tenant = TenantContext.getTenantId();
        if (tenant == null) {
            throw new RuntimeException("Tenant header missing");
        }
        return baseIndex + "_" + tenant.toLowerCase();
    }
}
