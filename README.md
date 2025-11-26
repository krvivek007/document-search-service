This project consists of two modules:

* document-search-service
* gateway

The document-search-service is a multi-tenant service that exposes the following endpoints:

* POST /documents – Index a new document

* GET /search?q={query} – Search documents

* GET /documents/{id} – Retrieve document details

* DELETE /documents/{id} – Remove a document

For all these endpoints, the tenant ID is provided through the X-Tenant header.

The idea is to have the gateway generate the X-Tenant header after authenticating the client.

Document service uses mongo to store metadata and elastic to store document indexes.
