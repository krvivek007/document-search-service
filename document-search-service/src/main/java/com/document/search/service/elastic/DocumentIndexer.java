package com.document.search.service.elastic;

import com.document.search.model.elastic.DocumentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DocumentIndexer {
    @Autowired
    private ElasticsearchOperations operations;

    @Autowired
    private TenantIndexResolver indexResolver;

    public String indexDocument(DocumentEntity doc) {
        IndexQuery query = new IndexQuery();
        query.setId(doc.getId());
        query.setObject(doc);
        String index = indexResolver.getIndex("documents");
        return operations.index(query, IndexCoordinates.of(index));
    }

    public DocumentEntity getDocument(String id) {
        String index = indexResolver.getIndex("documents");
        return operations.get(id, DocumentEntity.class, IndexCoordinates.of(index));
    }

    public List<DocumentEntity> search(String text) {
        String index = indexResolver.getIndex("documents");
        Criteria criteria = new Criteria("content").matches(text);
        Query query = new CriteriaQuery(criteria);
        SearchHits<DocumentEntity> hits =
                operations.search(query, DocumentEntity.class, IndexCoordinates.of(index));
        return hits.getSearchHits().stream()
                .map(hit -> hit.getContent())
                .toList();
    }

}

