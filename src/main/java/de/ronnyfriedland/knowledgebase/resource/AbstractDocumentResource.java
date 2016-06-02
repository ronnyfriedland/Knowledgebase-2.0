/*
 * Copyright (c) 2015 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden, Germany
 * All rights reserved.
 */
package de.ronnyfriedland.knowledgebase.resource;

import java.util.Collection;

import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.repository.IRepository;

/**
 * @author rofr &lt;Ronny.Friedland@t-systems.com&gt;
 */
public abstract class AbstractDocumentResource<T> extends AbstractResource {
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
    protected Collection<T> retrieveData(Integer offset, Integer limit, final String tag,
            final String search) throws DataException {
        if (null == offset) {
            offset = 0;
        }
        if (null == limit) {
            limit = 10;
        }

        Collection<T> documents;
        if (null != search) {
            documents = getRepository().searchDocuments(offset, limit, search);
        } else {
            documents = getRepository().listDocuments(offset, limit, tag);
        }
        return documents;
    }

    protected abstract IRepository<T> getRepository();
}
