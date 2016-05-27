/*
 * Copyright (c) 2015 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden, Germany
 * All rights reserved.
 */
package de.ronnyfriedland.knowledgebase.resource;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.repository.IRepository;

/**
 * @author rofr &lt;Ronny.Friedland@t-systems.com&gt;
 */
public abstract class AbstractDocumentResource extends AbstractResource {
    @Autowired
    @Qualifier("jcr")
    private IRepository<String> repository;

    /**
     * Retrieve data based on input parameters.
     *
     * @param offset the offset
     * @param limit the limit
     * @param tag the tags
     * @param search the search
     * @return list of documents
     * @throws DataException if an error occurs retrieving the documents
     */
    protected Collection<Document<String>> retrieveData(Integer offset, Integer limit, final String tag,
            final String search) throws DataException {
        if (null == offset) {
            offset = 0;
        }
        if (null == limit) {
            limit = 10;
        }

        Collection<Document<String>> documents;
        if (null != search) {
            documents = repository.searchDocuments(offset, limit, search);
        } else {
            documents = repository.listDocuments(offset, limit, tag);
        }
        return documents;
    }
}
