package com.document.search.repository;

import com.document.search.model.TenantConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TenantConfigRepository extends MongoRepository<TenantConfig, String>{
}
