package de.ronnyfriedland.knowledgebase.repository;

import java.util.Collection;

import de.ronnyfriedland.knowledgebase.entity.Document;
import de.ronnyfriedland.knowledgebase.exception.DataException;

/**
 * Defines the interface for repository access.
 *
 * @author ronnyfriedland
 */
public interface IRepository {

    /**
     * Retrieves the (text-based) document by the given id
     *
     * @param id the unique identifier of the document
     * @return the document
     * @throws DataException if an error occurs retrieving the document
     */
    Document<String> getTextDocument(final String id) throws DataException;

    /**
     * Saves the given document
     *
     * @param message the document to save
     * @return the unique identifier of the document
     * @throws DataException if an error occurs retrieving the document
     */
    String saveTextDocument(final Document<String> message) throws DataException;

    /**
     * Retrieves all documents in the repository
     *
     * @param offset the offset
     * @param max the result limit
     * @return a list of documents
     * @throws DataException if an error occurs retrieving the document
     */
    Collection<Document<String>> listTextDocuments(final int offset, final int max) throws DataException;

    void removeDocument(final String id) throws DataException;
}