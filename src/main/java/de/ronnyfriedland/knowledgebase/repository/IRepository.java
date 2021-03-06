package de.ronnyfriedland.knowledgebase.repository;

import java.util.Collection;

import de.ronnyfriedland.knowledgebase.exception.DataException;
import de.ronnyfriedland.knowledgebase.resource.RepositoryMetadata;

/**
 * Defines the interface for repository access.
 *
 * @author ronnyfriedland
 */
public interface IRepository<T> {

    /**
     * Retrieves the document by the given id
     *
     * @param id the unique identifier of the document
     * @return the document
     * @throws DataException if an error occurs retrieving the document
     */
    T getDocument(final String id) throws DataException;

    /**
     * Saves the given document
     *
     * @param message the document to save
     * @return the unique identifier of the document
     * @throws DataException if an error occurs retrieving the document
     */
    String saveDocument(final T message) throws DataException;

    /**
     * Retrieves all documents in the repository
     *
     * @param offset the offset
     * @param max the result limit
     * @param tag the tag filter
     * @return a list of documents
     * @throws DataException if an error occurs retrieving the documents
     */
    Collection<T> listDocuments(final int offset, final int max, String tag) throws DataException;

    /**
     * Retrieves all documents in the repository based on the search string
     *
     * @param offset the offset
     * @param max the result limit
     * @param search the search string
     * @return a list of documents
     * @throws DataException if an error occurs retrieving the documents
     */
    Collection<T> searchDocuments(final int offset, final int max, String search)
            throws DataException;

    /**
     * Removes the given document from the repository
     *
     * @param id the unique identifier of the document
     * @throws DataException if an error occurs removing the document
     */
    void removeDocument(final String id) throws DataException;

    /**
     * List metadata of documents in repository
     *
     * @param id the unique identifier of the document
     * @return repository metadata
     * @throws DataException if an error occurs retrieving the documents
     */
    RepositoryMetadata getMetadata(final String id) throws DataException;
}