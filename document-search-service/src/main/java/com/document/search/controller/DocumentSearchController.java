package com.document.search.controller;

import com.document.search.model.DocumentMetadata;
import com.document.search.service.DocumentSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/search")
public class DocumentSearchController {
    private final DocumentSearchService documentSearchService;


    public DocumentSearchController(DocumentSearchService documentSearchService) {
        this.documentSearchService = documentSearchService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/search")
    public Set<DocumentMetadata> searchDocument(@RequestParam String q) {
       return  documentSearchService.search(q);
    }
}
