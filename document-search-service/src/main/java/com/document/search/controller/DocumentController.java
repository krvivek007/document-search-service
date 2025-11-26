package com.document.search.controller;

import com.document.search.model.DocumentMetadata;
import com.document.search.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public DocumentMetadata indexDocument(@RequestParam("document") MultipartFile document) {
        return documentService.index(document);
    }

    @GetMapping(value = "/{id}")
    public DocumentMetadata getDocument(@PathVariable String id) {
        return documentService.getById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Document with ID %s not found", id)
        ));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void deleteDocument(@PathVariable String id) {
        documentService.deleteById(id);
    }


}
